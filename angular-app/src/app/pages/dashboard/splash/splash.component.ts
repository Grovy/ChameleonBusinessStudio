import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/AuthenticationService.service';
import { DomSanitizer } from "@angular/platform-browser";
import { HttpClient } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { MatIconRegistry } from "@angular/material/icon";
import { SignupModalComponent } from 'src/app/theme/modals/signup-modal/signup-modal.component';


@Component({
  selector: 'app-splash',
  templateUrl: './splash.component.html',
  styleUrls: ['./splash.component.css']
})
export class SplashComponent implements OnInit {

  public isRegisteredValue: boolean;
  public isAuthenticatedValue: boolean;
  public shouldDisplayModal: boolean;
  public userEmail: string;

  constructor(private http: HttpClient, private matIconRegistry: MatIconRegistry, private domSanitizer : DomSanitizer, public dialog: MatDialog, private authenticationService: AuthenticationService) {
    this.matIconRegistry.addSvgIcon(
      "calendar",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/images/icons8-calendar.svg")
    );
    this.matIconRegistry.addSvgIcon(
      `github`,
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/images/icons8-github.svg")
    );
    this.matIconRegistry.addSvgIcon(
      'linkedIn',
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/images/icons8-linkedin-circled.svg")
    );
    this.matIconRegistry.addSvgIcon(
      'graphQl',
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/images/icons8-graphql.svg")
    );
    this.matIconRegistry.addSvgIcon(
      'checkAll',
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/images/icons8-check-all.svg")
    );
    this.matIconRegistry.addSvgIcon(
      'edit',
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/images/icons8-edit.svg")
    );
  }

  ngOnInit(): void {
    this.checkIfAuthenticated();
    this.checkIfRegisteredWithVendia();
    this.shouldDisplayModal = (this.isAuthenticatedValue && this.isRegisteredValue === false);
  }

  getUserEmail() {
    this.authenticationService.getPrincipal().subscribe(
      data => { 
        this.userEmail = data.valueOf(); 
        this.updateShouldDisplayModal(this.isRegisteredValue, this.userEmail);
    });  
  }

  checkIfAuthenticated() {
    this.authenticationService.isAuthenticated().subscribe(
      data => { 
        this.isAuthenticatedValue = data;
    });
  }

  checkIfRegisteredWithVendia() {
    this.authenticationService.isUserRegistered().subscribe(
      data => { 
        this.isRegisteredValue = data;
        this.getUserEmail();
        this.updateShouldDisplayModal(this.isRegisteredValue, this.userEmail); 
    });
  }


  updateShouldDisplayModal(newRegisteredValue: boolean, email: string): void {
    if(this.shouldDisplayModal = (this.isAuthenticatedValue && newRegisteredValue === false) && email !== undefined){
      this.displayModal(email);
    }
  }

  displayModal(email: string): void {
    console.log("About to build the modal. The value of the email here is: " + email);
    const dialogRef = this.dialog.open(SignupModalComponent, { 
      disableClose: true,
      data: { userEmailValue: email } 
    });
  } 

}
