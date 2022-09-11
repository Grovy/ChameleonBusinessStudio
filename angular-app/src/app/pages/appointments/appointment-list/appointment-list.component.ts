import { Component, Input, SimpleChanges } from '@angular/core';
import { IAppointment } from 'src/app/models/interfaces/IAppointment';

/*
This component is currently responsible for rendering a list of appointments. As
our design evolves, we may need to push more responsibilities into this
component.
*/

@Component({
    selector: 'appointment-list',
    templateUrl: './appointment-list.component.html',
    styleUrls: ['./appointment-list.component.css']
})
export class AppointmentListComponent {
    @Input() appointments: IAppointment[];

    constructor(){
        this.appointments = [];
    }

    ngOnChanges(changes: SimpleChanges){
        console.log(changes);
        this.appointments = changes['appointments'].currentValue;
    }
}
