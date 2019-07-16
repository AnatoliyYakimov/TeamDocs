import {Injectable} from "@angular/core";

@Injectable(
  { providedIn: 'root'}
)
export class InjectableAuthServiceConfig {
  url: string;
  authEndpointPrefix: string;
  localStoragePrefix: string;
  header: string;
}
