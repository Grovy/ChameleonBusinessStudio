import { Component, OnInit, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'admin-gen-user',
  template:`
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

