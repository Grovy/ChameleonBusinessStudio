import { AccountModalComponent } from 'src/app/theme/modals/account-modal/account-modal.component';
import { Component } from '@angular/core';
import { DomSanitizer } from "@angular/platform-browser";
import { IUser } from 'src/app/models/interfaces/IUser';
import { MatDialog } from '@angular/material/dialog';
import { MatIconRegistry } from "@angular/material/icon";
import { AuthenticationService } from 'src/app/services/AuthenticationService.service';
import { UserService } from 'src/app/services/UserService.service';
import { WebsiteAppearanceService } from 'src/app/services/WebsiteAppearanceService.service';

@Component({
  selector: 'app-default-header',
  templateUrl: './default-header.component.html',
  styleUrls: ['./default-header.component.css']
})
export class DefaultHeaderComponent {
  organizationName: string = 'Chameleon Business Studio';
  public isRegisteredValue: boolean = false;
  public isAuthenticatedValue: boolean = false;
  public shouldDisplayModal: boolean = false;
  public userEmail: string = '';
  public currentUser?: IUser;

  constructor(
    public dialog: MatDialog, 
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private websiteAppearanceService: WebsiteAppearanceService,
    private matIconRegistry: MatIconRegistry, 
    private domSanitizer : DomSanitizer
  ) {
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
    this.updateBannerColor();
    this.updateOrganizationName();
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

  updateBannerColor() {
    this.websiteAppearanceService.getBannerColor()
      .subscribe(obj => {
        const h = <HTMLElement>document.querySelector("#site_header");
        h.style.backgroundColor = obj.color;
      });
  }

  updateOrganizationName() {
    this.websiteAppearanceService.getOrganizationName()
      .subscribe(obj => {
        this.organizationName = obj.name;
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
