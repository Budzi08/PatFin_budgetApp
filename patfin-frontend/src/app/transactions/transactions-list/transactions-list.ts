import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionsService, Transaction } from '../transactions.service';
import { TransactionsAdd } from '../transactions-add/transactions-add';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-transactions-list',
  standalone: true,
  imports: [CommonModule, TransactionsAdd],
  templateUrl: './transactions-list.html',
  styleUrls: ['./transactions-list.scss']
})
export class TransactionsList {
  private service = inject(TransactionsService);
  transactions = signal<Transaction[]>([]);
  isAdmin = false;

  constructor(private auth: AuthService, private router: Router) {
    this.isAdmin = this.auth.isAdmin();
    this.refresh();
  }

  refresh() {
    this.service.getAll().subscribe({
      next: (data) => this.transactions.set(data),
      error: (err) => console.error('Błąd podczas pobierania transakcji:', err)
    });
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
}
