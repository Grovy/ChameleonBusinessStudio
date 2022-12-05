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
import { AppointmentService } from 'src/app/services/AppointmentService.service';
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
  // currentUser: IUser = MockParticipantList[0] ;
  //currentUser: IUser = MockAdminUserList[0];
  currentUser!: IUser;
  reqCompleted: boolean = false;
    // @Input() appointments: IAppointment[] =  MockAppointmentList;
    @Input() appointments: IAppointment[] =[];
    // @Input() role: UserRole = UserRole.PARTICIPANT;


    constructor(private http: HttpClient,private userService: UserService,private authService: AuthenticationService
              ,private appointmentService: AppointmentService,private datemng: DateManager){
         }

    ngOnInit(): void {


      this.authService.getPrincipal().subscribe(
        data => {
          this.userEmail = data.valueOf();
          this.userService.getUser(this.userEmail).subscribe((data) => {
            this.currentUser = data as IUser;
            // this.role = this.currentUser.role as UserRole;
            if(this.isAdmin()){
              //If the User is Admin then fetch all the appointments
              this.appointmentService.getAllAppointments().subscribe({
                next:(data) =>{

                    this.appointments = [...data];

                    this.appointments.map((data)=>{
                      let startDate = data.startTime as number[];
                      let endDate = data.endTime as number [];
                      data.startTime = this.datemng.arrayToDate(startDate);
                      data.endTime = this.datemng.arrayToDate(endDate);
                      return data;
                    });
                    this.reqCompleted = true;

                },
                error:(err)=>{
                    this.reqCompleted = false;
                    
                }
              })
            } else{

              //  participants and TALENT: they are only allowed to view their appointments
              this.appointmentService.getMyAppointments().subscribe({
                next:(data)=>{
                 

                      this.appointments = [...data.content];
                      this.reqCompleted = true;
                      this.appointments.map((data)=>{
                        let startDate = data.startTime as number[];
                        let endDate = data.endTime as number [];
                        data.startTime = this.datemng.arrayToDate(startDate);
                        data.endTime = this.datemng.arrayToDate(endDate);
                        return data;
                      });

                },
                error:(err) =>{
                    this.reqCompleted = false;
                    
                }
              })
            }

          },

          ); // make sure to import UserService.service.ts in constructor
      });

    }


    /**
     *
     * @returns true if there is atleast one appointment
     */
    isValid(){
      return this.appointments.length > 0 ;
    }

    isAdmin(){
      if(this.currentUser) return (this.currentUser.role === 'ADMIN' || this.currentUser.role === UserRole.ADMIN)
      else return false;
    }

    dataFromMock(){

      this.appointments.map((data)=>{
                    let startDate = data.startTime as number[];
                    let endDate = data.endTime as number [];
                    data.startTime = this.datemng.arrayToDate(startDate);
                    data.endTime = this.datemng.arrayToDate(endDate);
                    return data;
                  });
      this.reqCompleted = true;
    }

}


