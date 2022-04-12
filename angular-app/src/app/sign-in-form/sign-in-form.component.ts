import { Component, OnInit } from '@angular/core';
import { MatIconRegistry} from "@angular/material/icon";
import { DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-sign-in-form',
  templateUrl: './sign-in-form.component.html',
  styleUrls: ['./sign-in-form.component.css']
})
export class SignInFormComponent implements OnInit {

  private googleUrl : string =  "/oauth2/authorization/google";

  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer : DomSanitizer) {
    this.matIconRegistry.addSvgIcon(
      `google`,
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/images/google_icon.svg")
    );
  }

  goToGoogle(): void{

  }

  ngOnInit(): void {
  }

}
