import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalenderViewComponent } from './calender-view.component';

describe('CalenderViewComponent', () => {
  let component: CalenderViewComponent;
  let fixture: ComponentFixture<CalenderViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CalenderViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CalenderViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
