import { Component, signal, inject, OnInit, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../core/services/order.service';
import { DishService } from '../../core/services/dish.service';
import { DeliveryPartnerService } from '../../core/services/delivery-partner.service';
import { ToastService } from '../../core/services/toast.service';
import { Order, OrderStatus, DeliveryPartner, Dish, DishRequest } from '../../core/models/models';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css',
})
export class AdminDashboardComponent implements OnInit {
  private orderService = inject(OrderService);
  private dishService = inject(DishService);
  private partnerService = inject(DeliveryPartnerService);
  private toast = inject(ToastService);

  // Tab
  activeTab = signal<'orders' | 'dishes' | 'partners'>('orders');

  // Orders
  orders = signal<Order[]>([]);
  ordersLoading = signal(true);
  updatingOrderId = signal<number | null>(null);
  availablePartners = signal<DeliveryPartner[]>([]);
  assignPartnerId = signal<Record<number, number>>({});

  // Stats
  totalOrders = computed(() => this.orders().length);
  activeOrders = computed(() => this.orders().filter(o => !['DELIVERED','CANCELLED'].includes(o.orderStatus)).length);
  revenue = computed(() => this.orders().filter(o => o.orderStatus === 'DELIVERED').reduce((s, o) => s + o.totalAmount, 0));

  // Dishes
  dishes = signal<Dish[]>([]);
  dishesLoading = signal(false);
  showDishModal = signal(false);
  editingDish = signal<Dish | null>(null);
  dishForm = signal<DishRequest>({ hotelId: 1, name: '', description: '', price: 0, imageUrl: '', category: '', available: true });
  savingDish = signal(false);

  // Partners
  partners = signal<DeliveryPartner[]>([]);
  partnersLoading = signal(false);

  readonly orderStatuses: OrderStatus[] = ['PLACED','CONFIRMED','PREPARING','READY_FOR_PICKUP','ASSIGNED','OUT_FOR_DELIVERY','DELIVERED','CANCELLED'];

  ngOnInit() {
    this.loadOrders();
    this.loadDishes();
    this.loadPartners();
  }

  // ---- Orders ----
  loadOrders() {
    this.ordersLoading.set(true);
    this.orderService.getAllOrders().subscribe({
      next: (o) => { this.orders.set(o); this.ordersLoading.set(false); },
      error: () => this.ordersLoading.set(false),
    });
  }

  updateStatus(order: Order, status: string) {
    this.updatingOrderId.set(order.id);
    this.orderService.updateStatus(order.id, status as OrderStatus).subscribe({
      next: (updated) => {
        this.orders.update(list => list.map(o => o.id === order.id ? updated : o));
        this.updatingOrderId.set(null);
        this.toast.success('Status updated');
      },
      error: (err) => { this.updatingOrderId.set(null); this.toast.error(err?.error?.message || 'Update failed'); },
    });
  }

  assignPartner(order: Order) {
    const pid = this.assignPartnerId()[order.id];
    if (!pid) { this.toast.error('Select a partner first'); return; }
    this.updatingOrderId.set(order.id);
    this.orderService.assignPartner(order.id, { deliveryPartnerId: pid }).subscribe({
      next: (updated) => {
        this.orders.update(list => list.map(o => o.id === order.id ? updated : o));
        this.updatingOrderId.set(null);
        this.toast.success('Partner assigned');
      },
      error: (err) => { this.updatingOrderId.set(null); this.toast.error(err?.error?.message || 'Assign failed'); },
    });
  }

  setAssignPartner(orderId: number, partnerId: number) {
    this.assignPartnerId.update(m => ({ ...m, [orderId]: partnerId }));
  }

  // ---- Dishes ----
  loadDishes() {
    this.dishesLoading.set(true);
    this.dishService.getAll().subscribe({
      next: (d) => { this.dishes.set(d); this.dishesLoading.set(false); },
      error: () => this.dishesLoading.set(false),
    });
  }

  openNewDish() {
    this.editingDish.set(null);
    this.dishForm.set({ hotelId: 1, name: '', description: '', price: 0, imageUrl: '', category: '', available: true });
    this.showDishModal.set(true);
  }

  openEditDish(dish: Dish) {
    this.editingDish.set(dish);
    this.dishForm.set({ hotelId: dish.hotelId, name: dish.name, description: dish.description, price: dish.price, imageUrl: dish.imageUrl, category: dish.category, available: dish.available });
    this.showDishModal.set(true);
  }

  saveDish() {
    const form = this.dishForm();
    if (!form.name || !form.price) { this.toast.error('Name and price are required'); return; }
    this.savingDish.set(true);
    const obs = this.editingDish()
      ? this.dishService.update(this.editingDish()!.id, form)
      : this.dishService.create(form);
    obs.subscribe({
      next: () => { this.savingDish.set(false); this.showDishModal.set(false); this.loadDishes(); this.toast.success('Dish saved!'); },
      error: (err) => { this.savingDish.set(false); this.toast.error(err?.error?.message || 'Save failed'); },
    });
  }

  deleteDish(dish: Dish) {
    if (!confirm(`Delete "${dish.name}"?`)) return;
    this.dishService.delete(dish.id).subscribe({
      next: () => { this.dishes.update(d => d.filter(x => x.id !== dish.id)); this.toast.info('Dish deleted'); },
      error: (err) => this.toast.error(err?.error?.message || 'Delete failed'),
    });
  }

  updateDishForm(field: keyof DishRequest, value: unknown) {
    this.dishForm.update(f => ({ ...f, [field]: value }));
  }

  // ---- Partners ----
  loadPartners() {
    this.partnersLoading.set(true);
    this.partnerService.getAll().subscribe({
      next: (p) => {
        this.partners.set(p);
        this.availablePartners.set(p.filter(x => x.availabilityStatus === 'AVAILABLE' && x.active));
        this.partnersLoading.set(false);
      },
      error: () => this.partnersLoading.set(false),
    });
  }

  statusLabel(s: string) {
    const m: Record<string,string> = { PLACED:'Placed', CONFIRMED:'Confirmed', PREPARING:'Preparing', READY_FOR_PICKUP:'Ready', ASSIGNED:'Assigned', OUT_FOR_DELIVERY:'On Way', DELIVERED:'Delivered', CANCELLED:'Cancelled' };
    return m[s] ?? s;
  }

  fmt(n: number) { return `₹${n.toFixed(0)}`; }

  formatDate(d: string) {
    return new Date(d).toLocaleString('en-IN', { day:'numeric', month:'short', hour:'2-digit', minute:'2-digit' });
  }
}
