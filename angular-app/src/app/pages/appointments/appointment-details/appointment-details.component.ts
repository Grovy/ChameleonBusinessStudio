import { Component } from '@angular/core';
import { IAppointment } from 'src/app/models/interfaces/IAppointment';
import { AppointmentService } from 'src/app/services/AppointmentService.service';
import { DateManager } from 'src/app/services/DateManager';
import { AppointmentDateFilterPipe } from 'src/app/services/AppointmentDateFilterPipe';
import { AuthenticationService } from 'src/app/services/AuthenticationService.service';
import { UserService } from 'src/app/services/UserService.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-appointment-details',
  templateUrl: './appointment-details.component.html',
  styleUrls: ['./appointment-details.component.css']
})
export class AppointmentDetailsComponent {

  dateApptDictionary = new Map<string, any[]>();
  allAppointments;
  allDates;
  userEmail;
  currentUser;
  isRegisteredValue;
  isAuthenticatedValue;

  constructor(private appointmentService: AppointmentService, private dateManager: DateManager, 
    private authenticationService: AuthenticationService, private userService: UserService) {
    this.checkIfAuthenticated();
    this.checkIfRegisteredWithVendia();
    this.getAppointments();
  }

  getAppointments() {
    this.appointmentService.getAllAppointments().subscribe(
      data => {
        data.map(
          appt => {
            // Processing each appointment to have Date objects instead of number[]
            appt.startTime = this.dateManager.arrayToDate(appt.startTime as number[]);
            appt.endTime = this.dateManager.arrayToDate(appt.endTime as number[]);
          }
        ) 
        this.allAppointments = data;
        this.getDates(this.allAppointments); 
      }
    );
  }

  getDates(appts: IAppointment[]) {
    let myDates: string[] = [];
    appts.map(
      data => {
        // let startdate: number[] = data.startTime as number[];
        let tempStartDate: string = data.startTime.toLocaleString().split(",")[0];
        if( !(myDates.includes(tempStartDate)) ) {
          myDates.push(tempStartDate);
          return data;
        }
        return data;
      }
    )
    this.allDates = myDates.sort();
    this.getApptsPerDate(appts, this.allDates);
  }

  getApptsPerDate(appts: IAppointment[], dates: string[]) {
    let dictionary: Map<string, any[]> = new Map<string, any[]>();
    dates.map(
      data => {
        let apptArray: any[] = [];
        for(let i = 0; i < appts.length; i++) {
          if(appts[i].startTime.toLocaleString().split(",")[0] == data) {
            apptArray.push(appts[i]);
          }
        }
        dictionary.set(data, apptArray);
      }
    )
    this.dateApptDictionary = dictionary;
    console.log(this.dateApptDictionary);
  }

  getUserEmail() {
    this.authenticationService.getPrincipal().subscribe(
      data => { 
        this.userEmail = data.valueOf(); 
        this.userService.getUser(this.userEmail).subscribe(data => {this.currentUser = data});
    });  
  }

  checkIfAuthenticated() {
    this.authenticationService.isAuthenticated().subscribe(
      data => { 
        this.isAuthenticatedValue = data;
    });
  }

  checkIfRegisteredWithVendia() {
    this.authenticationService.isUserRegistered().subscribe(
      data => { 
        this.isRegisteredValue = data;
        this.getUserEmail();
    });
  }  

  // Funciton to book the currently signed in user
  bookUser(appt: IAppointment) {
    if(appt.participants.length < appt.totalSlots) {
      this.appointmentService.bookCurrentUser(appt._id as string).subscribe(
        data => { console.log(data) }
      );
      /* this.appointmentService.bookOtherUser(appt._id as string, this.userEmail).subscribe(
        data => { console.log(data) }
      ); */
    } else {
      console.log("Cannot book this appointment. Something went wrong.");
    }
  }

  unbookUser(appt: IAppointment) {
    if(!(appt.participants[1] === undefined)) {
      this.appointmentService.unbookOtherUser(appt._id as string, appt.participants[1] as string).subscribe(
        data => { 
          console.log(data); 
        }
      );
    } else {
      console.log("Cannot unbook this appointment. Something went wrong.");
    }
  }
}
