import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParticipantBookedApptsComponent } from './participant-booked-appts.component';

describe('ParticipantBookedApptsComponent', () => {
  let component: ParticipantBookedApptsComponent;
  let fixture: ComponentFixture<ParticipantBookedApptsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ParticipantBookedApptsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ParticipantBookedApptsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
