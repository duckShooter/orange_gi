import { Component, OnInit } from '@angular/core';
import { Product } from 'src/app/_models/product';
import { Category } from 'src/app/_models/category';
import { ProductService } from 'src/app/_services/product.service';
import { CategoryService } from 'src/app/_services/category.service';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-add-dialog',
  templateUrl: './product-add-dialog.component.html',
  styleUrls: ['./product-add-dialog.component.css']
})
export class ProductAddDialogComponent implements OnInit {
  product: Product;
  categories: Category[];

  constructor(private productService: ProductService,
    private categoryService: CategoryService,
    private dialogRef: MatDialogRef<ProductAddDialogComponent>) {
      this.product = new Product();
      this.product.category = new Category();
  }

  ngOnInit() {
    this.categoryService.findAll().subscribe(
      data => {
        this.categories = data;
      }
    )
  }

  onClickAddReload() {
    this.productService.save(this.product).subscribe(
      next => {
        this.dialogRef.close();
        location.reload();
      }
    );
  }

  onClickClose() {
    this.dialogRef.close();
  }
}
