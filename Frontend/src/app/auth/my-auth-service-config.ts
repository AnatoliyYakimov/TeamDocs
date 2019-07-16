import {InjectableAuthServiceConfig} from "./injectable-auth-service-config";
import {environment} from "../../environments/environment";

export const MyAuthServiceConfig: InjectableAuthServiceConfig = {
  url: environment.api,
  authEndpointPrefix: 'login',
  localStoragePrefix: 'TeamDocs',
  header: 'Authorization'
};
