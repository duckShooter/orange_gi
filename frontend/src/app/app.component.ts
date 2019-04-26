import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from './_services/user.service';

//Reference: https://angular.io/api/core/Component
@Component({
  selector: 'app-root', //The CSS selector that identifies this directive in a template
  templateUrl: './app.component.html', //The relative path or absolute URL of a template file
  styleUrls: ['./app.component.css'] //One or more relative paths or absolute URLs for files containing CSS
})

export class AppComponent {
  constructor(private router: Router,
    private userService: UserService) {

  }

  onClickLogout() {
    this.userService.logout();
    this.router.navigate(['/login']);
  }
}
