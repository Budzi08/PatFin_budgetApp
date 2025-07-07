import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  login(email: string, password: string) {
    return this.http.post<{ token: string }>(`${this.baseUrl}/login`, { email, password })
      .pipe(
        tap(response => {
          localStorage.setItem('jwt', response.token);
        })
      );
  }

  register(email: string, password: string) {
    return this.http.post(`${this.baseUrl}/register`, { email, password });
  }

  logout() {
    localStorage.removeItem('jwt');
  }

  getToken() {
    return localStorage.getItem('jwt');
  }

  isAdmin(): boolean {
    const token = localStorage.getItem('jwt');
    if (!token) {
      console.log('AuthService: No token found');
      return false;
    }
    try {
      const decoded: any = jwtDecode(token);
      console.log('AuthService: Decoded token:', decoded);
      console.log('AuthService: isAdmin value:', decoded.isAdmin);
      return decoded.isAdmin === true;
    } catch (error) {
      console.log('AuthService: Error decoding token:', error);
      return false;
    }
  }
}
