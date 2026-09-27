import { Component, signal, inject } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { CartService } from '../../core/services/cart.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class LoginComponent {
  private auth = inject(AuthService);
  private toast = inject(ToastService);
  private cartService = inject(CartService);
  private router = inject(Router);

  form = { email: '', password: '' };
  loading = signal(false);
  showPwd = signal(false);
  errorMsg = signal('');

  onSubmit() {
    this.errorMsg.set('');
    if (!this.form.email || !this.form.password) {
      this.errorMsg.set('Please fill in all fields.');
      return;
    }
    this.loading.set(true);
    this.auth.login(this.form).subscribe({
      next: (res) => {
        this.loading.set(false);
        // Load cart for customers
        if (res.role === 'ROLE_CUSTOMER') {
          this.cartService.load().subscribe();
        }
        this.toast.success(`Welcome back, ${res.name.split(' ')[0]}!`);
        if (res.role === 'ROLE_HOTEL_ADMIN') {
          this.router.navigate(['/admin']);
        } else if (res.role === 'ROLE_DELIVERY_PARTNER') {
          this.router.navigate(['/delivery']);
        } else {
          this.router.navigate(['/']);
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMsg.set(err?.error?.message || 'Invalid email or password.');
      },
    });
  }

  fillDemo(type: 'customer' | 'admin' | 'partner') {
    const map = {
      customer: { email: 'customer@gmail.com', password: 'Customer@123' },
      admin: { email: 'admin@akashhotels.com', password: 'Admin@123' },
      partner: { email: 'partner1@akashhotels.com', password: 'Partner@123' },
    };
    this.form = { ...map[type] };
  }
}
