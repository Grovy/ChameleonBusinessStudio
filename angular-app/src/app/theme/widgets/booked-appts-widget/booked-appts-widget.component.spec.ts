import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookedApptsWidgetComponent } from './booked-appts-widget.component';

describe('BookedApptsWidgetComponent', () => {
  let component: BookedApptsWidgetComponent;
  let fixture: ComponentFixture<BookedApptsWidgetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BookedApptsWidgetComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BookedApptsWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
