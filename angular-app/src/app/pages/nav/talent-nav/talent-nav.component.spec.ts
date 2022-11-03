import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TalentNavComponent } from './talent-nav.component';

describe('TalentNavComponent', () => {
  let component: TalentNavComponent;
  let fixture: ComponentFixture<TalentNavComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TalentNavComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TalentNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
