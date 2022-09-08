/*
    If you have to import something here, please try to maintain alphabetical order
*/
import { NgModule } from '@angular/core';

import { AdminConfigContainerComponent } from "./admin-config-container/admin-config-container.component";
import { AdminConfigurationComponent } from './admin-configuration/admin-configuration.component';
import { AdminConfigurationFormComponent } from './admin-configuration-form/admin-configuration-form.component';
import { AdminGenUserComponent } from './admin-gen-user/admin-gen-user.component';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { AdminPanelTestComponent } from './admin-panel-test/admin-panel-test.component';
import { AppComponent } from './app.component';
import { AppointmentComponent} from './appointment/appointment.component';
import { AppointmentDetailsComponent } from './appointment-details/appointment-details.component';
import { AppointmentListComponent } from './appointment-list/appointment-list.component';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { BookingPageComponent} from './booking-page/booking-page.component';
import { DefaultHeaderComponent } from './default-header/default-header.component';
import { FlexLayoutModule, MediaObserver } from '@angular/flex-layout';
import { FormsModule} from "@angular/forms";
import { HelloComponent } from './hello/hello.component';
import { HttpClientModule } from '@angular/common/http';
import { LandingPageConfigurationComponent } from './landing-page-configuration/landing-page-configuration.component';
import { NavComponent } from './nav/nav.component';
import { ReactiveFormsModule } from '@angular/forms'
import { RouterModule } from '@angular/router';
import { SetupWizardComponent } from './setup-wizard/setup-wizard.component';
import { SidenavAdminConfigComponent } from './sidenav-admin-config/sidenav-admin-config.component';
import { SignInComponent } from './signin/signin.component';
import { SignInFormComponent } from './sign-in-form/sign-in-form.component';
import { SiteFooterComponent } from './site-footer/site-footer.component';
import { SiteHeaderComponent } from './site-header/site-header.component';
import { SplashComponent } from './splash/splash.component';

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
    AppointmentDetailsComponent,
    AppointmentListComponent,
    BookingPageComponent,
    DefaultHeaderComponent,
    HelloComponent,
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
