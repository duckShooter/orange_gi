import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Api } from '../_models/urls';
import { Observable } from 'rxjs';
import { Category } from '../_models/category';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private headers: HttpHeaders;
  constructor(private http: HttpClient) { 
    this.headers = new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
    });
  }

  public findAll(): Observable<Category[]> {
    return this.http.get<Category[]>(`${Api.categories}`,
      { headers: this.headers.set('Authorization', `Basic ${sessionStorage.getItem('basic')}`)
    });
  }

  public save(category: Category) {
    return this.http.put<Category>(Api.categories, category,
      { headers: this.headers.set('Authorization', `Basic ${sessionStorage.getItem('basic')}`)
    });
  }

  public rename(id: number, name: string) {
    return this.http.post(`${Api.categories}/${id}`, null,
      { headers: this.headers.set('Authorization', `Basic ${sessionStorage.getItem('basic')}`),
      params: {'name': name}});
  }

  public delete(id: number) {
    return this.http.delete(`${Api.categories}/${id}`,
      { headers: this.headers.set('Authorization', `Basic ${sessionStorage.getItem('basic')}`)
    });
  }
}
