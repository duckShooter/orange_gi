import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ProductsViewComponent } from './products/products-view/products-view.component';
import { CategoriesViewComponent } from './categories/categories-view/categories-view.component';



const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', component: ProductsViewComponent, pathMatch: 'full'},
  { path: 'products', component: ProductsViewComponent},
  { path: 'categories/:id', component: ProductsViewComponent},
  { path: 'categories', component: CategoriesViewComponent}
]

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})

export class AppRoutingModule { }
