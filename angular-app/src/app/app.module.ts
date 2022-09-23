/*
    If you have to import something here, maintain alphabetical order
*/
import { NgModule } from '@angular/core';

import { AdminConfigContainerComponent } from "./pages/admin-configuration/admin-config-container/admin-config-container.component";
import { AdminConfigurationComponent } from './pages/admin-configuration/admin-configuration/admin-configuration.component';
import { AdminConfigurationFormComponent } from './pages/admin-configuration/admin-configuration-form/admin-configuration-form.component';
import { AdminGenUserComponent } from './pages/admin-panel/admin-gen-user/admin-gen-user.component';
import { AdminPanelComponent } from './pages/admin-panel/admin-panel/admin-panel.component';
import { AdminPanelTestComponent } from './pages/admin-panel/admin-panel-test/admin-panel-test.component';
import { AppComponent } from './app.component';
import { AppointmentComponent} from './pages/appointments/appointment/appointment.component';
import { AppointmentCreateFormComponent } from './pages/appointments/appointment-create-form/appointment-create-form.component';
import { AppointmentDetailsComponent } from './pages/appointments/appointment-details/appointment-details.component';
import { AppointmentListComponent } from './pages/appointments/appointment-list/appointment-list.component';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { BookingPageComponent} from './pages/appointments/booking-page/booking-page.component';
import { ConfigPropertySetterComponent} from './pages/admin-configuration/config-property-setter/config-property-setter.component';
import { DefaultHeaderComponent } from './theme/default-header/default-header.component';
import { FlexLayoutModule, MediaObserver } from '@angular/flex-layout';
import { FormsModule} from "@angular/forms";
import { HttpClientModule } from '@angular/common/http';
import { LandingPageConfigurationComponent } from './pages/admin-configuration/landing-page-configuration/landing-page-configuration.component';
import { NavComponent } from './pages/nav/nav.component';
import { ReactiveFormsModule } from '@angular/forms'
import { RouterModule } from '@angular/router';
import { SetupWizardComponent } from './pages/auth/setup-wizard/setup-wizard.component';
import { SidenavAdminConfigComponent } from './pages/admin-configuration/sidenav-admin-config/sidenav-admin-config.component';
import { SignInComponent } from './pages/auth/signin/signin.component';
import { SignInFormComponent } from './pages/auth/sign-in-form/sign-in-form.component';
import { SiteFooterComponent } from './theme/site-footer/site-footer.component';
import { SiteHeaderComponent } from './theme/site-header/site-header.component';
import { SplashComponent } from './pages/dashboard/splash/splash.component';

// All Angular Material imports
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIcon } from "@angular/material/icon";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from "@angular/material/list";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelect, MatSelectModule } from "@angular/material/select";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatSliderModule } from '@angular/material/slider';
import { MatTableModule } from "@angular/material/table";
import { MatTabsModule } from "@angular/material/tabs";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatTooltipModule } from '@angular/material/tooltip';


@NgModule({
  declarations: [
    AdminConfigContainerComponent,
    AdminConfigurationComponent,
    AdminConfigurationFormComponent,
    AdminGenUserComponent,
    AdminPanelComponent,
    AdminPanelTestComponent,
    AppComponent,
    AppointmentComponent,
    AppointmentCreateFormComponent,
    AppointmentDetailsComponent,
    AppointmentListComponent,
    BookingPageComponent,
    ConfigPropertySetterComponent,
    DefaultHeaderComponent,
    LandingPageConfigurationComponent,
    NavComponent,
    SetupWizardComponent,
    SidenavAdminConfigComponent,
    SignInComponent,
    SignInFormComponent,
    SiteFooterComponent,
    SiteHeaderComponent,
    SplashComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserAnimationsModule,
    BrowserModule,
    FlexLayoutModule,
    FormsModule,
    HttpClientModule,
    MatCardModule,
    MatButtonModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatSidenavModule,
    MatSliderModule,
    MatListModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
    ReactiveFormsModule,
    RouterModule.forRoot([
      { path: '', component: SplashComponent },
      { path: 'admin-configuration', component: AdminConfigurationComponent },
      { path: 'admin-panel', component: AdminPanelComponent },
      { path: 'admin-panel-test', component: AdminPanelComponent },
      { path: 'appointment-details', component: AppointmentDetailsComponent },
      { path: 'booking-page', component: BookingPageComponent },
      { path: 'landing-page-component', component: LandingPageConfigurationComponent },
      { path: 'setup-wizard', component: SetupWizardComponent},
      { path: 'sign-in', component: SignInFormComponent },
      { path: 'site-header', component: SiteHeaderComponent },
    ]),
  ],

  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule { }
