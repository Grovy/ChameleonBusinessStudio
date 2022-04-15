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
   terms = "TERMS AND CONDITION";
   private mediaSub!: Subscription;
   paragraph="asdvkasdnvadf";
   deviceSmall!: boolean;

  ngOnInit(): void {
    this.profileForm = new FormGroup({
      firstName: new FormControl(" ",[Validators.required,Validators.maxLength(25)]),
      lastName :new FormControl("",[Validators.required,Validators.maxLength(25)]),
      emails: new FormControl("",[Validators.required,Validators.email]),
      Phone: new FormControl("",[Validators.required,Validators.max(10)]),
      password: new FormControl("",[Validators.required]),

    })

    this.mediaSub = this.mediaObserver.asObservable().subscribe((result)=>{
     result.forEach((item)=>{
       console.log(item.mqAlias);
     })
    })
  }

ngOnDestroy(): void {
   this.mediaSub.unsubscribe();
}
getfirstNameError(){
  if(this.profileForm.controls["firstName"].hasError('required')){
    return "Name cannot be blank"
  }
  return this.profileForm.controls["firstName"].errors ? 'must be 25 characters': " ";
}
getlastNameError(){
  if(this.profileForm.controls["lastName"].hasError('required')){
    return "Name cannot be blank"
  }
  return this.profileForm.controls["lirstName"].errors ? 'must be 25 characters': " ";
}
getErrorMessage(){
  if(this.email.hasError('required')){
    return "You must enter a value";
  }
  return this.email.hasError('email')? 'Not a valid email': ' ';
}
}
