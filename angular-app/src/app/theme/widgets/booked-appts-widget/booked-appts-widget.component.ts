import { Component, OnInit } from '@angular/core';
import { ISchedule } from 'src/app/models/interfaces/ISchedule';
import { IAppointment } from 'src/app/models/interfaces/IAppointment';
import { IRepeatingAppointment } from 'src/app/models/interfaces/IRepeatingAppointment';
import { IPage } from 'src/app/models/interfaces/IPage';
import { ScheduleService } from 'src/app/services/ScheduleService.service';
import { DateManager } from 'src/app/services/DateManager';

@Component({
  selector: 'app-booked-appts-widget',
  templateUrl: './booked-appts-widget.component.html',
  styleUrls: ['./booked-appts-widget.component.css']
})
export class BookedApptsWidgetComponent implements OnInit {

  myPage: IPage;
  mySchedules: ISchedule[];
  myRepeatingAppointments: IRepeatingAppointment[];
  myBookedAppointments: IAppointment[];
  displayedColumns: string[] = ['startTime', 'endTime', 'title'];

  constructor(private scheduleService: ScheduleService, private dateManager: DateManager) { }

  ngOnInit(): void {
    this.getSchedule();
  }

  getSchedule() {
    this.scheduleService.getAllSchedules().subscribe(
        data => {
        this.myPage = data;
        this.parsePage(this.myPage);
        }
    );
  }

  parsePage(page: IPage) {
    this.mySchedules = page.content;
    let repeatingAppts: IRepeatingAppointment[] = [];
    let appts: IAppointment[] = [];

    // Construct array of RepeatingAppointments
    this.mySchedules.map(
      data => {
        repeatingAppts = data.appointments;
        this.myRepeatingAppointments = repeatingAppts;
        return data;
      }
    );

    // Construct array of Appointments
    this.myRepeatingAppointments.map( 
      data => {
        // Filtering booked appointments. Those that have 2 participants
        if(data.appointment.participants[1] !== undefined) {
          // Converting the date format to be more user friendly
          let startdate: number[] = data.appointment.startTime as number[];
          let enddate: number[] = data.appointment.endTime as number[];
          data.appointment.startTime = this.dateManager.arrayToDate(startdate).toLocaleString();
          data.appointment.endTime = this.dateManager.arrayToDate(enddate).toLocaleString()

          appts.push(data.appointment);
          this.myBookedAppointments = appts;
          return data;
        }
        return data;
      },
  
    );

  }
}
