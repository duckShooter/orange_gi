import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { ServerComponent } from './Server/server.component';
import { ServersComponent } from './servers/servers.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { UserService } from './_services/user.service';
import { HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import { ProductService } from './_services/product.service';
import { ProductsViewComponent } from './products/products-view/products-view.component'; 
import { MaterialModule } from './_utils/material.module';
import { ProductEditDialogComponent } from './products/product-dialog/product-edit-dialog.component';
import { AppRoutingModule } from './app-routing.module'; 
import { CategoryService } from './_services/category.service';
import { ProductAddDialogComponent } from './products/product-dialog/product-add-dialog.component';
import { CategoriesViewComponent } from './categories/categories-view/categories-view.component';
import { CategoryDialogComponent } from './categories/category-dialog/category-dialog.component';

@NgModule({
  declarations: [ 
    AppComponent,
    ServerComponent,
    ServersComponent,
    LoginComponent,
    ProductsViewComponent,
    ProductEditDialogComponent,
    ProductAddDialogComponent,
    CategoriesViewComponent,
    CategoryDialogComponent
  ],

  imports: [ 
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MaterialModule,
    AppRoutingModule
  ],

  providers: [UserService, ProductService, CategoryService],

  bootstrap: [AppComponent],

  entryComponents: [
    ProductEditDialogComponent, 
    ProductAddDialogComponent,
    CategoryDialogComponent
  ]
})
export class AppModule { }
