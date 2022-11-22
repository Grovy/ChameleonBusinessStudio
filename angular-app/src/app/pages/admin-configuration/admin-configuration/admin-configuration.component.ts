import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { MatIconRegistry } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DomSanitizer } from '@angular/platform-browser';
import { catchError, Observable, tap, throwError } from 'rxjs';

type RGB = `rgb(${number}, ${number}, ${number})`;
type HEX = `#${string}`;
type Color = HEX | RGB;

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
  previewAndSaveImage: string = 'assets/images/admin-config-check.svg';

  logoFileName: string;
  splashFileName: string;
  bannerColor = '';

  isLinear = false;
  bannerFormGroup: FormGroup;
  logoFormGroup: FormGroup; 
  splashFormGroup: FormGroup;
  orgNameFormGroup: FormGroup;
  lastFormGroup: FormGroup;

  constructor(
    private _formBuilder: FormBuilder, 
    private http: HttpClient, 
    private _snackBar: MatSnackBar,
  ) { }

  ngOnInit(): void {
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
  

    this.lastFormGroup = this._formBuilder.group({
      submit: ['', Validators.required]
    });
  }

  // Function changes the name of the file to reflect in the UI
  onFileSelected(event) {
    const file: File = event.target.files[0];
    const splashFile: File = event.target.files[1];

    if(file) {
      this.logoFileName = file.name;
      this.splashFileName = splashFile.name;
    }
  }

  onColorSelected(event) {
    const value = event.target.value;
    value ? this.bannerColor = value : '';
    console.log("Selected color is: " + this.bannerColor);
  }

  onLogoSelected(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target != null && target.files != null && target.files.length > 0) {
      const file = target.files[0];
      this.logoFormGroup.get("logo")?.setValue(file);
      console.log("User selected their logo: ", file);
    }
  }

  /**
   * makes a POST request to /api/v1/config/logo with the logo the user selected
   */
  submitLogo() {
    // TODO move this into the service layer
    console.log("submitting logo...");
    const formData = new FormData();
    formData.append("file", this.logoFormGroup.get("logo")?.value);
    console.log(formData);

    this.http.post<any>("/api/v1/config/logo", formData)
      .pipe(
        tap(console.log), // todo better way of showing success or not
        catchError(this.handleError)
      )
      .subscribe();
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error(error);
    return throwError(() => error.message);
  }

  showPreviewLogo(event) {
    const logoFile = (event.target as HTMLInputElement).files[0];

    this.logoFormGroup.patchValue({
      avatar: logoFile[0]
    });

    this.logoFormGroup.get('avatar')?.updateValueAndValidity();
    
    const reader = new FileReader();
    reader.onload = () => {
      this.logoFileName = reader.result as string;
    }

    reader.readAsDataURL(logoFile);
  }

  showPreviewSplash(event) {
    const splashFile = (event.target as HTMLInputElement).files[0];

    this.splashFormGroup.patchValue({
      avatar: splashFile[0]
    });

    this.splashFormGroup.get('avatar')?.updateValueAndValidity();
    
    const reader = new FileReader();
    reader.onload = () => {
      this.splashFileName = reader.result as string;
    }

    reader.readAsDataURL(splashFile);
  }

  openSnackBar() {
    this._snackBar.open(
      "Configuration saved!", "Close");
  }
}