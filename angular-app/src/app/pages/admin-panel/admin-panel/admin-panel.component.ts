//Author: Ariel Camargo
import { Component } from "@angular/core";
import { IUser } from "src/app/models/interfaces/IUser";
import { FormBuilder, FormGroup } from "@angular/forms";
import { MockEmployeeList } from "src/app/models/mock/mock-employees";

@Component ({
  selector:'app-admin-panel',
  templateUrl:'./admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})

export class AdminPanelComponent {

  // New stuff

  employeeData = MockEmployeeList;
  displayedColumns: string[] = ['email', 'role'];
  selectedProfile: IUser | undefined;
  profileForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.selectedProfile = this.employeeData ? this.employeeData[0] : undefined;
    this.profileForm = this.fb.group({
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

  createNewUser() {
    
  }

  onClickSubmit(data) {
    const newProfile: IUser = {
      displayName: data?.displayName ? data.displayName : data.email,
      email: data.email,
      role: data.role,
    }
    console.log("Clicking save: " + newProfile.email);
    console.log("Clicking save: " + newProfile.role);
    console.log("Clicking save: " + newProfile.displayName);

    
    this.employeeData = [newProfile, ...this.employeeData ];
    console.log(newProfile.email + " was added.");
    console.log(this.employeeData);
  }

  onSave() {
    console.log("New employee: " + this.profileForm);
  }



  



 /*  onCreateUser() {

    if(this.userName != '' && this.selectedRole != '' && this.email != '') {
      // this.selectRole = true;
      this.names.push(this.userName);
      this.roles.push(this.selectedRole);
      this.numbers.push(this.names.length);
      this.emails.push(this.email);
      this.outputString = this.userName + " was added as " + this.selectedRole;
      this.message.push(this.outputString);
      //this.clearField();
    }
  }

      ngOnInit()
      {
      }

      clearField() {
        this.stringConfirmation = '';
        this.userName = '';
        this.email = '';
      }

      //Example for grabbing text and live putting it somewhere
      updateUsername(event: any)
      {
        this.userName = (<HTMLInputElement>event.target).value;
      }

      updateEmail(event: any)
      {
        this.email = (<HTMLInputElement>event.target).value;
      }

      emailGrab(event: any)
      {
        this.updateEmail(event);

      }

      textGrab(event: any)
      {
        this.updateUsername(event);
        //this.actualStringOutputTest();

      }

      actualStringOutputTest()
      {
        if((<HTMLInputElement>event.target).value != '')
        {
          this.outputString = this.userName + " was added as " + this.selectedRole;
        }
        else
        {
          this.outputString = '';
        }

      }

      deleteTest(i: number) {
        this.names.splice(i, 1);
        this.roles.splice(i, 1);
        this.numbers.pop();
        this.message.splice(i, 1);
        this.emails.splice(i, 1);
      }

      displayData(i: number)
      {
        this.printString = this.names[i] + "  " + this.roles[i] + " " + this.emails[i];
      }

      onShow()
      {
         this.displayData(this.parseNumber(this.numberSelected));
      }

      parseNumber(test: string)
      {
        return Number(test);
      }


      roleGrab($event: Event)
      {
        this.roleToAdd = (<HTMLInputElement>event.target).value;
      }

      removeRole()
      {
        if(this.namedRoles.length > 1)
        {
          var index  = this.namedRoles.indexOf(this.selectedRole);
          if(index != -1)
          {
            this.namedRoles.splice(index, 1);
          }
          this.selectedRole = '';
        }

      } */
}

