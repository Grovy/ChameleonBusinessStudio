import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule} from "@angular/forms";
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';
import { SignInComponent } from './sign-in-form/signin/signin.component';
import { SplashComponent } from './splash/splash.component';
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatToolbarModule} from "@angular/material/toolbar";
import { MatSliderModule } from '@angular/material/slider';
import { AdminConfigurationComponent } from './admin-configuration/admin-configuration.component';
import {MatButtonModule} from "@angular/material/button";
import { HelloComponent } from './hello/hello.component';
import {MatListModule} from "@angular/material/list";
import {MatTabsModule} from "@angular/material/tabs";
import { SetupWizardComponent } from './setup-wizard/setup-wizard.component';
import { SignInFormComponent } from './sign-in-form/sign-in-form.component';
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";


@NgModule({
  declarations: [
    AppComponent,
    SignInComponent,
    SplashComponent,
    HelloComponent,
    AdminConfigurationComponent,
    SplashComponent,
    SetupWizardComponent,
    SignInFormComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    MatButtonModule,
    RouterModule.forRoot([
      {path: '', component: SplashComponent},
      {path: 'admin-configuration', component: AdminConfigurationComponent},
      {path: 'sign-in', component: SignInFormComponent}
    ]),
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatListModule,
    MatTabsModule,
    MatCardModule,
    MatIconModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule { }
