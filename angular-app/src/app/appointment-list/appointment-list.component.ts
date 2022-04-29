import {Component, Input, SimpleChanges} from '@angular/core';
import {Appointment} from '../appointment/model';

@Component({
    selector: 'appointment-list',
    templateUrl: './appointment-list.component.html',
    styleUrls: ['./appointment-list.component.css']
})
export class AppointmentListComponent {
    @Input() appointments: Appointment[];
    
    constructor(){
        this.appointments = [];
    }
    
    ngOnChanges(changes: SimpleChanges){
        //console.log("changes:");
        //console.log(changes);
        this.appointments = changes['appointments'].currentValue;
    }
}