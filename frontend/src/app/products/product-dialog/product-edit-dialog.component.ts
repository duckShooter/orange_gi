import { Component, OnInit, Inject } from '@angular/core';
import { Product } from 'src/app/_models/product';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ProductService } from 'src/app/_services/product.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-product-dialog',
  templateUrl: './product-edit-dialog.component.html',
  styleUrls: ['./product-edit-dialog.component.css']
})
export class ProductEditDialogComponent implements OnInit {
  product: Product;

  constructor(@Inject(MAT_DIALOG_DATA) private data,
    private dialogRef: MatDialogRef<ProductEditDialogComponent>,
    private productService: ProductService,
    private snackBar: MatSnackBar) {
    this.product = this.data;
    this.product = Object.assign({}, this.data); //No modification to original data
  }

  ngOnInit() {
    
  }

  onClickUpdateClose() {
    this.productService.update(this.product).subscribe(
      next => {
        this.dialogRef.close();
        this.data = Object.assign(this.data, this.product);
        this.snackBar.open('Successfully Updated', null, {
          duration: 2000
        });
      },
      error => {
        /* 
          The check for 401 error is done in onInit() method of ProductsViewComponent.
          Another scenario here is to check on 401 error in case the user token has 
          expired but I'll ignore this as it doesn't happen in my case 
        */
        if(error.status == 404)
          this.snackBar.open('Failed! This item is no longer available', null, {
            duration: 5000
          });
        else 
          this.snackBar.open('Failed! Something went wrong', null, {
            duration: 2000
          });
      }
    ) 
  }

  onClickClose() {
    this.dialogRef.close();
  }
}
