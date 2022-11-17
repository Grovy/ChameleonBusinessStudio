//Author: Ariel Camargo
import { Component } from "@angular/core";
import { IUser } from "src/app/models/interfaces/IUser";
import { FormBuilder, FormGroup } from "@angular/forms";
import { MockEmployeeList } from "src/app/models/mock/mock-employees";
import { v4 as uuidv4 } from 'uuid';

@Component ({
  selector:'app-admin-panel',
  templateUrl:'./admin-panel-test.component.html',
  styleUrls: ['./admin-panel-test.component.css']
})

export class AdminPanelTestComponent {

  employeeData = MockEmployeeList;
  displayedColumns: string[] = ['email', 'role'];
  selectedProfile: IUser | undefined;
  profileForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.selectedProfile = this.employeeData ? this.employeeData[0] : undefined;
    this.profileForm = this.fb.group({
      displayName: [''],
      email: [''],
      confirmEmail: [''],
      role: [''],
    })
    console.log(this.employeeData);
  }

  setSelectedProfile(searchEmail: string) {
    this.selectedProfile = MockEmployeeList.find( ({email}) => email === searchEmail);
  }

  getSelectedProfile() {
    return this.selectedProfile;
  }

  onClickSubmit(data) {
    const newProfile: IUser = {
      _id: uuidv4(),
      displayName: data?.displayName ? data.displayName : data.email,
      email: data.email,
      role: data.role,
    }

    this.employeeData = [newProfile, ...this.employeeData ];
  }

  onSave() {
    console.log("New employee: " + this.profileForm);
  }


}
  