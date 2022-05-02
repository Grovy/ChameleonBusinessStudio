import { Component, OnInit, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'admin-gen-user',
  template:`
//   <p>User has been created</p>
  <ng-content></ng-content>
  `
})

export class AdminGenUserComponent implements OnInit {
  newName = '';
  newEmail = '';
  newPosition = '';

  constructor() {}

  onAddUser()
  {
//     this.userCreated.emit({name: this.newName,
//                            email: this.newEmail,
//                            position: this.newPosition});
  }

  ngOnInit()
  {
  }
}

