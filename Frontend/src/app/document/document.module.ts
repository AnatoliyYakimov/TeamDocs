import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DocumentComponent} from "./document.component";
import {ActiveUsersComponent} from "./activeUsers/active-users.component";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {CKEditorModule} from "ckeditor4-angular";
import { DocumentHistoryComponent } from './document-history/document-history.component';
import { DocumentHistoryItemComponent } from './document-history/document-history-item/document-history-item.component';



@NgModule({
  declarations: [
    DocumentComponent,
    ActiveUsersComponent,
    DocumentHistoryComponent,
    DocumentHistoryItemComponent
  ],
  imports: [
    HttpClientModule,
    CommonModule,
    FormsModule,
    NgbModule,
    CKEditorModule
  ]
})
export class DocumentModule {
}
