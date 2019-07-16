import {Component, EventEmitter, OnInit} from '@angular/core';
import {Document} from './document';
import {MessagesService} from 'src/app/messages/messages.service';
import {ActiveDocumentService} from "./active-document.service";
import * as CKEditor from "@ckeditor/ckeditor5-angular";


@Component({
  selector: 'app-document',
  templateUrl: './document.component.html',
  styleUrls: ['./document.component.css']
})
export class DocumentComponent implements OnInit {
  private editorConfig = {
    autoGrow_onStartup: true
  };

  private loadedDocument: Document;
  private changedDocument: Document;
  private loadId: number;
  private hash: string;

  private clearEvent = new EventEmitter<boolean>();

  constructor(private documentService: ActiveDocumentService, private messagesService: MessagesService) {
    this.documentService.document$.subscribe(
      value => {
        this.updateLoadedDocument(value);
      }
    )
  }

  private onChoise(doc: Document){
    this.updateLoadedDocument(doc);
  }

  private updateLoadedDocument(value: Document){
    this.loadedDocument = value;
    this.changedDocument = new Document();
    this.changedDocument.text = value.text;
    this.changedDocument.name = value.name;
    this.changedDocument.hash = value.hash;
  }

  newDocument() {
    this.documentService.newDocument();
    this.clearEvent.emit(true);
  }

  saveDocument() {
    this.documentService.saveDocument(this.changedDocument);
  }

  loadDocumentById() {
    this.documentService.loadDocumentById(this.loadId);
  }

  loadDocumentByHash() {
    this.documentService.loadDocumentByHash(this.hash);
  }

  ngOnInit() {

  }

}
