// src/app/admin/categories-admin/categories-admin.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoriesService, Category } from '../../categories/categories';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-categories-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categories-admin.html',
  styleUrls: ['./categories-admin.scss']
})
export class CategoriesAdminComponent {
  categories: Category[] = [];
  newCategory = '';
  isLoading = false;
  error = '';

constructor(
  private categoriesService: CategoriesService,
  private auth: AuthService
) {
  this.refresh();
}


  refresh() {
    this.isLoading = true;
    this.categoriesService.list().subscribe({
      next: cats => { this.categories = cats; this.isLoading = false; },
      error: err => { this.error = 'Błąd pobierania kategorii'; this.isLoading = false; }
    });
  }

  addCategory() {
    if (!this.newCategory.trim()) return;
    this.categoriesService.add({ name: this.newCategory }).subscribe({
      next: () => {
        this.newCategory = '';
        this.refresh();
      }
    });
  }

  deleteCategory(id: number) {
    if (!confirm('Na pewno usunąć kategorię?')) return;
    this.categoriesService.delete(id).subscribe({
      next: () => this.refresh()
    });
  }
  get isAdmin() {
    return this.auth.isAdmin();
  }
}
