import { Component, OnDestroy, OnInit } from '@angular/core';
import { MediaObserver, MediaChange } from '@angular/flex-layout';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { first, Subscription } from 'rxjs';

@Component({
  selector: 'app-setup-wizard',
  templateUrl: './setup-wizard.component.html',
  styleUrls: ['./setup-wizard.component.css']
})
export class SetupWizardComponent implements OnInit, OnDestroy {

  constructor(private mediaObserver:MediaObserver) {

this.email = new FormControl(" ",[Validators.required,Validators.email]);
  }
  profileForm!: FormGroup;
   email:FormControl;




  ngOnInit(): void {

    this.profileForm = new FormGroup({
      firstName: new FormControl("",[Validators.required,Validators.maxLength(25)]),
      lastName :new FormControl("",[Validators.required,Validators.maxLength(25)]),
      emails: new FormControl("",[Validators.required,Validators.email]),
      Phone: new FormControl("",[Validators.required,Validators.pattern("^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$")]),
      password: new FormControl("",[Validators.required,Validators.maxLength(15)]),
      nameofCompany: new FormControl("",[Validators.required,Validators.maxLength(30)]),
      address: new FormControl("",[Validators.required,Validators.maxLength(46)]),
      city: new FormControl("",[Validators.required]),
      state: new FormControl("",[Validators.required]),
      zipCode: new FormControl("",[Validators.required,Validators.maxLength(5)]),
      consent: new FormControl("i agree",[Validators.required])

    })


  }

ngOnDestroy(): void {


}

onSubmit(){
  if(this.profileForm.valid){
    console.log(this.profileForm.value);
  } else{
    console.log("Please provide sufficient information");
  }

}
getfirstNameError(){
  if(this.profileForm.controls["firstName"].hasError('required')){
    return "Name cannot be blank";
  }
  return this.profileForm.controls["firstName"].errors ? 'must be 25 characters': " ";
}
getlastNameError(){
  if(this.profileForm.controls["lastName"].hasError('required')){
    return "Name cannot be blank";
  }
  return this.profileForm.controls["lirstName"].errors ? 'must be 25 characters': " ";
}

getPasswordError(){
  if(this.profileForm.controls["password"].hasError('required')){
    return "Password cannot be blank";
  }
  return this.profileForm.controls["password"].errors? 'must be 15 characters': " ";

}
getErrorMessage(){
  if(this.profileForm.controls["emails"].hasError('required')){
    return "You must enter a value";
  }
  return this.profileForm.controls["emails"].hasError('email')? 'Not a valid email': ' ';
}
}
