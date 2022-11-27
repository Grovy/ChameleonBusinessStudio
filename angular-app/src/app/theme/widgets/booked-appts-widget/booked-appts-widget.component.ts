import { Component, OnInit } from '@angular/core';
import { IAppointment } from 'src/app/models/interfaces/IAppointment';
import { DateManager } from 'src/app/services/DateManager';
import { AuthenticationService } from 'src/app/services/AuthenticationService.service';
import { UserService } from 'src/app/services/UserService.service';
import { IUser } from 'src/app/models/interfaces/IUser';
import { AppointmentService } from 'src/app/services/AppointmentService.service';

@Component({
  selector: 'app-booked-appts-widget',
  templateUrl: './booked-appts-widget.component.html',
  styleUrls: ['./booked-appts-widget.component.css']
})
export class BookedApptsWidgetComponent implements OnInit {

  myBookedAppointments: IAppointment[];
  displayedColumns: string[] = ['startTime', 'endTime', 'title'];

  public isRegisteredValue: boolean;
  public isAuthenticatedValue: boolean;
  public shouldDisplayModal: boolean;
  public userEmail: string;
  public currentUser: IUser;

  constructor(private dateManager: DateManager, private authenticationService: AuthenticationService, 
    private userService: UserService, private appointmentService: AppointmentService) { }

  ngOnInit(): void {
    this.getUserEmail();
  }

  getBookedAppts(user: IUser) {
    let apptArray: IAppointment[] = [];
    if(user.appointments) {
      for(let i = 0; i < user.appointments?.length; i++) {
        this.appointmentService.getAppointmentById(user.appointments[i] as string).subscribe( 
          data => {
            if(data.participants.length === data.totalSlots) {
              let startdate: number[] = data.startTime as number[];
              let enddate: number[] = data.endTime as number[];
              data.startTime = this.dateManager.arrayToDate(startdate).toLocaleString();
              data.endTime = this.dateManager.arrayToDate(enddate).toLocaleString() 
              apptArray.push(data); 
            }
          }
        );
      }
    }
    
    this.myBookedAppointments = apptArray;
    
  }

  getUserEmail() {
    this.authenticationService.getPrincipal().subscribe(
      data => { 
        this.userEmail = data.valueOf(); 
        this.userService.getUser(this.userEmail).subscribe(
          data => {
            this.currentUser = data; 
            this.getBookedAppts(this.currentUser);
          }
        );
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
  
}
