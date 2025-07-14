import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { StatisticsService } from '../statistics/statistics.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  userStats = {
    income: 0,
    expense: 0,
    balance: 0,
    loading: false
  };
  
  constructor(
    private router: Router, 
    private authService: AuthService,
    private statisticsService: StatisticsService
  ) {}

  ngOnInit() {
    if (this.isLoggedIn()) {
      this.loadUserStats();
    }
  }

  async loadUserStats() {
    this.userStats.loading = true;
    try {
      const data = await firstValueFrom(this.statisticsService.getSummary());
      this.userStats.income = data.income || 0;
      this.userStats.expense = data.expense || 0;
      this.userStats.balance = data.balance || 0;
    } catch (error) {
      console.error('Błąd podczas ładowania statystyk:', error);
    } finally {
      this.userStats.loading = false;
    }
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }

  navigateToRegister() {
    this.router.navigate(['/register']);
  }

  navigateToTransactions() {
    this.router.navigate(['/transactions']);
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  getUserEmail(): string {
    return this.authService.getCurrentUserEmail() || '';
  }

  refreshStats() {
    if (this.isLoggedIn()) {
      this.loadUserStats();
    }
  }

  onSummaryCardClick() {
    if (this.isLoggedIn()) {
      this.navigateToTransactions();
    } else {
      this.navigateToLogin();
    }
  }
}
