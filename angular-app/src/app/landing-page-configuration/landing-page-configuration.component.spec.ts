import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LandingPageConfigurationComponent } from './landing-page-configuration.component';

describe('LandingPageConfigurationComponent', () => {
  let component: LandingPageConfigurationComponent;
  let fixture: ComponentFixture<LandingPageConfigurationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LandingPageConfigurationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LandingPageConfigurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
