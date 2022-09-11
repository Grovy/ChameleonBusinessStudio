import { Component } from '@angular/core';
import { MockAppointmentList, MockSelectedAppointment } from '../../../models/mock/mock-appointments';
import { IAppointment } from 'src/app/models/interfaces/IAppointment';


@Component({
  selector: 'app-appointment-details',
  templateUrl: './appointment-details.component.html',
  styleUrls: ['./appointment-details.component.css']
})
export class AppointmentDetailsComponent {
  displayedColumns: string[] = ['id', 'service', 'client-name', 'date', 'start-time', 'end-time'];
  dataSource = MockAppointmentList;
  selectedAppt = MockSelectedAppointment;

  constructor() { }

  setSelectedAppt(findID: number) {
    this.selectedAppt = MockAppointmentList.find( ({id}) => id === findID );
  }

  getSelectedAppt() {
    return this.selectedAppt;
  }

  

}
