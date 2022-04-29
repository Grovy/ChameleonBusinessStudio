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
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatToolbarModule} from "@angular/material/toolbar";
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatSliderModule } from '@angular/material/slider';
import { AdminConfigContainerComponent } from "./admin-config-container/admin-config-container.component";
import { AdminConfigurationComponent } from './admin-configuration/admin-configuration.component';
import {MatButtonModule} from "@angular/material/button";
import { HelloComponent } from './hello/hello.component';
import {MatListModule} from "@angular/material/list";
import {MatTabsModule} from "@angular/material/tabs";
import { SetupWizardComponent } from './setup-wizard/setup-wizard.component';
import { SignInFormComponent } from './sign-in-form/sign-in-form.component';
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {ReactiveFormsModule} from '@angular/forms'
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { SidenavAdminConfigComponent } from './sidenav-admin-config/sidenav-admin-config.component';
import { SplashPagePreviewComponent } from './splash-page-preview/splash-page-preview.component';
import {MatDatepickerModule} from "@angular/material/datepicker";
import { SiteFooterComponent } from './site-footer/site-footer.component';


@NgModule({
  declarations: [
    AppComponent,
    SignInComponent,
    SiteHeaderComponent,
    SplashComponent,
    HelloComponent,
    AdminConfigurationComponent,
    AdminConfigContainerComponent,
    SplashComponent,
    SetupWizardComponent,
    SignInFormComponent,
    SidenavAdminConfigComponent,
    SplashPagePreviewComponent,
    SiteFooterComponent,
  ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        HttpClientModule,
        MatButtonModule,
        RouterModule.forRoot([
            {path: '', component: SplashComponent},
            {path: 'admin-configuration', component: AdminConfigurationComponent},
            {path: 'splash-page-preview', component: SplashPagePreviewComponent},
            {path: 'sign-in', component: SignInFormComponent},
            {path: 'site-header', component: SiteHeaderComponent}
        ]),
        MatToolbarModule,
        MatButtonModule,
        MatSidenavModule,
        MatListModule,
        MatTabsModule,
        MatCardModule,
        MatIconModule,
        FlexLayoutModule,
        MatFormFieldModule,
        ReactiveFormsModule,
        MatInputModule,
        MatDatepickerModule,


    ],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule { }
