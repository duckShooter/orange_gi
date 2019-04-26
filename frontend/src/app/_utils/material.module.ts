import { NgModule } from '@angular/core';
import { MatGridListModule } from '@angular/material/grid-list'; 
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatInputModule } from '@angular/material/input'
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSelectModule } from '@angular/material/select';
import { MatListModule } from '@angular/material/list'; 

@NgModule({
    exports: [
        MatButtonModule,
        MatInputModule,
        MatToolbarModule,
        MatCardModule,
        MatGridListModule,
        MatIconModule, 
        MatTooltipModule,
        MatDialogModule,
        MatSnackBarModule,
        MatSelectModule,
        MatListModule
    ]
})
export class MaterialModule {

}