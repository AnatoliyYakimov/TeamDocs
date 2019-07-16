import {InjectableRxStompConfig} from "@stomp/ng2-stompjs";
import {environment} from "../../environments/environment";
import * as SockJS from 'sockjs-client';

export const StompConfig: InjectableRxStompConfig = {

  webSocketFactory: () => new SockJS(environment.ws),


  heartbeatIncoming: 0, // Typical value 0 - disabled
  heartbeatOutgoing: 20000, // Typical value 20000 - every 20 seconds

  reconnectDelay: 2000,
  debug: (msg: string): void => {
    console.log(new Date(), msg);
  }
}
