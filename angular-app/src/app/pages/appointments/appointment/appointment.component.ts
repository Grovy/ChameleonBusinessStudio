/**
 * This component is reponsible for rendering list view and calendar view
 * of the appointments. List view and calendar view are the child of this component
 * and pass appointments[] to the child components
 */



import { HttpClient } from '@angular/common/http';
import { Component, Input, SimpleChanges } from '@angular/core';
import { IAppointment } from 'src/app/models/interfaces/IAppointment';
import { IUser, UserRole } from 'src/app/models/interfaces/IUser';
import { MockAppointmentList } from 'src/app/models/mock/mock-appointments';
import { MockAdminUserList, MockParticipantList } from 'src/app/models/mock/mock-users';
import { AppointmentService } from 'src/app/services/appointment.service';
import { AuthenticationService } from 'src/app/services/AuthenticationService.service';
import { UserService } from 'src/app/services/UserService.service';
import { DateManager } from 'src/app/services/DateManager';

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

  userEmail: string = "";
  @Input()
  currentUser!: IUser;


    @Input() appointments: IAppointment[] =[];
    // @Input() role: UserRole = UserRole.PARTICIPANT;


    constructor(private http: HttpClient,private userService: UserService,private authService: AuthenticationService
              ,private appointmentService: AppointmentService,private datemng: DateManager){
         }

    ngOnInit(): void {


      this.authService.getPrincipal().subscribe(
        data => {
          this.userEmail = data.valueOf();
          this.userService.getUser(this.userEmail).subscribe(data => {
            this.currentUser = data as IUser;
            // this.role = this.currentUser.role as UserRole;
            this.appointmentService.getAllappointments(this.currentUser.role as UserRole).subscribe(data=>{
              this.appointments = [...data];
              this.appointments.map((data)=>{
                let startDate = data.startTime as number[];
                let endDate = data.endTime as number [];
                data.startTime = this.datemng.arrayToDate(startDate);
                data.endTime = this.datemng.arrayToDate(endDate);
                return data;
              });
          })
          }); // make sure to import UserService.service.ts in constructor
      });







    }

}


