import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPanelTestComponent } from './admin-panel-test.component';

describe('AdminPanelTestComponent', () => {
  let component: AdminPanelTestComponent;
  let fixture: ComponentFixture<AdminPanelTestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminPanelTestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminPanelTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
