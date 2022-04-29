import {Component, Input} from '@angular/core';
import { Appointment } from './model';

/*
we'll need to change this component a bit once we allow listing unavailable
appointments.
*/

@Component({
    selector: 'appointment',
    templateUrl: './appointment.component.html',
    styleUrls: ['./appointment.component.css']
})
export class AppointmentComponent {
    @Input() appt?: Appointment;
    
    constructor(){
        
    }
    
    public formatTime(time: string): string {
        let dayStr = new Date(time).toLocaleString();
        //console.log(dayStr);
        return dayStr;
    }
}