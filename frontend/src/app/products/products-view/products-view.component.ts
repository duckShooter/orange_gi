import { Component, OnInit } from '@angular/core';
import { ProductService } from 'src/app/_services/product.service';
import { Product } from 'src/app/_models/product';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ProductEditDialogComponent } from '../product-dialog/product-edit-dialog.component';
import { Router, ActivatedRoute } from '@angular/router';
import { ProductAddDialogComponent } from '../product-dialog/product-add-dialog.component';
import { observable, Observable } from 'rxjs';

@Component({
  selector: 'app-products-view',
  templateUrl: './products-view.component.html',
  styleUrls: ['./products-view.component.css']
})
export class ProductsViewComponent implements OnInit {
  products: Product[];
  categoryId: number;

  constructor(private productService: ProductService,
    private matDialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe( params => {
      this.categoryId = params['id'];
    });

    let observableProducts : Observable<Product[]>;
    if(this.categoryId)
      observableProducts = this.productService.findAllInCategory(this.categoryId);
    else
      observableProducts = this.productService.findAll();

    observableProducts.subscribe(
      next => {
          this.products = next;
      },
      error => {
        if(error.status == 401)
          this.router.navigate(['/login']);
      }
    )
  }

  onClickDelete(id: number) {
    console.log(id);
    this.productService.delete(id).subscribe(
      () => {
        window.location.reload(); //I'm shamefully using this
      }
    )
  }
  
  onClickEdit(product: Product) {
    const matdialogConfig = new MatDialogConfig();
    matdialogConfig.autoFocus = true;
    matdialogConfig.data = product;
    this.matDialog.open(ProductEditDialogComponent, matdialogConfig);
  }

  onClickAdd() {
    this.matDialog.open(ProductAddDialogComponent);
  }
}
