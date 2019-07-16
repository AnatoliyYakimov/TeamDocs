import{ LinkHal } from './link-hal';

export class Document {
  name: string;
  text: string;
  hash?: string;
  author?: string;
  modifiedBy?: string;
  createdAt?: Date;
  updatedAt?: Date;
  links?: LinkHal[];
}
