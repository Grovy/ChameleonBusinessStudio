import { AdminConfigurationComponent } from './admin-configuration/admin-configuration.component';
import { AdminPanelComponent} from "./admin-panel/admin-panel.component";
import { AdminPanelTestComponent } from "./admin-panel-test/admin-panel-test.component";
import { BookingPageComponent} from "./booking-page/booking-page.component";
import { LandingPageConfigurationComponent } from "./landing-page-configuration/landing-page-configuration.component";
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SetupWizardComponent } from './setup-wizard/setup-wizard.component';
import { SplashComponent } from './splash/splash.component';

const routes: Routes = [
  { path: '', component: SplashComponent },
  { path: 'setup-wizard', component: SetupWizardComponent },
  { path: 'admin-configuration', component: AdminConfigurationComponent },
  { path: 'booking-page', component: BookingPageComponent },
  { path: 'admin-panel', component: AdminPanelComponent },
  { path: 'admin-panel-test', component: AdminPanelTestComponent },
  { path: 'landing-page-configuration', component: LandingPageConfigurationComponent },


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
