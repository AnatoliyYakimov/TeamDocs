import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {AppComponent} from './app.component';
import {DocumentComponent} from './document/document.component';
import {MessagesComponent} from './messages/messages.component';
import {LoginComponent} from './auth/login.component';
import {NavBarComponent} from './nav-bar/nav-bar.component';
import {AuthGuardService} from './auth/auth-guard.service';
import {WebSocketModule} from "./websocket/web-socket.module";
import {AuthModule} from "./auth/auth.module";
import {DocumentModule} from "./document/document.module";
import {CKEditorComponent} from "@ckeditor/ckeditor5-angular";

const appRoutes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'documents', component: DocumentComponent, canActivate: [AuthGuardService]},
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  }
  //{ path: '**', component: PageNotFoundComponent }
];


@NgModule({
  declarations: [
    AppComponent,
    MessagesComponent,
    NavBarComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(
      appRoutes
    ),
    WebSocketModule,
    AuthModule,
    DocumentModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
