import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { WebsiteAppearanceService } from '../../../services/WebsiteAppearanceService.service';

@Component({
  selector: 'app-admin-configuration',
  templateUrl: './admin-configuration.component.html',
  styleUrls: ['./admin-configuration.component.css']
})
export class AdminConfigurationComponent {
  // Image paths
  bannerColorImage: string = 'assets/images/paintbrush.png';
  logoCardImage: string = 'assets/images/upload-logo.svg';
  companyNameImage: string = 'assets/images/type-company-name.svg';
  previewAndSaveImage: string = 'assets/images/admin-config-check.svg'; // not used

  logoFile?: File;
  logoFileName: string = '';

  bannerColor = '';

  isLinear = false;
  bannerFormGroup: FormGroup;
  logoFormGroup: FormGroup; 
  splashFormGroup: FormGroup;
  orgNameFormGroup: FormGroup;

  constructor(
    private service: WebsiteAppearanceService,
    private _formBuilder: FormBuilder,
    private _snackBar: MatSnackBar,
  ) { 
    this.bannerFormGroup = this._formBuilder.group({
      banner_color: ['', Validators.required]
    });

    this.logoFormGroup = this._formBuilder.group({
      logo: ['', Validators.required],
      avatar: [null],
      name: ['']
    });

    this.splashFormGroup = this._formBuilder.group({
      splash: ['', Validators.required],
      avatar: [null],
      name: ['']
    });

    this.orgNameFormGroup = this._formBuilder.group({
      org_name: ['', Validators.required]
    });
  }

  /**
   * Called whenever the user selects a new banner color
   * @param event the HTML event that occurs when a new color is selected
   */
  onColorSelected(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target != null) {
      this.bannerColor = target.value ?? '';
      console.log("Selected color is: " + this.bannerColor);
      const e = document.getElementById("banner-color-display");
      if (e != null) {
        e.style.backgroundColor = this.bannerColor;
      }
    }
  }

  /**
   * posts the banner color to the backend
   */
  submitBannerColor() {
    console.log("Submitting color...");
    this.service.setBannerColor(this.bannerColor)
      .subscribe(this.showSnackBar("Saved banner color successfully!"));
  }

  /**
   * Called whenever the user selects a new logo file.
   * 
   * @param event the HTML event that occurs when the logo is selected
   */
  onLogoSelected(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target != null && target.files != null && target.files.length > 0) {
      const file = target.files[0];
      this.logoFile = file;

      // update avatar
      this.logoFormGroup.patchValue({
        avatar: file
      });
      this.logoFormGroup.get('avatar')?.updateValueAndValidity();
      const reader = new FileReader();
      reader.onload = () => {
        this.logoFileName = reader.result as string;
      }
      reader.readAsDataURL(file);

      console.log("User selected their logo: ", file);
    }
  }

  /**
   * posts the new website logo to the backend
   */
  submitLogo() {
    console.log("submitting logo...");
    if (this.logoFile) {
      this.service.setLogo(this.logoFile)
        .subscribe(this.showSnackBar("Saved logo successfully!"));
    }
  }

  /**
   * posts the splash page to the backend
   */
  submitSplashPage() {
    console.log("Submitting splash page...");
    const text = this.splashFormGroup.get('splash')?.value;

    if (text != null) {
      this.service.setSplashPage(text)
        .subscribe(this.showSnackBar("Saved splash page successfully!"));
    }
  }

  /**
   * posts the selected organization name to the backend
   */
  submitOrganizationName() {
    console.log("Submitting organization name...");
    const orgName = this.orgNameFormGroup.get('org_name')?.value; 
    if (orgName != null) {
      this.service.setOrganizationName(orgName)
        .subscribe(this.showSnackBar("Save organization name successfully!"));
    }
  }

  /**
   * Shows a success message for a brief period of time.
   * 
   * @param successMessage the message to display
   * @returns an observer to pass to `Observable::subscribe`
   */
  private showSnackBar(successMessage: string) {
    return {
      next: () => this._snackBar.open(successMessage, "OK", {
        duration: 3000
      }),
      error: (error: Error) => this._snackBar.open(`An error occured: ${error}`, "Yikes", {
        duration: 3000
      })
    }
  }
}