import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminConfigContainerComponent } from './admin-config-container.component';

describe('AdminConfigContainerComponent', () => {
  let component: AdminConfigContainerComponent;
  let fixture: ComponentFixture<AdminConfigContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminConfigContainerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminConfigContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
