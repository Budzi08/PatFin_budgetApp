import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Transaction {
  id: number;
  amount: number;
  description: string;
  date: string;
  type: string;
  category?: { id: number, name: string };
}

export interface TransactionFilter {
  startDate?: string;
  endDate?: string;
  minAmount?: number;
  maxAmount?: number;
  type?: string;
  categoryId?: number;
}

@Injectable({ providedIn: 'root' })
export class TransactionsService {
  private apiUrl = 'http://localhost:8080/api/transactions';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(this.apiUrl);
  }

  add(transaction: any): Observable<Transaction> {
    return this.http.post<Transaction>(this.apiUrl, transaction);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getFiltered(filters: TransactionFilter): Observable<Transaction[]> {
    let params = new HttpParams();
    
    if (filters.startDate) params = params.set('startDate', filters.startDate);
    if (filters.endDate) params = params.set('endDate', filters.endDate);
    if (filters.minAmount !== undefined && filters.minAmount !== null) params = params.set('minAmount', filters.minAmount.toString());
    if (filters.maxAmount !== undefined && filters.maxAmount !== null) params = params.set('maxAmount', filters.maxAmount.toString());
    if (filters.type) params = params.set('type', filters.type);
    if (filters.categoryId !== undefined && filters.categoryId !== null) params = params.set('categoryId', filters.categoryId.toString());
    
    return this.http.get<Transaction[]>(`${this.apiUrl}/filter`, { params });
  }
}
