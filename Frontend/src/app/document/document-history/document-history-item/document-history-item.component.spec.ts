import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentHistoryItemComponent } from './document-history-item.component';

describe('DocumentHistoryItemComponent', () => {
  let component: DocumentHistoryItemComponent;
  let fixture: ComponentFixture<DocumentHistoryItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DocumentHistoryItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentHistoryItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
