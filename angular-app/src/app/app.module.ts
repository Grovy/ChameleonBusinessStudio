import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule} from "@angular/forms";
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';
import { SignInComponent } from './signin/signin.component';
import { SiteHeaderComponent } from './site-header/site-header.component';
import { SplashComponent } from './splash/splash.component';
import {MatIcon} from "@angular/material/icon";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatToolbarModule} from "@angular/material/toolbar";
import { FlexLayoutModule, MediaObserver } from '@angular/flex-layout';
import { MatSliderModule } from '@angular/material/slider';
import { AdminConfigContainerComponent } from "./admin-config-container/admin-config-container.component";
import { AdminConfigurationComponent } from './admin-configuration/admin-configuration.component';
import { AdminConfigurationFormComponent } from './admin-configuration-form/admin-configuration-form.component';
import { AppointmentComponent} from './appointment/appointment.component';
import { AppointmentListComponent } from './appointment-list/appointment-list.component';
import { BookingPageComponent} from './booking-page/booking-page.component';
import {MatButtonModule} from "@angular/material/button";
import { HelloComponent } from './hello/hello.component';
import {MatListModule} from "@angular/material/list";
import {MatTabsModule} from "@angular/material/tabs";
import { SetupWizardComponent } from './setup-wizard/setup-wizard.component';
import { SignInFormComponent } from './sign-in-form/sign-in-form.component';
import {MatIconModule} from "@angular/material/icon";
import {ReactiveFormsModule} from '@angular/forms'
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { SidenavAdminConfigComponent } from './sidenav-admin-config/sidenav-admin-config.component';
import { SplashPagePreviewComponent } from './splash-page-preview/splash-page-preview.component';
import {MatDatepickerModule} from "@angular/material/datepicker";
import { SiteFooterComponent } from './site-footer/site-footer.component';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { AdminGenUserComponent } from './admin-gen-user/admin-gen-user.component';
import {MatCardModule} from '@angular/material/card';
import { DefaultHeaderComponent } from './default-header/default-header.component';
import { LandingPageConfigurationComponent } from './landing-page-configuration/landing-page-configuration.component';
import {MatSelect, MatSelectModule} from "@angular/material/select";

@NgModule({
  declarations: [
    AppComponent,
    SignInComponent,
    SiteHeaderComponent,
    SplashComponent,
    HelloComponent,
    AdminConfigurationComponent,
    AdminConfigContainerComponent,
    AdminConfigurationFormComponent,
    AppointmentComponent,
    AppointmentListComponent,
    BookingPageComponent,
    SplashComponent,
    SetupWizardComponent,
    SignInFormComponent,
    AdminPanelComponent,
    SidenavAdminConfigComponent,
    SplashPagePreviewComponent,
    SiteFooterComponent,
    DefaultHeaderComponent,
    AdminGenUserComponent,
    SiteFooterComponent,
    LandingPageConfigurationComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    MatButtonModule,
    RouterModule.forRoot([
      {path: 'admin-configuration', component: AdminConfigurationComponent},
      {path: '', component: SplashComponent},
      {path: 'booking-page', component: BookingPageComponent},
      {path: 'splash-page-preview', component: SplashPagePreviewComponent},
      {path: 'sign-in', component: SignInFormComponent},
      {path: 'site-header', component: SiteHeaderComponent},
      {path: 'admin-panel', component: AdminPanelComponent},
      {path: 'landing-page-component', component: LandingPageConfigurationComponent},
    ]),
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatListModule,
    MatTabsModule,
    FlexLayoutModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MatCardModule,
    MatIconModule,
    FormsModule,
    MatSelectModule,
  ],

  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule { }
