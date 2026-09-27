import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DeliveryPartner, DeliveryPartnerRequest, Delivery, PartnerEarnings, AvailabilityStatus } from '../models/models';

const BASE = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class DeliveryPartnerService {
  private http = inject(HttpClient);

  getMyProfile() { return this.http.get<DeliveryPartner>(`${BASE}/delivery-partners/me`); }
  getMyDeliveries(id: number) { return this.http.get<Delivery[]>(`${BASE}/delivery-partners/${id}/deliveries`); }
  getMyEarnings(id: number) { return this.http.get<PartnerEarnings>(`${BASE}/delivery-partners/${id}/earnings`); }
  updateAvailability(id: number, status: AvailabilityStatus) {
    return this.http.patch<DeliveryPartner>(`${BASE}/delivery-partners/${id}/availability`, { availabilityStatus: status });
  }
  acceptDelivery(deliveryId: number) { return this.http.post<Delivery>(`${BASE}/delivery-partners/deliveries/${deliveryId}/accept`, {}); }
  pickupDelivery(deliveryId: number) { return this.http.post<Delivery>(`${BASE}/delivery-partners/deliveries/${deliveryId}/pickup`, {}); }
  completeDelivery(deliveryId: number) { return this.http.post<Delivery>(`${BASE}/delivery-partners/deliveries/${deliveryId}/deliver`, {}); }

  getAll() { return this.http.get<DeliveryPartner[]>(`${BASE}/admin/delivery-partners`); }
  create(req: DeliveryPartnerRequest) { return this.http.post<DeliveryPartner>(`${BASE}/admin/delivery-partners`, req); }
  update(id: number, req: DeliveryPartnerRequest) { return this.http.put<DeliveryPartner>(`${BASE}/admin/delivery-partners/${id}`, req); }
  delete(id: number) { return this.http.delete<void>(`${BASE}/admin/delivery-partners/${id}`); }
}
