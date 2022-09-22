import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Component } from '@angular/core';

export enum SplashLayout {
  'Option1',
  'Option2',
  'Option3',
  'Option4',
}

@Component({
  selector: 'app-landing-page-configuration',
  templateUrl: './landing-page-configuration.component.html',
  styleUrls: ['./landing-page-configuration.component.css']
})
export class LandingPageConfigurationComponent {

  layoutOption: string;
  selected = '';
  backgroundImage: string;
  contactUsContent: string;
  aboutUsContent: string;

  isLinear = false;
  layoutFormGroup: FormGroup;
  backgroundImageGroup: FormGroup;
  contactUsGroup: FormGroup;
  aboutUsGroup: FormGroup;

  constructor(private _formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.layoutFormGroup = this._formBuilder.group({
      layoutOption: ['', Validators.required]
    })

    this.backgroundImageGroup = this._formBuilder.group({
      background: ['', Validators.required],
      avatar: [null],
      name: ['']
    })

    this.contactUsGroup = this._formBuilder.group({
      contactUsForm: ['', Validators.required]
    })

    this.aboutUsGroup = this._formBuilder.group({
      aboutUsForm: ['', Validators.required]
    })
  }

  onFileSelected(event) {
    const file: File = event.target.files[0];
    const backgroundImageFile: File = event.target.files[1];

    if(file) {
      this.backgroundImage = file.name;
    }

    this.backgroundImageGroup.get('avatar')?.updateValueAndValidity();

    const reader = new FileReader();
    reader.onload = () => {
      this.backgroundImage = reader.result as string;
    }

    reader.readAsDataURL(backgroundImageFile);

  }


}
