import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AuthInterceptor} from "./auth-interceptor";
import {InjectableAuthServiceConfig} from "./injectable-auth-service-config";
import {MyAuthServiceConfig} from "./my-auth-service-config";
import {LoginComponent} from "./login.component";
import {FormsModule} from "@angular/forms";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";



@NgModule({
  declarations: [
    LoginComponent
  ],
  imports: [
    HttpClientModule,
    CommonModule,
    FormsModule,
    NgbModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: InjectableAuthServiceConfig,
      useValue: MyAuthServiceConfig
    }
  ]
})
export class AuthModule { }
