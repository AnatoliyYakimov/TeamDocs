import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Document} from "../../document";

@Component({
  selector: 'app-document-history-item',
  templateUrl: './document-history-item.component.html',
  styleUrls: ['./document-history-item.component.css']
})
export class DocumentHistoryItemComponent implements OnInit {

  @Input() document: Document;
  @Output() onChoice = new EventEmitter<Document>();

  constructor() { }

  ngOnInit() {
  }

}
