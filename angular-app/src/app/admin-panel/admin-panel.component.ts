//Author: Ariel Camargo
import {Component, OnInit} from "@angular/core";

@Component ({
  selector:'app-admin-panel',
  templateUrl:'./admin-panel.component.html'
})

export class AdminPanelComponent implements OnInit
{
  selectedRole: string = '';
    stringConfirmation:string='';
    // selectRole: boolean = false;
    private userCreationStatus: string='';
    userName = '';
    outputString = '';
    email = '';
    names = [];
    roles = [];
    numbers = [];
    message = [];
    emails = [];
    // Plan is to have the individual users keep their own data
    newUserName = '';
    newEmail = '';
    printString = '';
    numberSelected = '';
    namedRoles = ['Admin', 'User'];
    roleToAdd = '';

    onCreateUser()
      {

        if(this.userName != '' && this.selectedRole != '' && this.email != '')
        {
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

      addRole()
      {
        if(this.roleToAdd != '')
        {
          for(let i = 0; i < this.namedRoles.length; i++)
          {
            if(this.namedRoles[i] == this.roleToAdd)
            {
              i = this.namedRoles.length;
            }
            if(i == this.namedRoles.length - 1)
            {
              this.namedRoles.push(this.roleToAdd);
            }
          }
        }
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

      }
}

