import { AdminConfigurationComponent } from './admin-configuration/admin-configuration.component';
import { AdminPanelComponent} from "./admin-panel/admin-panel.component";
import { AdminPanelTestComponent } from "./admin-panel-test/admin-panel-test.component";
import { AppointmentDetailsComponent } from './appointment-details/appointment-details.component';
import { BookingPageComponent} from "./booking-page/booking-page.component";
import { LandingPageConfigurationComponent } from "./landing-page-configuration/landing-page-configuration.component";
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SetupWizardComponent } from './setup-wizard/setup-wizard.component';
import { SignInFormComponent } from './sign-in-form/sign-in-form.component';
import { SiteHeaderComponent } from './site-header/site-header.component';
import { SplashComponent } from './splash/splash.component';

const routes: Routes = [
  { path: '', component: SplashComponent },
  { path: 'admin-configuration', component: AdminConfigurationComponent },
  { path: 'admin-panel', component: AdminPanelComponent },
  { path: 'admin-panel-test', component: AdminPanelTestComponent },
  { path: 'appointment-details', component: AppointmentDetailsComponent },
  { path: 'booking-page', component: BookingPageComponent },
  { path: 'landing-page-configuration', component: LandingPageConfigurationComponent },
  { path: 'setup-wizard', component: SetupWizardComponent },
  { path: 'sign-in', component: SignInFormComponent },
  { path: 'site-header', component: SiteHeaderComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
