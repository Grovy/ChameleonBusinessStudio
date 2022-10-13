import { Component, Input, SimpleChanges } from '@angular/core';
import { IAppointment } from 'src/app/models/interfaces/IAppointment';

/*
This component is currently responsible for rendering a list of appointments. As
our design evolves, we may need to push more responsibilities into this
component.
*/

@Component({
    selector: 'appointment',
    templateUrl: './appointment.component.html',
    styleUrls: ['./appointment.component.css']
})
export class AppointmentComponent {
    @Input() appointments: IAppointment[];

    temp:IAppointment ={
      id: 123,
      date: "masdf",
      startTime: '',
      endTime: '',
      title: 'Hair cut',
      location: 'jasdfjsd',
      description: 'sdfasdf',
      registeredUsers: []
    };
    constructor(){
        this.appointments = [this.temp];
    }

    ngOnChanges(changes: SimpleChanges){
        console.log(changes);
        this.appointments = changes['appointments'].currentValue;
    }
}
