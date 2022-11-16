import { Component, ViewChild } from "@angular/core";
import { IUser, UserRole } from "src/app/models/interfaces/IUser";
import { IUserResponse } from "src/app/models/interfaces/IUserResponse";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { UserService } from "src/app/services/UserService.service";
import { MatSnackBar } from "@angular/material/snack-bar";

@Component ({
  selector:'app-admin-panel',
  templateUrl:'./admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})

export class AdminPanelComponent {
  
  myUserResponse: IUserResponse = { users: [] };
  displayedColumns: string[] = ['displayName', 'email', 'role'];
  roleSelection: string[] = ['PARTICIPANT', 'ADMIN', 'TALENT', 'ORGANIZER'];
  selectedProfile: IUser | undefined;
  profileForm: FormGroup;
  changeRoleForm: FormGroup;
  reqCompleted = false;

  constructor(private fb: FormBuilder, private userService: UserService, private snackBar: MatSnackBar) {
    this.selectedProfile = this.myUserResponse.users ? this.myUserResponse.users[0] : undefined;
    this.profileForm = this.fb.group({
      displayName: [''],
      email: [''],
      confirmEmail: [''],
      role: [''],
    });
    this.changeRoleForm = this.fb.group({
      newRole: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Ensures we are retrieving users from Vendia when this page loads.
    this.userService.getAllUsers().subscribe(
      (data) => {
        this.myUserResponse = data;
        this.reqCompleted = true;
      }
    );
  }

  setSelectedProfile(searchEmail: string) {
    this.selectedProfile = this.myUserResponse.users.find( ({email}) => email === searchEmail);
  }

  getSelectedProfile() {
    return this.selectedProfile;
  }

  onClickSubmit(data) {
    const newUser: IUser = {
      displayName: data.displayName,
      email: data.email,
      role: data.role as UserRole,
    }

    this.userService.createUser(newUser).subscribe(
      data => console.log(data)
    );
  }

  changeUserRole(data, user: IUser) {
    let newUser: IUser = {
      id: user.id,
      displayName: user.displayName,
      email: user.email,
      role: data.newRole,
      phoneNumber: user.phoneNumber,
      appointments: user.appointments
    };

    this.userService.updateUser(newUser).subscribe(
      data => {
        if(data.status.toString() == '200'){
          this.openSnackBar("Successfully updated user's role!", "Dismiss", {
            duration: 5000,
          });
        } else {
          this.openSnackBar("An error occured updating user's role", "Dismiss", {
            duration: 5000,
          });
        }
      }
    );
  }

  openSnackBar(message, action?, config?) {
    this.snackBar.open(message, action, config);
  }

  onDiscard() {
    this.profileForm.reset();
  }

  onSave() {
    console.log("New employee: " + this.profileForm);
  }
}

