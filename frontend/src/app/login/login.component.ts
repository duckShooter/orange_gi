import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { User } from '../_models/user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  errorMessage: string;
  disabled: boolean;
  user: User;

  constructor(private userService: UserService,
    private router: Router) {
    this.user = new User(); 

  }

  onSubmit(valid: boolean) {
    if(valid) {
      this.disabled = true;
      this.userService.login(this.user).subscribe(
        next => {
          sessionStorage.setItem('basic', btoa(`${this.user.username}:${this.user.password}`));
          this.router.navigate(['/products']);
        },
        error => {
          this.disabled = false;
          console.log(error);
            this.errorMessage = error.status == 401 ? "Wrong Credentials"
              : "Unknown Error!"
        }
      );
    }
  }

  ngOnInit() {
  }

}
