import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

export const adminGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  console.log('AdminGuard: Checking admin status...');
  const isAdmin = auth.isAdmin();
  console.log('AdminGuard: User is admin:', isAdmin);

  if (isAdmin) {
    return true;
  } else {
    console.log('AdminGuard: Access denied, redirecting to home');
    router.navigate(['/']);
    return false;
  }
};
