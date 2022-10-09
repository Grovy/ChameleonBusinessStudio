import { Component } from '@angular/core';
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
export class SplashComponent {

  constructor(private http: HttpClient, private matIconRegistry: MatIconRegistry, private domSanitizer : DomSanitizer, public dialog: MatDialog) {
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
    http.get<{content: string}>("/custom/splash").subscribe((obj: {content: string})=>{
      const e = <HTMLElement>document.querySelector("#splash");
      e.innerHTML = obj.content; // replace element content
    });
  }

  displayModal(): void {
    const dialogRef = this.dialog.open(SignupModalComponent, { disableClose: true });

    dialogRef.afterClosed().subscribe(result => {
      console.log("The modal was closed");
    });
  }

}
