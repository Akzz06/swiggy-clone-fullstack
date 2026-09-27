import { Component, signal, computed, inject, HostListener, ElementRef, ViewChild } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { CartService } from '../../core/services/cart.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, FormsModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class NavbarComponent {
  auth = inject(AuthService);
  cartService = inject(CartService);
  router = inject(Router);

  searchQuery = '';
  showUserMenu = signal(false);

  userInitial = computed(() => {
    const name = this.auth.currentUser()?.name;
    return name ? name.charAt(0).toUpperCase() : '?';
  });

  userFirstName = computed(() => {
    const name = this.auth.currentUser()?.name;
    return name ? name.split(' ')[0] : '';
  });

  onSearch() {
    // Emit search query via router query params to home page
    if (this.searchQuery.trim()) {
      this.router.navigate(['/'], { queryParams: { q: this.searchQuery } });
    }
  }

  toggleUserMenu() {
    this.showUserMenu.update(v => !v);
  }

  logout() {
    this.showUserMenu.set(false);
    this.auth.logout();
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (!target.closest('.user-menu')) {
      this.showUserMenu.set(false);
    }
  }
}
