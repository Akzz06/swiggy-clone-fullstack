import { Component, signal, computed, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DishService } from '../../core/services/dish.service';
import { CartService } from '../../core/services/cart.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { Dish } from '../../core/models/models';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class HomeComponent implements OnInit {
  private dishService = inject(DishService);
  private cartService = inject(CartService);
  private auth = inject(AuthService);
  private toast = inject(ToastService);
  private route = inject(ActivatedRoute);

  dishes = signal<Dish[]>([]);
  loading = signal(true);
  selectedCategory = signal('All');
  searchQuery = signal('');
  addingDishId = signal<number | null>(null);

  categories = computed(() => {
    const cats = [...new Set(this.dishes().map(d => d.category))];
    return ['All', ...cats];
  });

  filteredDishes = computed(() => {
    let list = this.dishes();
    if (this.selectedCategory() !== 'All') {
      list = list.filter(d => d.category === this.selectedCategory());
    }
    const q = this.searchQuery().toLowerCase();
    if (q) {
      list = list.filter(d => d.name.toLowerCase().includes(q) || d.description.toLowerCase().includes(q));
    }
    return list;
  });

  ngOnInit() {
    // Read search query from URL
    this.route.queryParams.subscribe(p => this.searchQuery.set(p['q'] || ''));
    // Load dishes
    this.dishService.getAll().subscribe({
      next: (dishes) => { this.dishes.set(dishes); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
    // Load cart if logged in
    if (this.auth.isLoggedIn() && this.auth.isCustomer()) {
      this.cartService.load().subscribe();
    }
  }

  addToCart(dish: Dish) {
    if (!this.auth.isLoggedIn()) {
      this.toast.info('Please log in to add items to cart');
      return;
    }
    if (!this.auth.isCustomer()) {
      this.toast.error('Only customers can add items to cart');
      return;
    }
    this.addingDishId.set(dish.id);
    this.cartService.addItem({ dishId: dish.id, quantity: 1 }).subscribe({
      next: () => {
        this.addingDishId.set(null);
        this.toast.success(`${dish.name} added to cart!`);
      },
      error: () => {
        this.addingDishId.set(null);
        this.toast.error('Could not add to cart');
      },
    });
  }

  removeFromCart(dish: Dish) {
    const cart = this.cartService.cart();
    if (!cart) return;
    const item = cart.items.find(i => i.dishId === dish.id);
    if (!item) return;

    this.addingDishId.set(dish.id);
    if (item.quantity <= 1) {
      this.cartService.removeItem(item.id).subscribe({
        next: () => {
          this.addingDishId.set(null);
          this.toast.info(`${dish.name} removed from cart`);
        },
        error: () => this.addingDishId.set(null),
      });
    } else {
      this.cartService.updateItem(item.id, { dishId: dish.id, quantity: item.quantity - 1 }).subscribe({
        next: () => this.addingDishId.set(null),
        error: () => this.addingDishId.set(null),
      });
    }
  }

  getItemInCart(dishId: number): number {
    const cart = this.cartService.cart();
    if (!cart) return 0;
    return cart.items.find(i => i.dishId === dishId)?.quantity ?? 0;
  }

  formatPrice(p: number) { return `₹${p.toFixed(0)}`; }

  // Category icons map
  categoryIcon(cat: string): string {
    const map: Record<string, string> = {
      All: '🍽️', Biryani: '🍚', Tiffin: '🥞', Starters: '🍗', Curry: '🍛',
      Meals: '🍱', Beverages: '🥤', Breads: '🫓',
    };
    return map[cat] ?? '🍴';
  }
}
