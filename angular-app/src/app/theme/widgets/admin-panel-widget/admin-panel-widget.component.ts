import { Component, ViewChild, OnInit } from "@angular/core";
import { IUser, UserRole } from "src/app/models/interfaces/IUser";
import { IUserResponse } from "src/app/models/interfaces/IUserResponse";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { UserService } from "src/app/services/UserService.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { v4 as uuidv4 } from 'uuid';


@Component({
  selector: 'app-admin-panel-widget',
  templateUrl: './admin-panel-widget.component.html',
  styleUrls: ['./admin-panel-widget.component.css']
})
export class AdminPanelWidgetComponent implements OnInit {

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
      displayName: ['', Validators.required],
      email: ['', Validators.required],
      confirmEmail: ['', Validators.required],
      role: ['', Validators.required],
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
      _id: uuidv4(),
      displayName: data.displayName,
      email: data.email,
      role: data.role as UserRole,
    }

    this.userService.createUser(newUser).subscribe(
      data => {
        if(data.status.toString() == '200') {
          console.log(data);
          this.openSnackBar("Successfully updated user's role!", "Dismiss", {
            duration: 5000,
          });
        } else {
          console.log(data),
          this.openSnackBar("An error occured when updating user's role.", "Dismiss", {
            duration: 5000,
          });
        }
      }
    );
  }

  changeUserRole(data, user: IUser) {
    let newUser: IUser = {
      _id: user._id,
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
