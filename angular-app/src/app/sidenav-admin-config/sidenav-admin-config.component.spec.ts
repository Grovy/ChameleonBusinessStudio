import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidenavAdminConfigComponent } from './sidenav-admin-config.component';

describe('SidenavAdminConfigComponent', () => {
  let component: SidenavAdminConfigComponent;
  let fixture: ComponentFixture<SidenavAdminConfigComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SidenavAdminConfigComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SidenavAdminConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
