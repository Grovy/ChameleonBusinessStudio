import { AbstractControl, FormControl, FormGroup, Validators } from "@angular/forms";
import { IUser, UserRole } from 'src/app/models/interfaces/IUser';
import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from "src/app/services/UserService.service";

@Component({
  selector: 'app-signup-modal',
  templateUrl: './signup-modal.component.html',
  styleUrls: ['./signup-modal.component.css']
})
export class SignupModalComponent {

  profileForm: FormGroup;
  userEmailValue: string;

  constructor (@Inject(MAT_DIALOG_DATA) public data: { userEmailValue: string}, private userService: UserService, public dialogRef: MatDialogRef<SignupModalComponent>) {
    this.profileForm = new FormGroup({
      displayName: new FormControl ('', [ Validators.required ]),
      confirmDisplayName: new FormControl('')
    }, { validators: validateDisplayName });

    this.userEmailValue = data.userEmailValue;
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
      email: this.userEmailValue,
      role: "PARTICIPANT" as UserRole
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
