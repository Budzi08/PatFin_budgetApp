import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionsAdd } from './transactions-add';

describe('TransactionsAdd', () => {
  let component: TransactionsAdd;
  let fixture: ComponentFixture<TransactionsAdd>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionsAdd]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransactionsAdd);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
