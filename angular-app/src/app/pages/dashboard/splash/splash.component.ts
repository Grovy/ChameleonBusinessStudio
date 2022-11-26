import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/AuthenticationService.service';
import { DomSanitizer } from "@angular/platform-browser";
import { HttpClient } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { MatIconRegistry } from "@angular/material/icon";
import { SignupModalComponent } from 'src/app/theme/modals/signup-modal/signup-modal.component';
import { AccountModalComponent } from 'src/app/theme/modals/account-modal/account-modal.component';
import { UserService } from 'src/app/services/UserService.service';
import { IUser } from 'src/app/models/interfaces/IUser';
import { WebsiteAppearanceService } from 'src/app/services/WebsiteAppearanceService.service';


@Component({
  selector: 'app-splash',
  templateUrl: './splash.component.html',
  styleUrls: ['./splash.component.css']
})
export class SplashComponent implements OnInit {

  public isRegisteredValue: boolean = false;
  public isAuthenticatedValue: boolean = false;
  public shouldDisplayModal: boolean = false;
  public userEmail: string = '';
  public currentUser?: IUser;
  customPage: string = '';
  reqCompleted: boolean = false;

  constructor(
    private matIconRegistry: MatIconRegistry, 
    private domSanitizer : DomSanitizer, 
    public dialog: MatDialog, 
    private authenticationService: AuthenticationService, 
    private userService: UserService,
    private websiteAppearanceService: WebsiteAppearanceService
  ) {
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
    this.matIconRegistry.addSvgIcon(
      'accountIcon',
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/images/accountIcon.svg")
    );
  }


  ngOnInit(): void {
    this.checkIfAuthenticated();
    this.checkIfRegisteredWithVendia();
    this.loadCustomSplashPage();
    this.shouldDisplayModal = (this.isAuthenticatedValue && this.isRegisteredValue === false);
  }

  getUserEmail() {
    this.authenticationService.getPrincipal().subscribe(
      data => { 
        this.userEmail = data.valueOf(); 
        this.updateShouldDisplayModal(this.isRegisteredValue, this.userEmail);
        this.userService.getUser(this.userEmail).subscribe(data => {this.currentUser = data});
        this.reqCompleted = true;
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

  loadCustomSplashPage() {
    this.websiteAppearanceService.getSplashPage()
      .subscribe((obj: {content: string}) => {
        this.customPage = obj.content;
      });
  }

  updateShouldDisplayModal(newRegisteredValue: boolean, email: string): void {
    if(this.shouldDisplayModal = (this.isAuthenticatedValue && newRegisteredValue === false) && email !== undefined){
      this.displayModal(email);
    }
  }

  displayModal(email: string): void {
    const dialogRef = this.dialog.open(SignupModalComponent, { 
      disableClose: true,
      data: { userEmailValue: email } 
    });
  }
  
  onClickProfile(): void {
    if (this.currentUser != null) {
      this.displayProfileModal(this.currentUser);
    }
  }

  displayProfileModal(user: IUser) {
    const dialogRef = this.dialog.open(AccountModalComponent, {
      width: '450px',
      height: '150px',
      disableClose: false,
      position: {
        right: "5px",
        top: "100px"
      },
      hasBackdrop: false,
      data: { theUser: user }
    }); 

  }

}
