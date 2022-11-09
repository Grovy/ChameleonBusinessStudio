/*
    If you have to import something here, maintain alphabetical order
*/
import { NgModule } from '@angular/core';

import { AccountModalComponent } from './theme/modals/account-modal/account-modal.component';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { AdminConfigurationComponent } from './pages/admin-configuration/admin-configuration/admin-configuration.component';
import { AdminGenUserComponent } from './pages/admin-panel/admin-gen-user/admin-gen-user.component';
import { AdminNavComponent } from './pages/nav/admin-nav/admin-nav.component';
import { AdminPanelComponent } from './pages/admin-panel/admin-panel/admin-panel.component';
import { AdminPanelTestComponent } from './pages/admin-panel/admin-panel-test/admin-panel-test.component';
import { AppComponent } from './app.component';
import { AppointmentComponent} from './pages/appointments/appointment/appointment.component';
import { AppointmentCreateFormComponent } from './pages/appointments/appointment-create-form/appointment-create-form.component';
import { AppointmentDateFilterPipe } from './services/AppointmentDateFilterPipe';
import { AppointmentDetailsComponent } from './pages/appointments/appointment-details/appointment-details.component';
import { AppointmentListComponent } from './pages/appointments/appointment-list/appointment-list.component';
import { AppointmentService } from './services/AppointmentService.service';
import { AppRoutingModule } from './app-routing.module';
import { AuthenticationService } from './services/AuthenticationService.service';
import { BookedApptsWidgetComponent } from './theme/widgets/booked-appts-widget/booked-appts-widget.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { BookingPageComponent} from './pages/appointments/booking-page/booking-page.component';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { CalenderViewComponent } from './pages/appointments/appointment-calender/calender-view/calender-view.component';
import { CustomBannerDirective } from './theme/directives/custom-banner.directive';
import { DateManager } from './services/DateManager';
import { DefaultHeaderComponent } from './theme/default-header/default-header.component';
import { FlexLayoutModule, MediaObserver } from '@angular/flex-layout';
import { FormsModule} from "@angular/forms";
import { HttpClientModule } from '@angular/common/http';
import { LandingPageConfigurationComponent } from './pages/admin-configuration/landing-page-configuration/landing-page-configuration.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { ParticipantNavComponent } from './pages/nav/participant-nav/participant-nav.component';
import { ReactiveFormsModule } from '@angular/forms'
import { RouterModule } from '@angular/router';
import { ScheduleConfigurationComponent } from './pages/schedule-configuration/schedule-configuration.component';
import { ScheduleService } from './services/ScheduleService.service';
import { SetupWizardComponent } from './pages/auth/setup-wizard/setup-wizard.component';
import { SignInComponent } from './pages/auth/signin/signin.component';
import { SignInFormComponent } from './pages/auth/sign-in-form/sign-in-form.component';
import { SignupModalComponent } from './theme/modals/signup-modal/signup-modal.component';
import { SiteFooterComponent } from './theme/site-footer/site-footer.component';
import { SiteHeaderComponent } from './theme/site-header/site-header.component';
import { SplashComponent } from './pages/dashboard/splash/splash.component';
import { TalentNavComponent } from './pages/nav/talent-nav/talent-nav.component';
import { UserService } from './services/UserService.service';



// All Angular Material imports
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox'; 
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from "@angular/material/list";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from "@angular/material/select";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatSliderModule } from '@angular/material/slider';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatStepperModule } from '@angular/material/stepper';
import { MatTableModule } from "@angular/material/table";
import { MatTabsModule } from "@angular/material/tabs";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatTooltipModule } from '@angular/material/tooltip';
import { EventWidgetComponent } from './theme/widgets/event-widget/event-widget.component';


@NgModule({
  declarations: [
    AdminConfigurationComponent,
    AdminGenUserComponent,
    AdminPanelComponent,
    AdminPanelTestComponent,
    AppComponent,
    AppointmentComponent,
    AppointmentCreateFormComponent,
    AppointmentDateFilterPipe,
    AppointmentDetailsComponent,
    AppointmentListComponent,
    BookedApptsWidgetComponent,
    BookingPageComponent,
    CalenderViewComponent,
    CustomBannerDirective,
    DefaultHeaderComponent,
    LandingPageConfigurationComponent,
    PageNotFoundComponent,
    ScheduleConfigurationComponent,
    SetupWizardComponent,
    SignInComponent,
    SignInFormComponent,
    SignupModalComponent,
    SiteFooterComponent,
    SiteHeaderComponent,
    SplashComponent,
    AccountModalComponent,
    TalentNavComponent,
    AdminNavComponent,
    ParticipantNavComponent,
    EventWidgetComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserAnimationsModule,
    BrowserModule,
    FlexLayoutModule,
    FormsModule,
    HttpClientModule,
    MatCardModule,
    MatCheckboxModule,
    MatDialogModule,
    MatButtonModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatSidenavModule,
    MatSliderModule,
    MatSnackBarModule,
    MatStepperModule,
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
      { path: 'appointment-list',component:AppointmentListComponent},
      { path: 'appointment-creation',component:AppointmentCreateFormComponent},
      { path: 'booking-page', component: BookingPageComponent },
      { path: 'calender-view',component: CalenderViewComponent },
      { path: 'landing-page-component', component: LandingPageConfigurationComponent },
      { path: 'schedule-configuration', component: ScheduleConfigurationComponent },
      { path: 'setup-wizard', component: SetupWizardComponent},
      { path: 'sign-in', component: SignInFormComponent },
      { path: 'site-header', component: SiteHeaderComponent },

      { path: '**', component: PageNotFoundComponent }
    ]),
    CalendarModule.forRoot({
        provide:DateAdapter,
        useFactory:adapterFactory,
    }),
  ],

  providers: [
    AppointmentService,
    AuthenticationService,
    DateManager,
    ScheduleService,
    UserService
  ],
  bootstrap: [AppComponent]
})

export class AppModule { }
