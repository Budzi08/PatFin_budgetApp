import { CommonModule } from '@angular/common';
import { Component, inject, signal, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TransactionsService } from '../transactions.service';
import { CategoriesService, Category } from '../../categories/categories';
import { ThemeService } from '../../core/theme.service';
import { FontSizeService } from '../../core/font-size.service';

@Component({
  selector: 'app-transactions-add',
  imports: [CommonModule, FormsModule],
  templateUrl: './transactions-add.html',
  styleUrl: './transactions-add.scss'
})
export class TransactionsAdd {
  private service = inject(TransactionsService);
  private categoriesService = inject(CategoriesService);
  public themeService = inject(ThemeService);
  public fontSizeService = inject(FontSizeService);

  @Output() transactionAdded = new EventEmitter<void>();

  categories = signal<Category[]>([]);

  amount: number = 0;
  description: string = '';
  date = '';
  type: string = 'EXPENSE';
  categoryId: number | null = null;
  success = false;
  showThemeDropdown = false;

  constructor() {
    this.categoriesService.getAll().subscribe({
      next: (data) => this.categories.set(data),
      error: (err) => console.error('Błąd podczas pobierania kategorii:', err)
    });
  }

  addTransaction() {
    if (!this.categoryId) return;
    const newTransaction = {
      amount: this.amount,
      description: this.description,
      date: this.date,
      type: this.type,
      categoryId: this.categoryId
    };

    this.service.add(newTransaction).subscribe({
      next: () => {
        this.success = true;
        this.amount = 0;
        this.description = '';
        this.date = '';
        this.type = 'EXPENSE';
        this.categoryId = 1;
        
        this.transactionAdded.emit();
        
        setTimeout(() => {
          this.success = false;
        }, 3000);
      },
      error: () => {
        this.success = false;
        console.error('Błąd podczas dodawania transakcji');
      }
    });
  }

  toggleThemeDropdown() {
    this.showThemeDropdown = !this.showThemeDropdown;
  }

  selectTheme(themeId: string) {
    this.themeService.applyTheme(themeId as any);
    this.showThemeDropdown = false;
  }
}
