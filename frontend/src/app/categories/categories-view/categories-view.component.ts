import { Component, OnInit } from '@angular/core';
import { CategoryService } from 'src/app/_services/category.service';
import { Category } from 'src/app/_models/category';
import { MatDialog } from '@angular/material/dialog';
import { CategoryDialogComponent } from '../category-dialog/category-dialog.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-categories-view',
  templateUrl: './categories-view.component.html',
  styleUrls: ['./categories-view.component.css']
})
export class CategoriesViewComponent implements OnInit {
    private editMode: boolean = false;
    private categories: Category[];

  constructor(private categoryService: CategoryService,
    private matDialog: MatDialog,
    private router: Router) {
  }

  ngOnInit() {
    this.categoryService.findAll().subscribe(
      data => {
        this.categories = data;
      },
      error => {
        if(error.status == 401)
        this.router.navigate(['/login']);
      }
    )
  }

  onClickDelete(id: number) {
    this.categoryService.delete(id).subscribe(
      () => {
        location.reload(); //I can't stop using it! it's so addictive =')
      }
    );
  }

  onClickRename(categroy: Category, newName: string) {
    this.categoryService.rename(categroy.id, newName).subscribe(
      next => {
        categroy.name = newName;
      }
    )
  }

  onClickAdd() {
    this.matDialog.open(CategoryDialogComponent);
  }
}
