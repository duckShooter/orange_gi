import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Api } from '../_models/urls';
import { Observable } from 'rxjs';
import { Product } from '../_models/product';
import { CategoryService } from './category.service';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private headers: HttpHeaders
  constructor(private http: HttpClient,
    private categoryService: CategoryService,) {
    this.headers = new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      //'Authorization': `Basic ${sessionStorage.getItem('basic')}` //gets cached, needs to be freshly loaded
    });
  }

  public findAll() : Observable<Product[]> {
    return this.http.get<Product[]>(Api.products, {
      headers: this.headers.set('Authorization', `Basic ${sessionStorage.getItem('basic')}`)
    });
  }

  public findAllInCategory(id: number) : Observable<Product[]> {
    return this.http.get<Product[]>(`${Api.categories}/${id}/products`, {
      headers: this.headers.set('Authorization', `Basic ${sessionStorage.getItem('basic')}`)
    });
  }

  public save(product: Product) {
    return this.http.put<Product>(Api.products, product, {
      headers: this.headers.set('Authorization', `Basic ${sessionStorage.getItem('basic')}`)
    });
  }

  public update(product: Product) {
    return this.http.post<Product>(`${Api.products}/${product.id}`, product, {
      headers: this.headers.set('Authorization', `Basic ${sessionStorage.getItem('basic')}`)
    });
  }

  public delete(id: number) {
    return this.http.delete(`${Api.products}/${id}`, {
      headers: this.headers.set('Authorization', `Basic ${sessionStorage.getItem('basic')}`)
    });
  }
}