import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPanelWidgetComponent } from './admin-panel-widget.component';

describe('AdminPanelWidgetComponent', () => {
  let component: AdminPanelWidgetComponent;
  let fixture: ComponentFixture<AdminPanelWidgetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminPanelWidgetComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminPanelWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
