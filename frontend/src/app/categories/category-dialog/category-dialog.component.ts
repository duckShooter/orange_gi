import { Component, OnInit } from '@angular/core';
import { Category } from 'src/app/_models/category';
import { MatDialogRef } from '@angular/material/dialog';
import { CategoryService } from 'src/app/_services/category.service';

@Component({
  selector: 'app-category-dialog',
  templateUrl: './category-dialog.component.html',
  styleUrls: ['./category-dialog.component.css']
})
export class CategoryDialogComponent implements OnInit {
  private category: Category;

  constructor(private categoryService: CategoryService,
    private dialogRef: MatDialogRef<CategoryDialogComponent>) {
    this.category = new Category();
  }

  ngOnInit() {
  }

  onClickAddReload() {
    this.categoryService.save(this.category).subscribe(
      next => {
        this.dialogRef.close();
        location.reload() //Omg <3 
    });
  }

  onClickClose() {
    this.dialogRef.close();
  }

}
