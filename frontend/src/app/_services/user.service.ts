import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { User } from '../_models/user';
import { Api } from '../_models/urls';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) {
  }

  public login(user: User) {
    return this.http.post(Api.login, user);
  }

  public logout() {
    sessionStorage.removeItem('basic');
  }
}
