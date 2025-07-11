import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { StatisticsService, OverallStats, CategoryStats, MonthlyStats } from './statistics.service';
import { BaseChartDirective } from 'ng2-charts';
import { Chart, ChartConfiguration, ChartData, ChartType, registerables } from 'chart.js';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../auth/auth.service';


Chart.register(...registerables);

@Component({
  selector: 'app-statistics',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.scss']
})
export class StatisticsComponent implements OnInit {
  overallStats: OverallStats | null = null;
  categoryExpenses: CategoryStats[] = [];
  monthlyStats: MonthlyStats[] = [];
  loading = true;
  error: string | null = null;

  // Konfiguracja wykresu kołowego
  public pieChartType: ChartType = 'pie';
  public pieChartData: ChartData<'pie'> = {
    labels: [],
    datasets: [{
      data: [],
      backgroundColor: [
        '#FF6384',
        '#36A2EB',
        '#FFCE56',
        '#4BC0C0',
        '#9966FF',
        '#FF9F40',
        '#FF6384',
        '#C9CBCF'
      ]
    }]
  };
  public pieChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom'
      },
      tooltip: {
        callbacks: {
          label: (context) => {
            const label = context.label || '';
            const value = context.parsed as number;
            const total = (context.dataset.data as number[]).reduce((a: number, b: number) => a + b, 0);
            const percentage = total > 0 ? ((value / total) * 100).toFixed(1) : '0.0';
            return `${label}: ${value.toFixed(2)} PLN (${percentage}%)`;
          }
        }
      }
    }
  };

  // Konfiguracja wykresu słupkowego
  public barChartType: ChartType = 'bar';
  public barChartData: ChartData<'bar'> = {
    labels: [],
    datasets: [
      {
        label: 'Przychody',
        data: [],
        backgroundColor: '#4CAF50',
        borderColor: '#4CAF50',
        borderWidth: 1
      },
      {
        label: 'Wydatki',
        data: [],
        backgroundColor: '#F44336',
        borderColor: '#F44336',
        borderWidth: 1
      }
    ]
  };
  public barChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top'
      },
      tooltip: {
        callbacks: {
          label: (context) => {
            return `${context.dataset.label}: ${context.parsed.y.toFixed(2)} PLN`;
          }
        }
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          callback: function(value) {
            return value + ' PLN';
          }
        }
      }
    }
  };

  constructor(
    private statisticsService: StatisticsService,
    private router: Router,
    private auth: AuthService
  ) {}

  ngOnInit() {
    this.loadStatistics();
  }

  async loadStatistics() {
    this.loading = true;
    this.error = null;

    try {
      const [overall, expenses, monthly] = await Promise.all([
        firstValueFrom(this.statisticsService.getOverallStats()),
        firstValueFrom(this.statisticsService.getCategoryStats('EXPENSE')),
        firstValueFrom(this.statisticsService.getMonthlyStats())
      ]);

      this.overallStats = overall || null;
      this.categoryExpenses = expenses || [];
      this.monthlyStats = monthly || [];
      
      this.updateCharts();
      this.loading = false;
    } catch (error) {
      console.error('Error loading statistics:', error);
      this.error = 'Błąd podczas ładowania statystyk';
      this.loading = false;
    }
  }

  private updateCharts() {
    this.updatePieChart();
    this.updateBarChart();
  }

  private updatePieChart() {
    if (this.categoryExpenses.length > 0) {
      this.pieChartData = {
        labels: this.categoryExpenses.map(item => item.categoryName),
        datasets: [{
          data: this.categoryExpenses.map(item => Math.abs(item.totalAmount)),
          backgroundColor: [
            '#FF6384',
            '#36A2EB', 
            '#FFCE56',
            '#4BC0C0',
            '#9966FF',
            '#FF9F40',
            '#FF6384',
            '#C9CBCF'
          ]
        }]
      };
    }
  }

  private updateBarChart() {
    if (this.monthlyStats.length > 0) {
      const sortedStats = this.monthlyStats.sort((a, b) => {
        if (a.year !== b.year) return a.year - b.year;
        return a.month - b.month;
      });

      this.barChartData = {
        labels: sortedStats.map(item => this.getMonthName(item.month, item.year)),
        datasets: [
          {
            label: 'Przychody',
            data: sortedStats.map(item => item.totalIncome),
            backgroundColor: '#4CAF50',
            borderColor: '#4CAF50',
            borderWidth: 1
          },
          {
            label: 'Wydatki',
            data: sortedStats.map(item => Math.abs(item.totalExpenses)),
            backgroundColor: '#F44336',
            borderColor: '#F44336',
            borderWidth: 1
          }
        ]
      };
    }
  }

  getMonthName(month: number, year: number): string {
    const months = [
      'Sty', 'Lut', 'Mar', 'Kwi', 'Maj', 'Cze',
      'Lip', 'Sie', 'Wrz', 'Paź', 'Lis', 'Gru'
    ];
    return `${months[month - 1]} ${year}`;
  }

  refreshStatistics() {
    this.loadStatistics();
  }
  
  navigateToHome() {
    this.router.navigate(['/']);
  }
  
  navigateToTransactions() {
    this.router.navigate(['/transactions']);
  }
  
  logout() {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}