import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CategoryStats {
  categoryName: string;
  totalAmount: number;
  transactionCount: number;
}

export interface MonthlyStats {
  year: number;
  month: number;
  totalIncome: number;
  totalExpenses: number;
  balance: number;
}

export interface OverallStats {
  totalIncome: number;
  totalExpenses: number;
  currentBalance: number;
  totalTransactions: number;
  expensesByCategory: CategoryStats[];
  incomeByCategory: CategoryStats[];
  monthlyStats: MonthlyStats[];
}

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {
  private apiUrl = 'http://localhost:8080/api/statistics';

  constructor(private http: HttpClient) {}

  getOverallStats(): Observable<OverallStats> {
    return this.http.get<OverallStats>(`${this.apiUrl}/overview`);
  }

  getCategoryStats(type: 'INCOME' | 'EXPENSE'): Observable<CategoryStats[]> {
    return this.http.get<CategoryStats[]>(`${this.apiUrl}/categories/${type}`);
  }

  getMonthlyStats(): Observable<MonthlyStats[]> {
    return this.http.get<MonthlyStats[]>(`${this.apiUrl}/monthly-detailed`);
  }

  getSummary(params?: any): Observable<any> {
    const queryParams = params ? new URLSearchParams(params).toString() : '';
    const url = queryParams ? `${this.apiUrl}/summary?${queryParams}` : `${this.apiUrl}/summary`;
    return this.http.get(url);
  }

  getMonthlySummary(params?: any): Observable<any> {
    const queryParams = params ? new URLSearchParams(params).toString() : '';
    const url = queryParams ? `${this.apiUrl}/monthly?${queryParams}` : `${this.apiUrl}/monthly`;
    return this.http.get(url);
  }

  getByCategory(params?: any): Observable<any> {
    const queryParams = params ? new URLSearchParams(params).toString() : '';
    const url = queryParams ? `${this.apiUrl}/by-category?${queryParams}` : `${this.apiUrl}/by-category`;
    return this.http.get(url);
  }
}
