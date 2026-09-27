import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { Cart, CartItemRequest } from '../models/models';

const BASE = 'http://localhost:8080/api/cart';

@Injectable({ providedIn: 'root' })
export class CartService {
  private http = inject(HttpClient);

  private _cart = signal<Cart | null>(null);
  readonly cart = this._cart.asReadonly();
  readonly itemCount = computed(() => this._cart()?.itemCount ?? 0);
  readonly totalAmount = computed(() => this._cart()?.totalAmount ?? 0);

  load() {
    return this.http.get<Cart>(BASE).pipe(tap(c => this._cart.set(c)));
  }

  addItem(req: CartItemRequest) {
    return this.http.post<Cart>(`${BASE}/items`, req).pipe(tap(c => this._cart.set(c)));
  }

  updateItem(itemId: number, req: CartItemRequest) {
    return this.http.put<Cart>(`${BASE}/items/${itemId}`, req).pipe(tap(c => this._cart.set(c)));
  }

  removeItem(itemId: number) {
    return this.http.delete<Cart>(`${BASE}/items/${itemId}`).pipe(tap(c => this._cart.set(c)));
  }

  clear() {
    return this.http.delete<void>(BASE).pipe(tap(() => this._cart.set(null)));
  }

  reset() { this._cart.set(null); }
}
