import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Document} from "../document";
import {ActiveDocumentService} from "../active-document.service";

@Component({
  selector: 'app-document-history',
  templateUrl: './document-history.component.html',
  styleUrls: ['./document-history.component.css']
})
export class DocumentHistoryComponent implements OnInit {

  @Input() history: Document[] = [];
  @Output() onChoice = new EventEmitter<Document>();

  constructor(private documentService: ActiveDocumentService) {
    this.documentService.document$.subscribe(
      value => {
        if(value.hash) {
          this.documentService.loadHistoryOfDocument(value).subscribe(
            value1 => this.history = value1
          );
        }
        else{
          this.history = [];
        }
      }
    );
  }

  ngOnInit() {
  }

}
