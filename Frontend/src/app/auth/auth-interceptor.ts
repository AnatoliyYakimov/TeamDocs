import {Injectable, OnDestroy} from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import {Observable, Subscription} from 'rxjs';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor, OnDestroy{
    private token: string;
    private sub: Subscription;

    constructor(private authService: AuthService) {
        this.sub = this.authService.token$.subscribe(value => {
          this.token = value;
        })
    }
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (this.token) {
          const reqHeaders = req.clone({
            headers: req.headers.set('Authorization', this.token)
          });
          return next.handle(reqHeaders);
        }
        return next.handle(req);
    }

  ngOnDestroy(): void {
      this.sub.unsubscribe();
  }
}
