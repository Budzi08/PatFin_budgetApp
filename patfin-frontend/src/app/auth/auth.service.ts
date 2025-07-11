import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';

interface RegisterResponse {
  message: string;
}

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
    return this.http.post<RegisterResponse>(`${this.baseUrl}/register`, { email, password });
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

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;
    
    try {
      const decoded: any = jwtDecode(token);
      const currentTime = Date.now() / 1000;
      return decoded.exp > currentTime;
    } catch (error) {
      return false;
    }
  }

  getCurrentUserEmail(): string | null {
    const token = this.getToken();
    if (!token) return null;
    
    try {
      const decoded: any = jwtDecode(token);
      return decoded.sub || decoded.email || null;
    } catch (error) {
      return null;
    }
  }
}
