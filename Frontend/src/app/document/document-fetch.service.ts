import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Observable, of, Subject, throwError} from 'rxjs';
import { Document } from './document';
import { LinkHal } from './link-hal';
import { catchError, map, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import {PageMetadata} from "../entities/page-metadata";
import {ApiError} from "../entities/api-error";


@Injectable({
  providedIn: 'root'
})
export class DocumentFetchService {

    private errorsFlow$ = new Subject<ApiError>();
    private headers: HttpHeaders;
    private contextPath = environment.api;
    private urls: Record<string, string> = {
      save: 'documents/',
      getById: 'documents/',
    };

    public errors$ = this.errorsFlow$.asObservable();

    constructor(private http: HttpClient) {
      this.headers = new HttpHeaders().set('Content-Type', 'application/json');
    }

    getDocumentById(id: number): Observable<Document> {
      return this.http.get<Document>(this.contextPath + this.urls.getById + id.toString(),
      {
        headers: this.headers
      }).pipe(
        catchError(this.handleError('Load document by id'))
      );
    }

    saveDocument(doc: Document): Observable<Document> {
      const dataToSave: Document = {
        name: doc.name,
        text: doc.text,
        hash: doc.hash
      };
      return this.http.post<Document>(this.contextPath + this.urls.save, dataToSave, {
        headers: this.headers
      }).pipe(
        catchError(this.handleError('Save document'))
      );
    }

    getHistoryOfDocument(doc: Document, page?: PageMetadata): Observable<Document[]> {
      return this.http.get<Document[]>(`${this.contextPath}documents/history/${doc.hash}?sort=asc`, {headers: this.headers}).pipe(
        catchError(this.handleError('Get history of document'))
      );
    }

    getLastVersionOfDocument(doc: Document): Observable<Document> {
      const link = this.extractLink(doc, 'last_version').href;
      return this.http.get<Document>(link, { headers: this.headers}).pipe(
        catchError(this.handleError('Get last version of document'))
      );
    }

    getDocumentByHash(hash: string): Observable<Document> {
      return this.http.get<Document>(`${this.contextPath}documents/last/${hash}`, { headers: this.headers}).pipe(
        catchError(this.handleError('Get document by hash'))
      );
    }

    handleError(operation = 'Operation') {
      return (error: any, caught: Observable<any>) => {
        console.log(`${operation} failed with error: ${error.message}`);
        console.log(error);
        this.errorsFlow$.next(error as ApiError);
        return [];
      };
    }

    extractLink(doc: Document, rel: string): LinkHal {
      return doc.links.find((link: LinkHal) => link.rel === rel);
    }

}
