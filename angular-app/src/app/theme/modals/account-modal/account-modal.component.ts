import { Component, Inject } from '@angular/core';
import { DomSanitizer } from "@angular/platform-browser";
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatIconRegistry } from '@angular/material/icon';
import { IUser } from 'src/app/models/interfaces/IUser';

@Component({
  selector: 'app-account-modal',
  templateUrl: './account-modal.component.html',
  styleUrls: ['./account-modal.component.css']
})
export class AccountModalComponent {

  userDisplayName;
  userEmail;
  userRole;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { theUser: IUser }, public dialogRef: MatDialogRef<AccountModalComponent>, private matIconRegistry: MatIconRegistry, private domSanitizer : DomSanitizer) { 
    this.userDisplayName = data.theUser.displayName;
    this.userEmail = data.theUser.email;
    this.userRole = data.theUser.role;

    this.matIconRegistry.addSvgIcon(
      "accountIcon",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/images/accountIcon.svg")
    );
    this.matIconRegistry.addSvgIcon(
      "emailIcon",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/images/emailIcon.svg")
    );
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
