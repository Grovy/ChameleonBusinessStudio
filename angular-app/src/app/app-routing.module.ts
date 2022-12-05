import { AdminConfigurationComponent } from './pages/admin-configuration/admin-configuration/admin-configuration.component';
import { AdminPanelComponent} from "./pages/admin-panel/admin-panel/admin-panel.component";
import { AdminPanelTestComponent } from "./pages/admin-panel/admin-panel-test/admin-panel-test.component";
import { AppointmentComponent } from './pages/appointments/appointment/appointment.component';
import { AppointmentCreateFormComponent } from './pages/appointments/appointment-create-form/appointment-create-form.component';
import { AppointmentDetailsComponent } from './pages/appointments/appointment-details/appointment-details.component';
import { BookingPageComponent} from "./pages/appointments/booking-page/booking-page.component";
import { LandingPageConfigurationComponent } from "./pages/admin-configuration/landing-page-configuration/landing-page-configuration.component";
import { NgModule } from '@angular/core';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { RouterModule, Routes } from '@angular/router';
import { ScheduleConfigurationComponent } from './pages/schedule-configuration/schedule-configuration.component';
import { SetupWizardComponent } from './pages/auth/setup-wizard/setup-wizard.component';
import { SignInFormComponent } from './pages/auth/sign-in-form/sign-in-form.component';
import { SplashComponent } from './pages/dashboard/splash/splash.component';

const routes: Routes = [
  { path: '', component: SplashComponent },
  { path: 'admin-configuration', component: AdminConfigurationComponent },
  { path: 'admin-panel', component: AdminPanelComponent },
  { path: 'admin-panel-test', component: AdminPanelTestComponent },
  { path: 'appointment',component:AppointmentComponent},
  { path: 'appointment-details', component: AppointmentDetailsComponent },
  { path: 'appointment-creation', component: AppointmentCreateFormComponent },
  { path: 'booking-page', component: BookingPageComponent },
  { path: 'landing-page-configuration', component: LandingPageConfigurationComponent },
  { path: 'schedule-configuration', component: ScheduleConfigurationComponent },
  { path: 'setup-wizard', component: SetupWizardComponent },
  { path: 'sign-in', component: SignInFormComponent },
  { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
