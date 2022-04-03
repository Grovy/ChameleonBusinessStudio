import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';
import { SignInComponent } from './signin/signin.component';
import { SplashPageComponent } from './splash/splash.component';
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatToolbarModule} from "@angular/material/toolbar";
import { MatSliderModule } from '@angular/material/slider';
import { AdminConfigurationComponent } from './admin-configuration/admin-configuration.component';
import {MatButtonModule} from "@angular/material/button";


@NgModule({
  declarations: [
    AppComponent,
    SignInComponent,
    SplashPageComponent,
    AdminConfigurationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    MatButtonModule,
    RouterModule.forRoot([
      {path: 'admin-configuration', component: AdminConfigurationComponent},
    ]),
    MatToolbarModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule { }
