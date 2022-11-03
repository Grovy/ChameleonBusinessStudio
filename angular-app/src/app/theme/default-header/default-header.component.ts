import { AccountModalComponent } from 'src/app/theme/modals/account-modal/account-modal.component';
import { Component } from '@angular/core';
import { DomSanitizer } from "@angular/platform-browser";
import { IUser } from 'src/app/models/interfaces/IUser';
import { MatDialog } from '@angular/material/dialog';
import { MatIconRegistry } from "@angular/material/icon";
import { AuthenticationService } from 'src/app/services/AuthenticationService.service';
import { UserService } from 'src/app/services/UserService.service';

@Component({
  selector: 'app-default-header',
  templateUrl: './default-header.component.html',
  styleUrls: ['./default-header.component.css']
})
export class DefaultHeaderComponent {

  public isRegisteredValue: boolean;
  public isAuthenticatedValue: boolean;
  public shouldDisplayModal: boolean;
  public userEmail: string;
  public currentUser: IUser;

  constructor(public dialog: MatDialog, private authenticationService: AuthenticationService, private userService: UserService, private matIconRegistry: MatIconRegistry, private domSanitizer : DomSanitizer) {
    this.matIconRegistry.addSvgIcon(
      `github`,
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/images/icons8-github.svg")
    );
    this.matIconRegistry.addSvgIcon(
      'linkedIn',
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/images/icons8-linkedin-circled.svg")
    );
  }

  ngOnInit(): void {
    this.checkIfAuthenticated();
    this.checkIfRegisteredWithVendia();
  }

  checkIfAuthenticated() {
    this.authenticationService.isAuthenticated().subscribe(
      data => { 
        this.isAuthenticatedValue = data;
    });
  }

  getUserEmail() {
    this.authenticationService.getPrincipal().subscribe(
      data => { 
        this.userEmail = data.valueOf(); 
        this.userService.getUser(this.userEmail).subscribe(data => {this.currentUser = data});
    });  
  }

  checkIfRegisteredWithVendia() {
    this.authenticationService.isUserRegistered().subscribe(
      data => { 
        this.isRegisteredValue = data;
        this.getUserEmail();
    });
  }

  onClickProfile(): void {
    this.displayProfileModal(this.currentUser);
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
