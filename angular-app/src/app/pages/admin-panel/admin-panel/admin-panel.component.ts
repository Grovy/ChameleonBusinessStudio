//Author: Ariel Camargo
import { Component } from "@angular/core";
import { IUser } from "src/app/models/interfaces/IUser";
import { IUserResponse } from "src/app/models/interfaces/IUserResponse";
import { FormBuilder, FormGroup } from "@angular/forms";
import { UserService } from "src/app/services/UserService.service";

@Component ({
  selector:'app-admin-panel',
  templateUrl:'./admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})

export class AdminPanelComponent {

  myUserResponse: IUserResponse = { users: [] };
  displayedColumns: string[] = ['displayName', 'email', 'role'];
  selectedProfile: IUser | undefined;
  profileForm: FormGroup;

  constructor(private fb: FormBuilder, private userService: UserService) {
    this.selectedProfile = this.myUserResponse.users ? this.myUserResponse.users[0] : undefined;
    this.profileForm = this.fb.group({
      displayName: [''],
      email: [''],
      confirmEmail: [''],
      role: [''],
    });
  }

  ngOnInit(): void {
    this.userService.getAllUsers().subscribe(
      (data) => {
        this.myUserResponse = data;
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
    const newProfile: IUser = {
      displayName: data?.displayName ? data.displayName : data.email,
      email: data.email,
      role: data.role,
    }
    console.log("Clicking save: " + newProfile.displayName);
    console.log("Clicking save: " + newProfile.email);
    console.log("Clicking save: " + newProfile.role);
  }

  onSave() {
    console.log("New employee: " + this.profileForm);
  }
}

