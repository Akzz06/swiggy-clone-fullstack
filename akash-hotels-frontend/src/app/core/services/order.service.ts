import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Order, CreateOrderRequest, OrderStatus, AssignPartnerRequest } from '../models/models';

const BASE = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private http = inject(HttpClient);

  placeOrder(req: CreateOrderRequest) { return this.http.post<Order>(`${BASE}/orders`, req); }
  getMyOrders() { return this.http.get<Order[]>(`${BASE}/orders`); }
  getOrderById(id: number) { return this.http.get<Order>(`${BASE}/orders/${id}`); }
  cancelOrder(id: number) { return this.http.post<Order>(`${BASE}/orders/${id}/cancel`, {}); }

  getAllOrders() { return this.http.get<Order[]>(`${BASE}/admin/orders`); }
  updateStatus(id: number, status: OrderStatus) {
    return this.http.patch<Order>(`${BASE}/admin/orders/${id}/status`, { status });
  }
  assignPartner(orderId: number, req: AssignPartnerRequest) {
    return this.http.post<Order>(`${BASE}/admin/orders/${orderId}/assign`, req);
  }
}
