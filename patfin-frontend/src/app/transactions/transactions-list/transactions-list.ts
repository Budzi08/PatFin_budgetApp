import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransactionsService, Transaction, TransactionFilter } from '../transactions.service';
import { CategoriesService, Category } from '../../categories/categories';
import { TransactionsAdd } from '../transactions-add/transactions-add';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-transactions-list',
  standalone: true,
  imports: [CommonModule, FormsModule, TransactionsAdd],
  templateUrl: './transactions-list.html',
  styleUrls: ['./transactions-list.scss']
})
export class TransactionsList {
  private service = inject(TransactionsService);
  private categoriesService = inject(CategoriesService);
  transactions = signal<Transaction[]>([]);
  categories = signal<Category[]>([]);
  isAdmin = false;
  
  // Filtry
  filters: TransactionFilter = {};
  showFilters = false;

  constructor(private auth: AuthService, private router: Router) {
    this.isAdmin = this.auth.isAdmin();
    this.loadCategories();
    this.refresh();
  }

  loadCategories() {
    this.categoriesService.getAll().subscribe({
      next: (data: any) => this.categories.set(data),
      error: (err: any) => console.error('Błąd podczas pobierania kategorii:', err)
    });
  }

  refresh() {
    if (this.hasActiveFilters()) {
      this.applyFilters();
    } else {
      this.service.getAll().subscribe({
        next: (data: any) => this.transactions.set(data),
        error: (err: any) => console.error('Błąd podczas pobierania transakcji:', err)
      });
    }
  }

  applyFilters() {
    if (this.hasActiveFilters()) {
      this.service.getFiltered(this.filters).subscribe({
        next: (data: any) => this.transactions.set(data),
        error: (err: any) => console.error('Błąd podczas filtrowania transakcji:', err)
      });
    } else {
      this.refresh(); // Użyj standardowego endpointu jeśli brak filtrów
    }
  }

  clearFilters() {
    this.filters = {};
    this.refresh();
  }

  hasActiveFilters(): boolean {
    return Object.values(this.filters).some(value => value !== undefined && value !== null && value !== '');
  }

  toggleFilters() {
    this.showFilters = !this.showFilters;
  }

  onTransactionAdded() {
    this.refresh();
  }

  delete(id: number) {
    this.service.delete(id).subscribe(() => this.refresh());
  }

  addCategory() {
    this.router.navigate(['/admin/categories']);
  }

  viewStatistics() {
    this.router.navigate(['/statistics']);
  }
  
  navigateToHome() {
    this.router.navigate(['/']);
  }
  
  logout() {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}
