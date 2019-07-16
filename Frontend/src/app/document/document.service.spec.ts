import { TestBed } from '@angular/core/testing';

import { DocumentFetchService } from './document-fetch.service';

describe('DocumentFetchService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DocumentFetchService = TestBed.get(DocumentFetchService);
    expect(service).toBeTruthy();
  });
});
