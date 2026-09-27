import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { AuthResponse, LoginRequest, RegisterRequest, Role } from '../models/models';

const TOKEN_KEY = 'akash_token';
const USER_KEY = 'akash_user';
const API = 'http://localhost:8080/api/auth';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  // Reactive auth state via Angular signals
  private _currentUser = signal<AuthResponse | null>(this.loadUserFromStorage());
  readonly currentUser = this._currentUser.asReadonly();

  readonly isLoggedIn = computed(() => !!this._currentUser());
  readonly userRole = computed<Role | null>(() => this._currentUser()?.role ?? null);
  readonly isAdmin = computed(() => this._currentUser()?.role === 'ROLE_HOTEL_ADMIN');
  readonly isCustomer = computed(() => this._currentUser()?.role === 'ROLE_CUSTOMER');
  readonly isDeliveryPartner = computed(() => this._currentUser()?.role === 'ROLE_DELIVERY_PARTNER');

  login(data: LoginRequest) {
    return this.http.post<AuthResponse>(`${API}/login`, data).pipe(
      tap((res) => this.setSession(res))
    );
  }

  register(data: RegisterRequest) {
    return this.http.post<AuthResponse>(`${API}/register`, data).pipe(
      tap((res) => this.setSession(res))
    );
  }

  logout() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this._currentUser.set(null);
    this.router.navigate(['/']);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  private setSession(res: AuthResponse) {
    localStorage.setItem(TOKEN_KEY, res.token);
    localStorage.setItem(USER_KEY, JSON.stringify(res));
    this._currentUser.set(res);
  }

  private loadUserFromStorage(): AuthResponse | null {
    try {
      const raw = localStorage.getItem(USER_KEY);
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  }
}
