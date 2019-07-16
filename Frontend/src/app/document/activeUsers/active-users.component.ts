import {Component, EventEmitter, Input, OnInit} from '@angular/core';
import {WsMessage} from "../../websocket/WsMessage";
import {WebSocketEvent} from "../../websocket/websocket-events";
import {ActiveDocumentService} from "../active-document.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-active-users',
  templateUrl: './active-users.component.html',
  styleUrls: ['./active-users.component.css']
})
export class ActiveUsersComponent implements OnInit {

  private users: string[] = [];

  constructor(private documentService: ActiveDocumentService) {
    this.documentService.wsMessages$.subscribe(
      (value: WsMessage<any>) => {
        this.handleMessage(value)
      }
    );
    this.documentService.newdoc$.subscribe(
      value => this.clear()
    )
  }

  private handleMessage(msg: WsMessage<any>) {
    let data = msg.data;
    let event = msg.event;
    console.log('Handling event', msg);
    switch (event) {
      case WebSocketEvent.ALL_SUBS:
        this.users = data as string[];
        break;
      case WebSocketEvent.SUB:
        if (!this.users.includes(data as string)){
          this.users = [...this.users, (data as string)];
        }
        break;
      case WebSocketEvent.UNSUB:
        this.users = this.users.filter(value => value != data as string);
        break;
    }
  }

  public clear(){
    this.users = [];
  }

  ngOnInit() {

  }

}

