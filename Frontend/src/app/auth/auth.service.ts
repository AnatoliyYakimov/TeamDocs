import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {throwError, Observable, Subject, BehaviorSubject, of} from 'rxjs';
import {Token} from './token';
import {ApiError} from '../entities/api-error';
import {WebSocketService} from 'src/app/websocket/web-socket.service';
import {InjectableAuthServiceConfig} from "./injectable-auth-service-config";
import {filter, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loggedSubj$ = new BehaviorSubject(false);
  private tokenSubj$ = new BehaviorSubject('');
  private errorsSubj$ = new Subject<ApiError>();
  private logged: boolean;
  private username: string = '';

  public readonly logged$ = this.loggedSubj$.asObservable();
  public readonly token$ = this.tokenSubj$.asObservable();
  public readonly errors$ = this.errorsSubj$.asObservable();


  options = {
    observe: 'response',
    headers: {
      'Content-Type': 'application/json'
    }
  };

  constructor(private http: HttpClient, private router: Router,
              private config: InjectableAuthServiceConfig) {
    this.logged$.subscribe(
      value => this.logged = value
    );
    if (this.isRemembered()){
      this.performLogin(this.loadTokenFromStorage(), this.loadUsernameFromStorage());
    }
  }

  public isLogged(){
    return this.logged;
  }

  public getUsername(): string{
    return this.username;
  }

  public login(username: string, password: string): Observable<ApiError>{
    const user = {username, password};
    let resp$ = this.http.post<Token>(this.config.url + this.config.authEndpointPrefix, user,
      {
        headers: {
          'Content-Type': 'application/json'
        },
        observe: 'response'
      });
      resp$.subscribe(
      (resp: HttpResponse<Token>) => {
        console.log(resp);
        this.performLogin(resp.body.token, username);
      },
      (resp: HttpResponse<ApiError>) => {
        console.log('Login error');
        console.error(resp.body);
        this.errorsSubj$.next(resp.body);
      });
      return this.errors$;
  }


  private performLogin(token: string, username: string): void {
    this.saveToken(token);
    this.username = username;
    if (this.isRemembered()){
      this.saveUserNameToStorage(username);
    }
    this.loggedSubj$.next(true);
    this.router.navigate(['documents']);
  }

  public logout(): void {
    this.loggedSubj$.next(false);
    this.tokenSubj$.next('');
    localStorage.removeItem(this.config.localStoragePrefix + this.config.header);
    localStorage.removeItem(this.config.localStoragePrefix + 'remember');
  }


  private saveToken(token: string): void {
    localStorage.setItem(this.config.localStoragePrefix + this.config.header, token);
    console.log('Published token');
    console.log(token);
    this.tokenSubj$.next(token);
  }

  private loadTokenFromStorage(): string{
    return localStorage.getItem(this.config.localStoragePrefix + this.config.header);
  }

  private loadUsernameFromStorage(): string{
    return localStorage.getItem(this.config.localStoragePrefix + 'username');
  }

  private saveUserNameToStorage(username: string){
    localStorage.setItem(this.config.localStoragePrefix + 'username', username);
  }

  public rememberMe(){
    localStorage.setItem(this.config.localStoragePrefix + 'remember', 'true');
  }

  public isRemembered(){
    return localStorage.getItem(this.config.localStoragePrefix + 'remember');
  }
}
