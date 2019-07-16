import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {
  private logged: boolean;

  canActivate(): boolean {
    if (this.logged) {
      return true;
    } else {
      this.router.navigate(['login']);
      return false;
    }
  }

  constructor(public login: AuthService, public router: Router) {
    login.logged$.subscribe(
      value => this.logged = value
    )
  }
}
