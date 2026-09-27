import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DeliveryLocation } from '../models/models';

const BASE = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class LocationService {
  private http = inject(HttpClient);

  getAll(activeOnly = true) {
    return this.http.get<DeliveryLocation[]>(`${BASE}/locations`, { params: { activeOnly: String(activeOnly) } });
  }
}
