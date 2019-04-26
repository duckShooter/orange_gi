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



//Reference: https://angular.io/api/core/NgModule
/* An NgModule is a class marked by the @NgModule decorator. @NgModule takes a metadata object that describes 
   how to compile a component's template and how to create an injector at runtime. It identifies the module's 
   own components, directives, and pipes, making some of them public, through the exports property, so that 
   external components can use them. @NgModule can also add service providers to the application dependency 
   injectors.
*/
@NgModule({
  //The set of components, directives, and pipes (declarables) that belong to this module.
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
  //The set of NgModules whose exported declarables are available to templates in this module.
  imports: [ 
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MaterialModule,
    AppRoutingModule
  ],
  //The set of injectable objects that are available in the injector of this module. 
  providers: [UserService, ProductService, CategoryService],
  /*The set of components that are bootstrapped when this module is bootstrapped. 
  The components listed here are automatically added to entryComponents.*/
  bootstrap: [AppComponent],
  entryComponents: [
    ProductEditDialogComponent, 
    ProductAddDialogComponent,
    CategoryDialogComponent
  ]
})
export class AppModule { }
