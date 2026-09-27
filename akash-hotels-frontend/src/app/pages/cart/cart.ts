import { Component, signal, inject, OnInit, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CartService } from '../../core/services/cart.service';
import { ToastService } from '../../core/services/toast.service';
import { CartItem } from '../../core/models/models';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './cart.html',
  styleUrl: './cart.css',
})
export class CartComponent implements OnInit {
  cartService = inject(CartService);
  private toast = inject(ToastService);

  loading = signal(true);
  updatingId = signal<number | null>(null);

  cart = this.cartService.cart;
  items = computed(() => this.cart()?.items ?? []);
  subtotal = computed(() => this.items().reduce((s, i) => s + i.itemTotal, 0));

  ngOnInit() {
    this.cartService.load().subscribe({ next: () => this.loading.set(false), error: () => this.loading.set(false) });
  }

  updateQty(item: CartItem, delta: number) {
    const newQty = item.quantity + delta;
    if (newQty < 1) { this.remove(item); return; }
    this.updatingId.set(item.id);
    this.cartService.updateItem(item.id, { dishId: item.dishId, quantity: newQty }).subscribe({
      next: () => this.updatingId.set(null),
      error: () => { this.updatingId.set(null); this.toast.error('Failed to update quantity'); },
    });
  }

  remove(item: CartItem) {
    this.updatingId.set(item.id);
    this.cartService.removeItem(item.id).subscribe({
      next: () => { this.updatingId.set(null); this.toast.info(`${item.dishName} removed`); },
      error: () => { this.updatingId.set(null); this.toast.error('Failed to remove item'); },
    });
  }

  fmt(n: number) { return `₹${n.toFixed(0)}`; }
}
