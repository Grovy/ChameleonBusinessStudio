import { Component } from '@angular/core';
import { IAppointment } from 'src/app/models/interfaces/IAppointment';
import { AppointmentService } from 'src/app/services/AppointmentService.service';
import { DateManager } from 'src/app/services/DateManager';
import { AppointmentDateFilterPipe } from 'src/app/services/AppointmentDateFilterPipe';
import { AuthenticationService } from 'src/app/services/AuthenticationService.service';
import { UserService } from 'src/app/services/UserService.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IUser } from 'src/app/models/interfaces/IUser';
import { IUserResponse } from 'src/app/models/interfaces/IUserResponse';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-appointment-details',
  templateUrl: './appointment-details.component.html',
  styleUrls: ['./appointment-details.component.css']
})
export class AppointmentDetailsComponent {

  dateApptDictionary = new Map<string, any[]>();
  filterApptName;
  allAppointments;
  allDates;
  userEmail;
  currentUser;
  isRegisteredValue;
  isAuthenticatedValue;
  showBookUserForm = false;
  reqCompleted: boolean = false;

  userBookingForm: FormGroup;
  myUserResponse: IUserResponse = { users: [] };

  constructor(private appointmentService: AppointmentService, private dateManager: DateManager,
    private authenticationService: AuthenticationService, private userService: UserService, private snackBar: MatSnackBar,
    private formBuilder: FormBuilder, private activatedRoute: ActivatedRoute) {
    this.checkIfAuthenticated();
    this.checkIfRegisteredWithVendia();
    this.getAppointments();
    this.activatedRoute.queryParams.subscribe( data => { this.filterApptName = data['event'];} );
    this.userBookingForm = this.formBuilder.group({
      email: ['', Validators.required],
    });
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
        );
        this.allAppointments = data;
        // When we it recognizes there is a query param, we will apply the title filter
        if(this.filterApptName) {
          let newAppts: IAppointment[] = [];
          newAppts = this.findByApptTitle(this.allAppointments);
         
          this.allAppointments = newAppts;
        }
        this.getDates(this.allAppointments);
        this.reqCompleted = true;
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
  bookCurrentUser(appt: IAppointment) {
    if(appt.participants.length < appt.totalSlots) {
      this.appointmentService.bookCurrentUser(appt._id as string).subscribe(
        data => {
        
          if(data.status.toString() == '200') {
            this.openSnackBar("Appointment successfully booked!", "Dismiss", {
              duration: 5000,
            });
            appt.participants[1] = this.userEmail as string;
          } else {
            this.openSnackBar("An error occured when trying to book this appointment.", "Dismiss", {
              duration: 5000,
            });
          }
        }
      );
    } else {
      this.openSnackBar("Cannot book this appointment. Something went wrong.", "Dismiss", {
        duration: 5000,
      });
    }
  }

  bookOtherUser(data, appt: IAppointment) {
    this.appointmentService.bookOtherUser(appt._id as string, data.email).subscribe( 
      data => {
        
        if(data.status.toString() == '200') {
          this.openSnackBar("You have successfully booked " + data.email + " for this appointment!", "Dismiss", {
            duration: 5000,
          });
          appt.participants[1] = data.email as string;
        } else {
          this.openSnackBar("An error occured when trying to book user: " + data.email + ".", "Dismiss", {
            duration: 5000,
          });
        }
      } 
    );
  }

  unbookUser(appt: IAppointment) {
    if(!(appt.participants[1] === undefined)) {
      let email = appt.participants[1] as string;
      this.appointmentService.unbookOtherUser(appt._id as string, email).subscribe(
        data => {
          
          if(data.status.toString() == '200') {
            this.openSnackBar("Appointment successfully unbooked!", "Dismiss", {
              duration: 5000,
            });
            appt.participants[1] == email ? appt.participants.pop() : appt.participants.splice(appt.participants.indexOf(email), 1);
          } else {
            this.openSnackBar("An error occured when trying to unbook this appointment.", "Dismiss", {
              duration: 5000,
            });
          }
        }
      );
    } else {
      this.openSnackBar("Something went wrong! cannot unbook this appointment.","Dismiss",{
        duration: 5000,
      });
    }
  }

  openSnackBar(message, action?, config?) {
    this.snackBar.open(message, action, config);
  }

  showBookOtherUserForm() {
    this.showBookUserForm = true
    let listOfUsers: IUser[] = [];
    this.userService.getAllUsers().subscribe(
      data => { 
        this.myUserResponse = data;
        
      }
    );
  }

  findByApptTitle(appts: IAppointment[]) {
    let newAppts: IAppointment[] = [];
    if(this.filterApptName) {
      for(let i = 0; i < appts.length; i++) {
        if(appts[i].title == this.filterApptName) {
          newAppts.push(appts[i]);
        }
      }
    }
   
    return newAppts;
  }
}
