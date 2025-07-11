import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  email = '';
  password = '';
  error = '';
  isLoading = false;
  showPassword = false;

  constructor(private authService: AuthService, private router: Router) { }

  onLogin() {
    if (!this.email || !this.password) {
      this.error = 'Proszę wypełnić wszystkie pola!';
      return;
    }

    if (!this.isValidEmail(this.email)) {
      this.error = 'Proszę wprowadzić poprawny adres e-mail!';
      return;
    }

    this.isLoading = true;
    this.error = '';
    
    this.authService.login(this.email, this.password).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/transactions']);
      },
      error: (error) => {
        this.isLoading = false;
        console.log('Login error:', error);
        
        // Sprawdzamy czy mamy błąd z backendu w formacie JSON
        if (error.error && error.error.message) {
          this.error = this.translateError(error.error.message);
        } else {
          this.error = 'Błędny e-mail lub hasło. Spróbuj ponownie!';
        }
      }
    });
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  private translateError(englishMessage: string): string {
    const translations: { [key: string]: string } = {
      'Invalid email or password': 'Błędny e-mail lub hasło. Spróbuj ponownie!',
      'User not found': 'Użytkownik nie został znaleziony',
      'Account disabled': 'Konto zostało wyłączone'
    };

    return translations[englishMessage] || 'Wystąpił błąd podczas logowania. Spróbuj ponownie!';
  }

  private isValidEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  navigateToHome() {
    this.router.navigate(['/']);
  }

  navigateToRegister() {
    this.router.navigate(['/register']);
  }
}
