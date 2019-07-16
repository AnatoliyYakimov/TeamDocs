import { TestBed } from '@angular/core/testing';

import { ActiveDocumentService } from './active-document.service';

describe('ActiveDocumentService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ActiveDocumentService = TestBed.get(ActiveDocumentService);
    expect(service).toBeTruthy();
  });
});
