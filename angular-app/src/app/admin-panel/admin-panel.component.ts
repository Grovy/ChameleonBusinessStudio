//Author: Ariel Camargo
import {Component, OnInit} from "@angular/core";

@Component ({
  selector:'app-admin-panel',
  templateUrl:'./admin-panel.component.html'
})

export class AdminPanelComponent implements OnInit
{
  userName: string = '';
  userCreatedStatus: string = '';
  selectedRole:'';
  names: string[] = [];
  userTotal: number = 0;
  adminTotal: 0;
  userTotalString: string = '';
  onCreateUser()
  {
    this.userCreatedStatus = this.userName + " was added as " + this.selectedRole;
  }

  onUpdateUserName(event: Event)
  {
    this.userName = (<HTMLInputElement>event.target).value;
    this.names.push((<HTMLInputElement>event.target).value);
  }
  ngOnInit()
  {
  }
}

