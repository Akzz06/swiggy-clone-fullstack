import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/home/home').then(m => m.HomeComponent),
  },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login').then(m => m.LoginComponent),
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register/register').then(m => m.RegisterComponent),
  },
  {
    path: 'cart',
    loadComponent: () => import('./pages/cart/cart').then(m => m.CartComponent),
    canActivate: [authGuard],
  },
  {
    path: 'checkout',
    loadComponent: () => import('./pages/checkout/checkout').then(m => m.CheckoutComponent),
    canActivate: [authGuard],
  },
  {
    path: 'orders',
    loadComponent: () => import('./pages/orders/orders').then(m => m.OrdersComponent),
    canActivate: [authGuard],
  },
  {
    path: 'admin',
    loadComponent: () => import('./pages/admin/admin-dashboard').then(m => m.AdminDashboardComponent),
    canActivate: [roleGuard],
    data: { role: 'ROLE_HOTEL_ADMIN' },
  },
  {
    path: 'delivery',
    loadComponent: () => import('./pages/delivery/delivery').then(m => m.DeliveryComponent),
    canActivate: [roleGuard],
    data: { role: 'ROLE_DELIVERY_PARTNER' },
  },
  {
    path: '**',
    redirectTo: '',
  },
];
