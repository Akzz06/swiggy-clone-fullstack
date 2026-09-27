import { inject } from '@angular/core';
import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const requiredRole = route.data?.['role'] as string;
  if (!auth.isLoggedIn()) { router.navigate(['/login']); return false; }
  if (requiredRole && auth.currentUser()?.role !== requiredRole) {
    router.navigate(['/']);
    return false;
  }
  return true;
};
