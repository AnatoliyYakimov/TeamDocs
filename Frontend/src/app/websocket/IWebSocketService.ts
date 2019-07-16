import { Observable} from 'rxjs';
import { WsMessage } from './WsMessage';


export interface IWebSocketService<T> {
  subscribe(destination: string): void;
}
