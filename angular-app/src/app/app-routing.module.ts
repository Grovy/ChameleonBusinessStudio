import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SetupWizardComponent } from './setup-wizard/setup-wizard.component';
import { SplashComponent } from './splash/splash.component';

const routes: Routes = [
  {path:'',component: SplashComponent},
  { path: 'setupWizard', component: SetupWizardComponent },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
