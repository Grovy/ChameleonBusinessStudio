import { AbstractControl, FormControl, FormGroup, Validators } from "@angular/forms";
import { IUser, UserRole } from 'src/app/models/interfaces/IUser';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from "src/app/services/UserService.service";

@Component({
  selector: 'app-signup-modal',
  templateUrl: './signup-modal.component.html',
  styleUrls: ['./signup-modal.component.css']
})
export class SignupModalComponent implements OnInit {

  profileForm: FormGroup;
  userEmailValue: string;
  private phoneNumberValidators = [
    Validators.pattern("^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$")
  ];

  constructor (@Inject(MAT_DIALOG_DATA) public data: { userEmailValue: string}, private userService: UserService, public dialogRef: MatDialogRef<SignupModalComponent>) { }

  ngOnInit() {
    this.profileForm = new FormGroup({
      displayName: new FormControl ('', [ Validators.required ]),
      confirmDisplayName: new FormControl('', [ Validators.required ]),
      phoneNumber: new FormControl('', this.phoneNumberValidators),
    }, { validators: validateDisplayName });

    this.profileForm.get('phoneNumber').valueChanges
        .subscribe(value => {
            this.profileForm.get('phoneNumber').setValidators(this.phoneNumberValidators.concat(conditionalRequired(value)))
        }
    );

    this.userEmailValue = this.data.userEmailValue;
  }

  getDisplayName() {
    return this.profileForm.get('displayName');
  }

  getConfirmDisplayName() {
    return this.profileForm.get('confirmDisplayName');
  }

  getPhoneNumber() {
    return this.profileForm.get('phoneNumber');
  }
   
  onClickSubmit(data): void {
    const newUser: IUser = {
      displayName: data.displayName,
      email: this.userEmailValue,
      role: "PARTICIPANT" as UserRole,
      phoneNumber: "+1" + data.phoneNumber
    }
    
    // Will need to add error handling and a spinner animation here later
    this.userService.createUser(newUser).subscribe(
      data => console.log(data)
    );

    this.dialogRef.close();
  }

  onNoClick(): void {
    this.dialogRef.close();
  }  

  onClear(): void {
    this.profileForm.reset();
  }
}

// Validates the Display Name is the same as the Confirmation Name
export function validateDisplayName(c: AbstractControl) {
  return c.get('displayName')?.value === c.get('confirmDisplayName')?.value ? null : { 'invalidDisplayName' : true };
}

function conditionalRequired(condition) {
  return condition ? Validators.required : Validators.nullValidator;
}
