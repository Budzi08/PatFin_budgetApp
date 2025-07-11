import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  imports: [FormsModule, CommonModule],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {
  email = '';
  password = '';
  confirmPassword = '';
  error = '';
  success = '';

  constructor(private authService: AuthService, private router: Router) { }

  onRegister() {
    console.log('Rejestracja - email:', this.email, 'password length:', this.password.length);
    
    if (!this.isFormValid()) {
      this.error = 'Sprawdź poprawność wprowadzonych danych';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.error = 'Hasła nie są identyczne';
      return;
    }

    console.log('Wysyłanie żądania rejestracji...');
    this.authService.register(this.email, this.password).subscribe({
      next: (response: {message: string}) => {
        console.log('Rejestracja zakończona sukcesem:', response);
        this.success = response.message || 'Konto zostało utworzone! Przekierowuję do logowania...';
        this.error = '';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (error) => {
        console.error('Błąd rejestracji:', error);
        console.error('Status:', error.status);
        console.error('Error body:', error.error);
        
        if (error.status === 0) {
          this.error = 'Brak połączenia z serwerem. Sprawdź czy backend jest uruchomiony.';
        } else if (error.error && error.error.error) {
          // Tłumaczenie błędów z backend na polski
          const backendError = error.error.error;
          this.error = this.translateError(backendError);
        } else if (error.error && typeof error.error === 'string') {
          // Tłumaczenie dla błędów w formacie string
          this.error = this.translateError(error.error);
        } else if (error.message) {
          this.error = error.message;
        } else {
          this.error = 'Błąd podczas rejestracji. Spróbuj ponownie.';
        }
      }
    });
  }

  isFormValid(): boolean {
    return this.email.length > 0 && 
           this.password.length >= 6 && 
           this.confirmPassword.length > 0;
  }

  // Funkcja do tłumaczenia błędów z backend na polski
  private translateError(errorMessage: string): string {
    const translations: {[key: string]: string} = {
      'Email already exists': 'Użytkownik z tym adresem email już istnieje. Spróbuj się zalogować lub użyj innego adresu.',
      'Invalid email format': 'Nieprawidłowy format adresu email.',
      'Password too short': 'Hasło jest za krótkie. Minimum 6 znaków.',
      'Invalid credentials': 'Nieprawidłowe dane logowania.'
    };

    return translations[errorMessage] || errorMessage;
  }

  navigateToHome() {
    this.router.navigate(['/']);
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
