import { Component, signal, inject } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class RegisterComponent {
  private auth = inject(AuthService);
  private toast = inject(ToastService);
  private router = inject(Router);

  form = { name: '', email: '', password: '', phone: '' };
  loading = signal(false);
  showPwd = signal(false);
  errorMsg = signal('');

  onSubmit() {
    this.errorMsg.set('');
    if (!this.form.name || !this.form.email || !this.form.password || !this.form.phone) {
      this.errorMsg.set('Please fill in all fields.');
      return;
    }
    if (this.form.password.length < 6) {
      this.errorMsg.set('Password must be at least 6 characters.');
      return;
    }
    this.loading.set(true);
    this.auth.register({ ...this.form, role: 'ROLE_CUSTOMER' }).subscribe({
      next: (res) => {
        this.loading.set(false);
        this.toast.success(`Welcome, ${res.name.split(' ')[0]}!`);
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMsg.set(err?.error?.message || 'Registration failed. Try again.');
      },
    });
  }
}
