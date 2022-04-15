import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SplashPagePreviewComponent } from './splash-page-preview.component';

describe('SplashPagePreviewComponent', () => {
  let component: SplashPagePreviewComponent;
  let fixture: ComponentFixture<SplashPagePreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SplashPagePreviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SplashPagePreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
