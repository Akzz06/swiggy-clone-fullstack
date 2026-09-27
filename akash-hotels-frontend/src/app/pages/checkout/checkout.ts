import { Component, signal, inject, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../core/services/cart.service';
import { OrderService } from '../../core/services/order.service';
import { LocationService } from '../../core/services/location.service';
import { ToastService } from '../../core/services/toast.service';
import { DeliveryLocation, PaymentMethod } from '../../core/models/models';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [RouterLink, FormsModule],
  templateUrl: './checkout.html',
  styleUrl: './checkout.css',
})
export class CheckoutComponent implements OnInit {
  private cartService = inject(CartService);
  private orderService = inject(OrderService);
  private locationService = inject(LocationService);
  private toast = inject(ToastService);
  private router = inject(Router);

  locations = signal<DeliveryLocation[]>([]);
  selectedLocationId = signal<number | null>(null);
  paymentMethod = signal<PaymentMethod>('MOCK_PAYMENT');
  loading = signal(false);
  loadingLocations = signal(true);

  cart = this.cartService.cart;

  paymentMethods: { value: PaymentMethod; label: string; icon: string }[] = [
    { value: 'MOCK_PAYMENT', label: 'Mock Payment (Test)', icon: '🧪' },
    { value: 'UPI', label: 'UPI', icon: '📱' },
    { value: 'CARD', label: 'Credit / Debit Card', icon: '💳' },
    { value: 'CASH', label: 'Cash on Delivery', icon: '💵' },
  ];

  ngOnInit() {
    this.locationService.getAll().subscribe({
      next: locs => { this.locations.set(locs); this.loadingLocations.set(false); },
      error: () => this.loadingLocations.set(false),
    });
    if (!this.cart()) {
      this.cartService.load().subscribe();
    }
  }

  selectedLocation() {
    return this.locations().find(l => l.id === this.selectedLocationId());
  }

  deliveryFeePreview(): number {
    const dist = this.selectedLocation()?.distanceFromHotelKm ?? 0;
    if (dist <= 3) return 30;
    if (dist <= 5) return 40;
    if (dist <= 8) return 50;
    if (dist <= 12) return 65;
    return 80;
  }

  subtotal(): number {
    return this.cart()?.totalAmount ?? 0;
  }

  placeOrder() {
    if (!this.selectedLocationId()) {
      this.toast.error('Please select a delivery location');
      return;
    }
    this.loading.set(true);
    this.orderService.placeOrder({
      deliveryLocationId: this.selectedLocationId()!,
      paymentMethod: this.paymentMethod(),
    }).subscribe({
      next: (order) => {
        this.loading.set(false);
        this.cartService.reset();
        this.toast.success('Order placed successfully!');
        this.router.navigate(['/orders']);
      },
      error: (err) => {
        this.loading.set(false);
        this.toast.error(err?.error?.message || 'Failed to place order');
      },
    });
  }
}
