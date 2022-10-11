import { AbstractControl, FormControl, FormGroup, Validators } from "@angular/forms";
import { IUser, UserRole } from 'src/app/models/interfaces/IUser';
import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-signup-modal',
  templateUrl: './signup-modal.component.html',
  styleUrls: ['./signup-modal.component.css']
})
export class SignupModalComponent {

  profileForm: FormGroup;

  constructor (public dialogRef: MatDialogRef<SignupModalComponent>) {
    this.profileForm = new FormGroup({
      displayName: new FormControl ('', [ Validators.required ]),
      confirmDisplayName: new FormControl('')
    }, { validators: validateDisplayName })
  }

  getDisplayName() {
    return this.profileForm.get('displayName');
  }

  getConfirmDisplayName() {
    return this.profileForm.get('confirmDisplayName');
  }
   
  onClickSubmit(data): void {
    const newUser: IUser = {
      displayName: data.displayName,
      email: "Test@email.com",
      role: UserRole.PARTICIPANT
    }
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
