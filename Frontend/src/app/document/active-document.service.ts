import {EventEmitter, Injectable, OnDestroy} from '@angular/core';
import {Document} from "./document";
import {DocumentFetchService} from "./document-fetch.service";
import {BehaviorSubject, Observable, Subject, Subscription} from "rxjs";
import {WsMessage} from "../websocket/WsMessage";
import {WebSocketService} from "../websocket/web-socket.service";
import {WebSocketEvent} from "../websocket/websocket-events";

@Injectable({
  providedIn: 'root'
})
export class ActiveDocumentService implements OnDestroy{
  private readonly DEFAULT_DOCUMENT: Document = {
    name: 'New Document',
    text: 'Enter text here'
  };
  private documentSubj$ = new BehaviorSubject<Document>(this.DEFAULT_DOCUMENT);
  private wsMessagesSubj$ = new Subject<WsMessage<any>>();

  public document$ = this.documentSubj$.asObservable();
  public wsMessages$ = this.wsMessagesSubj$.asObservable();

  private sub: Subscription;
  private subscriptionHash: string;

  public newdoc$ = new EventEmitter<boolean>();

  constructor(private fetchService: DocumentFetchService, private messagesService: WebSocketService) {
    this.messagesService.messages$.subscribe(
      value => this.wsMessagesSubj$.next(value)
    );
    this.documentSubj$.subscribe(
      value => {
        if (value.hash)
          this.subToDocument(value.hash)
      }
    );
    this.wsMessages$.subscribe(
      value => {
        if (value.event === WebSocketEvent.NEW_DOC) {
          let doc = value.data as Document;
          if (!!doc.hash) {
            this.subscriptionHash = doc.hash;
          }
          this.documentSubj$.next(value.data as Document)
        }
      }
    )
  }

  loadDocumentByHash(hash: string): void {
    this.fetchService.getDocumentByHash(hash).subscribe(
      value => this.documentSubj$.next(value)
    );
  }

  loadDocumentById(id: number): void {
    this.fetchService.getDocumentById(id)
      .subscribe(
        value => this.documentSubj$.next(value)
      );
  }

  loadHistoryOfDocument(doc: Document): Observable<Document[]>{
    return this.fetchService.getHistoryOfDocument(doc);
  }

  saveDocument(doc: Document) {
    this.fetchService.saveDocument(doc)
      .subscribe(
        (doc: Document) => {
          this.documentSubj$.next(doc)
        });
  }

  private broadcast(msg: WsMessage<any>): void {
    this.wsMessagesSubj$.next(msg);
  }

  private subToDocument(hash: string) {

    if (hash && this.subscriptionHash != hash) {
      if (!!this.sub) {
        this.sub.unsubscribe();
      }
      this.sub = this.messagesService.subscribe(`/topic/document.${hash}`);
    }

  }

  newDocument() {
    this.documentSubj$.next(this.DEFAULT_DOCUMENT);
    if (!!this.sub)
      this.sub.unsubscribe();
    this.subscriptionHash = '';
    this.newdoc$.emit(true);
  }

  ngOnDestroy(): void {
    !!this.sub ? this.sub.unsubscribe() : 0;
  }
}
