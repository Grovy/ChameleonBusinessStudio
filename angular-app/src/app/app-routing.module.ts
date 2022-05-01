import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SetupWizardComponent } from './setup-wizard/setup-wizard.component';
import { SplashComponent } from './splash/splash.component';
import {SplashPagePreviewComponent} from "./splash-page-preview/splash-page-preview.component";
import { AdminConfigurationComponent } from './admin-configuration/admin-configuration.component';

const routes: Routes = [
  {path:'',component: SplashComponent},
  {path: 'admin-configuration', component: AdminConfigurationComponent},
  { path: 'setupWizard', component: SetupWizardComponent },
  {path: 'splash-page-preview', component: SplashPagePreviewComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
