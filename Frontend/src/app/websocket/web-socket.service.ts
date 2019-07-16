import {Injectable, OnDestroy, OnInit} from '@angular/core';

import {IWebSocketService} from './IWebSocketService';
import {InjectableRxStompConfig, RxStompService} from "@stomp/ng2-stompjs";
import {map, tap} from "rxjs/operators";
import {WsMessage} from "./WsMessage";
import {Frame, IMessage} from "@stomp/stompjs";
import {Observable, Subject, Subscription, SubscriptionLike} from "rxjs";
import {AuthService} from "../auth/auth.service";


@Injectable({
  providedIn: 'root'
})
export class WebSocketService implements IWebSocketService<any>, OnDestroy{


  private messagesFlow$ = new Subject<WsMessage<any>>();

  public readonly messages$ = this.messagesFlow$.asObservable();

  private token: string;
  private stompClient = new RxStompService();
  private sub: SubscriptionLike;


  constructor(private config: InjectableRxStompConfig ,private authService: AuthService) {
    this.stompClient.configure(config);
    console.log("Init WebSocket");
    this.stompClient.deactivate();
    this.authService.token$.subscribe(
      value => {
        this.token = value;
        console.log('Configuring websocket with token');
        console.log(this.token);
        this.stompClient.configure({
          connectHeaders: {
            'Authorization': value
          }
        });
        if(this.stompClient.connected()){
          this.stompClient.deactivate();
        }
        this.stompClient.activate();
        let username = this.authService.getUsername();
        this. sub = this.subscribe(`/queue/reply-${username}`);
      });
    this.messagesFlow$.subscribe(value => console.log(value));
  }



  subscribe(destination: string): Subscription {
    return this.stompClient.watch(destination).pipe(
      map<IMessage, WsMessage<any>>(frame => JSON.parse(frame.body) as WsMessage<any>)
    ).subscribe( value => this.messagesFlow$.next(value));

  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
    this.stompClient.deactivate();
  }

}

