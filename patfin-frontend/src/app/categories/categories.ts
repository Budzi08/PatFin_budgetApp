import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


export interface Category {
  id: number;
  name: string;
}

@Injectable({ providedIn: 'root' })
export class CategoriesService {
  private apiUrl = 'http://localhost:8080/api/categories';

  constructor(private http: HttpClient) { }

  getAll(): Observable<Category[]> {
    return this.http.get<Category[]>(this.apiUrl);
  }
  list() { return this.http.get<Category[]>(this.apiUrl); }
  add(category: Partial<Category>) { return this.http.post<Category>(this.apiUrl, category); }
  delete(id: number) { return this.http.delete(this.apiUrl + '/' + id); }
}
