import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { OrderService } from '../../core/services/order.service';
import { ToastService } from '../../core/services/toast.service';
import { Order } from '../../core/models/models';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './orders.html',
  styleUrl: './orders.css',
})
export class OrdersComponent implements OnInit {
  private orderService = inject(OrderService);
  private toast = inject(ToastService);

  orders = signal<Order[]>([]);
  loading = signal(true);
  cancellingId = signal<number | null>(null);

  readonly statusSteps = [
    'PLACED', 'CONFIRMED', 'PREPARING', 'READY_FOR_PICKUP',
    'ASSIGNED', 'OUT_FOR_DELIVERY', 'DELIVERED',
  ];

  ngOnInit() {
    this.orderService.getMyOrders().subscribe({
      next: (o) => { this.orders.set(o); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  cancelOrder(id: number) {
    this.cancellingId.set(id);
    this.orderService.cancelOrder(id).subscribe({
      next: (updated) => {
        this.cancellingId.set(null);
        this.orders.update(list => list.map(o => o.id === id ? updated : o));
        this.toast.info('Order cancelled');
      },
      error: (err) => {
        this.cancellingId.set(null);
        this.toast.error(err?.error?.message || 'Cannot cancel this order');
      },
    });
  }

  canCancel(order: Order) {
    return !['DELIVERED', 'CANCELLED', 'OUT_FOR_DELIVERY'].includes(order.orderStatus);
  }

  stepIndex(status: string) {
    return this.statusSteps.indexOf(status);
  }

  statusLabel(status: string): string {
    const map: Record<string, string> = {
      PLACED: 'Order Placed',
      CONFIRMED: 'Confirmed',
      PREPARING: 'Preparing',
      READY_FOR_PICKUP: 'Ready for Pickup',
      ASSIGNED: 'Partner Assigned',
      OUT_FOR_DELIVERY: 'Out for Delivery',
      DELIVERED: 'Delivered',
      CANCELLED: 'Cancelled',
    };
    return map[status] ?? status;
  }

  statusIcon(status: string): string {
    const map: Record<string, string> = {
      PLACED: '📋', CONFIRMED: '✅', PREPARING: '👨‍🍳',
      READY_FOR_PICKUP: '📦', ASSIGNED: '🛵', OUT_FOR_DELIVERY: '🚀',
      DELIVERED: '🎉', CANCELLED: '❌',
    };
    return map[status] ?? '•';
  }

  fmt(n: number) { return `₹${n.toFixed(0)}`; }

  formatDate(d: string) {
    return new Date(d).toLocaleString('en-IN', {
      day: 'numeric', month: 'short', year: 'numeric',
      hour: '2-digit', minute: '2-digit',
    });
  }
}
