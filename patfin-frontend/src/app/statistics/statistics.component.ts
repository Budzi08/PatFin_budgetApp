import { Component, OnInit, signal, inject, ViewChild, ElementRef, AfterViewInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart, ChartConfiguration, ChartType, registerables } from 'chart.js';
import { StatisticsService, OverallStats, CategoryStats, MonthlyStats } from './statistics.service';

Chart.register(...registerables);

@Component({
  selector: 'app-statistics',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.scss']
})
export class StatisticsComponent implements OnInit, AfterViewInit, OnDestroy {
  
  private statisticsService = inject(StatisticsService);
  
  @ViewChild('pieChartCanvas', { static: false }) pieChartCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('barChartCanvas', { static: false }) barChartCanvas!: ElementRef<HTMLCanvasElement>;
  
  overallStats = signal<OverallStats | null>(null);
  loading = signal(true);
  error = signal<string | null>(null);

  private pieChart!: Chart;
  private barChart!: Chart;

  ngOnInit() {
    this.loadStatistics();
  }

  ngAfterViewInit() {
    // Inicjalizuj wykresy po załadowaniu danych
  }

  loadStatistics() {
    this.loading.set(true);
    this.error.set(null);

    this.statisticsService.getOverallStats().subscribe({
      next: (stats) => {
        this.overallStats.set(stats);
        this.updateCharts(stats);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Błąd podczas pobierania statystyk:', err);
        this.error.set('Nie udało się pobrać statystyk');
        this.loading.set(false);
      }
    });
  }

  private updateCharts(stats: OverallStats) {
    // Poczekaj na ViewChild
    setTimeout(() => {
      this.createPieChart(stats);
      this.createBarChart(stats);
    }, 100);
  }

  private createPieChart(stats: OverallStats) {
    if (!this.pieChartCanvas || !stats.expensesByCategory.length) return;

    // Zniszcz poprzedni wykres jeśli istnieje
    if (this.pieChart) {
      this.pieChart.destroy();
    }

    const ctx = this.pieChartCanvas.nativeElement.getContext('2d');
    if (!ctx) return;

    this.pieChart = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: stats.expensesByCategory.map(cat => cat.categoryName),
        datasets: [{
          data: stats.expensesByCategory.map(cat => cat.totalAmount),
          backgroundColor: [
            '#FF6384',
            '#36A2EB',
            '#FFCE56',
            '#4BC0C0',
            '#9966FF',
            '#FF9F40',
            '#FF6384',
            '#C9CBCF',
            '#4BC0C0',
            '#FF9F40'
          ],
          borderWidth: 2,
          borderColor: '#fff'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              padding: 20,
              usePointStyle: true
            }
          },
          title: {
            display: true,
            text: 'Wydatki według kategorii',
            font: {
              size: 16
            }
          }
        }
      }
    });
  }

  private createBarChart(stats: OverallStats) {
    if (!this.barChartCanvas || !stats.monthlyStats.length) return;

    // Zniszcz poprzedni wykres jeśli istnieje
    if (this.barChart) {
      this.barChart.destroy();
    }

    const ctx = this.barChartCanvas.nativeElement.getContext('2d');
    if (!ctx) return;

    // Sortuj po roku i miesiącu i weź ostatnie 12 miesięcy
    const sortedStats = stats.monthlyStats.sort((a, b) => {
      if (a.year !== b.year) return a.year - b.year;
      return a.month - b.month;
    });
    const last12Months = sortedStats.slice(-12);

    this.barChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: last12Months.map(stat => 
          `${this.getMonthName(stat.month)} ${stat.year}`
        ),
        datasets: [
          {
            label: 'Przychody',
            data: last12Months.map(stat => stat.totalIncome),
            backgroundColor: '#4CAF50',
            borderColor: '#4CAF50',
            borderWidth: 1
          },
          {
            label: 'Wydatki',
            data: last12Months.map(stat => stat.totalExpenses),
            backgroundColor: '#F44336',
            borderColor: '#F44336',
            borderWidth: 1
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: true,
            position: 'top'
          },
          title: {
            display: true,
            text: 'Miesięczne przychody vs wydatki',
            font: {
              size: 16
            }
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: function(value) {
                return value + ' zł';
              }
            }
          }
        }
      }
    });
  }

  getMonthName(monthNumber: number): string {
    const months = [
      'Sty', 'Lut', 'Mar', 'Kwi', 'Maj', 'Cze',
      'Lip', 'Sie', 'Wrz', 'Paź', 'Lis', 'Gru'
    ];
    return months[monthNumber - 1] || monthNumber.toString();
  }

  getFullMonthName(monthNumber: number): string {
    const months = [
      'Styczeń', 'Luty', 'Marzec', 'Kwiecień', 'Maj', 'Czerwiec',
      'Lipiec', 'Sierpień', 'Wrzesień', 'Październik', 'Listopad', 'Grudzień'
    ];
    return months[monthNumber - 1] || monthNumber.toString();
  }

  // Metoda do odświeżania danych
  refreshData() {
    this.loadStatistics();
  }

  // Zniszcz wykresy przy zniszczeniu komponentu
  ngOnDestroy() {
    if (this.pieChart) {
      this.pieChart.destroy();
    }
    if (this.barChart) {
      this.barChart.destroy();
    }
  }
}
