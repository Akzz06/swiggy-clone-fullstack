import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Dish, DishRequest } from '../models/models';

const BASE = 'http://localhost:8080/api';

@Injectable({ providedIn: 'root' })
export class DishService {
  private http = inject(HttpClient);

  getAll() { return this.http.get<Dish[]>(`${BASE}/dishes`); }
  getById(id: number) { return this.http.get<Dish>(`${BASE}/dishes/${id}`); }
  create(data: DishRequest) { return this.http.post<Dish>(`${BASE}/dishes`, data); }
  update(id: number, data: DishRequest) { return this.http.put<Dish>(`${BASE}/dishes/${id}`, data); }
  delete(id: number) { return this.http.delete<void>(`${BASE}/dishes/${id}`); }
}
