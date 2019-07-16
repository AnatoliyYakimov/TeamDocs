import { Component, OnInit } from '@angular/core';
import { AuthService } from './auth.service';
import {ApiError} from "../entities/api-error";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  private username: string;
  private password: string;
  private remember: boolean;

  private loginError = false;

  constructor(private loginService: AuthService) { }

  Login() {
    if (this.remember){
      this.loginService.rememberMe();
    }
    this.loginService.login(this.username, this.password).subscribe(
      err => {
        this.handleError(err);
      }
    );
  }

  handleError(error: ApiError){
    console.log(error);
    this.loginError = true;
  }

  ngOnInit() {
  }

}
