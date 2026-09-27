import { Component, signal, inject, OnInit, computed } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { DeliveryPartnerService } from '../../core/services/delivery-partner.service';
import { ToastService } from '../../core/services/toast.service';
import { DeliveryPartner, Delivery, PartnerEarnings, AvailabilityStatus } from '../../core/models/models';

@Component({
  selector: 'app-delivery',
  standalone: true,
  imports: [],
  templateUrl: './delivery.html',
  styleUrl: './delivery.css',
})
export class DeliveryComponent implements OnInit {
  private auth = inject(AuthService);
  private partnerService = inject(DeliveryPartnerService);
  private toast = inject(ToastService);

  profile = signal<DeliveryPartner | null>(null);
  deliveries = signal<Delivery[]>([]);
  earnings = signal<PartnerEarnings | null>(null);
  loading = signal(true);
  activeTab = signal<'deliveries' | 'earnings'>('deliveries');
  updatingAvailability = signal(false);
  actionDeliveryId = signal<number | null>(null);

  activeDeliveries = computed(() =>
    this.deliveries().filter(d => !['DELIVERED', 'FAILED'].includes(d.deliveryStatus))
  );
  completedDeliveries = computed(() =>
    this.deliveries().filter(d => d.deliveryStatus === 'DELIVERED')
  );

  ngOnInit() {
    this.partnerService.getMyProfile().subscribe({
      next: (p) => {
        this.profile.set(p);
        this.loadDeliveries(p.id);
        this.loadEarnings(p.id);
      },
      error: () => this.loading.set(false),
    });
  }

  loadDeliveries(id: number) {
    this.partnerService.getMyDeliveries(id).subscribe({
      next: (d) => { this.deliveries.set(d); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  loadEarnings(id: number) {
    this.partnerService.getMyEarnings(id).subscribe({
      next: (e) => this.earnings.set(e),
    });
  }

  toggleAvailability() {
    const p = this.profile();
    if (!p) return;
    const newStatus: AvailabilityStatus = p.availabilityStatus === 'AVAILABLE' ? 'OFFLINE' : 'AVAILABLE';
    this.updatingAvailability.set(true);
    this.partnerService.updateAvailability(p.id, newStatus).subscribe({
      next: (updated) => {
        this.profile.set(updated);
        this.updatingAvailability.set(false);
        this.toast.success(`Status set to ${newStatus}`);
      },
      error: (err) => {
        this.updatingAvailability.set(false);
        this.toast.error(err?.error?.message || 'Could not update status');
      },
    });
  }

  accept(d: Delivery) {
    this.actionDeliveryId.set(d.id);
    this.partnerService.acceptDelivery(d.id).subscribe({
      next: (updated) => { this.updateDelivery(updated); this.actionDeliveryId.set(null); this.toast.success('Delivery accepted'); },
      error: (err) => { this.actionDeliveryId.set(null); this.toast.error(err?.error?.message || 'Failed'); },
    });
  }

  pickup(d: Delivery) {
    this.actionDeliveryId.set(d.id);
    this.partnerService.pickupDelivery(d.id).subscribe({
      next: (updated) => { this.updateDelivery(updated); this.actionDeliveryId.set(null); this.toast.success('Marked as picked up'); },
      error: (err) => { this.actionDeliveryId.set(null); this.toast.error(err?.error?.message || 'Failed'); },
    });
  }

  complete(d: Delivery) {
    this.actionDeliveryId.set(d.id);
    this.partnerService.completeDelivery(d.id).subscribe({
      next: (updated) => {
        this.updateDelivery(updated);
        this.actionDeliveryId.set(null);
        this.toast.success('Delivery completed! 🎉');
        // Refresh earnings
        if (this.profile()) this.loadEarnings(this.profile()!.id);
      },
      error: (err) => { this.actionDeliveryId.set(null); this.toast.error(err?.error?.message || 'Failed'); },
    });
  }

  private updateDelivery(updated: Delivery) {
    this.deliveries.update(list => list.map(d => d.id === updated.id ? updated : d));
  }

  statusLabel(s: string) {
    const m: Record<string,string> = { ASSIGNED:'Assigned', ACCEPTED:'Accepted', PICKED_UP:'Picked Up', DELIVERING:'Delivering', DELIVERED:'Delivered', FAILED:'Failed' };
    return m[s] ?? s;
  }

  fmt(n?: number) { return n != null ? `₹${n.toFixed(0)}` : '—'; }

  formatDate(d?: string) {
    if (!d) return '—';
    return new Date(d).toLocaleString('en-IN', { day:'numeric', month:'short', hour:'2-digit', minute:'2-digit' });
  }
}
