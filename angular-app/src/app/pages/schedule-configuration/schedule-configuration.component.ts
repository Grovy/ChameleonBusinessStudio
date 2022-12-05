import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AvailableTimes } from 'src/app/models/mock/mock-availabile-times';
import { Component } from '@angular/core';
import { DaysOfWeek, IAvailability } from 'src/app/models/interfaces/IAvailability';
import { DateManager } from 'src/app/services/DateManager';
import { IEvent } from 'src/app/models/interfaces/IEvent';
import { IAppointment } from 'src/app/models/interfaces/IAppointment';
import { IRepeatingAppointment } from 'src/app/models/interfaces/IRepeatingAppointment';
import { ISchedule } from 'src/app/models/interfaces/ISchedule';
import { ScheduleService } from 'src/app/services/ScheduleService.service';
import { v4 as uuidv4 } from 'uuid';
import { IUser } from 'src/app/models/interfaces/IUser';
import { UserService } from 'src/app/services/UserService.service';
import { AuthenticationService } from 'src/app/services/AuthenticationService.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-schedule-configuration',
  templateUrl: './schedule-configuration.component.html',
  styleUrls: ['./schedule-configuration.component.css']
})
export class ScheduleConfigurationComponent {

  // Availability Form variables
  mydaysOfTheWeek: any[] = [];
  selectedDays: DaysOfWeek[] = [];
  selectedTimeFrame;
  availableTimes: AvailableTimes[] = AvailableTimes;
  availabilityForm: FormGroup;
  myAvailability: IAvailability = {
    title: "",
    hoursFrom: "",
    hoursTo: "",
    daysOfWeek: [],
  }

  // Event/Service Form variables
  selectedLocationValue = '';
  locationValues: string[] = ["In Person", "Phone", "Zoom", "Discord", "Google Hangouts", "Microsoft Teams"];
  timeFrameValues: number[] = [15, 30, 45, 60]; // Should only support increments of 15 mins
  eventForm: FormGroup;
  myEvent: IEvent = {
    title: "",
    duration: 0,
    location: "",
    description: "",
  };

  mySchedule: ISchedule = {
    name: "",
    isEnabled: true,
    appointments: []
  }

  newApptTitle = '';
  currentUser: IUser;
  isLinear = true;

  mydaysOfWeek = [
    { id: 1, select: false, name: "MONDAY", viewName: "Monday" },
    { id: 2, select: false, name: "TUESDAY", viewName: "Tuesday" },
    { id: 3, select: false, name: "WEDNESDAY", viewName: "Wednesday" },
    { id: 4, select: false, name: "THURSDAY", viewName: "Thursday" },
    { id: 5, select: false, name: "FRIDAY", viewName: "Friday" },
    { id: 6, select: false, name: "SATURDAY", viewName: "Saturday" },
    { id: 7, select: false, name: "SUNDAY", viewName: "Sunday" },
  ];

  firstFormGroup = this.formBuilder.group({
    firstCtrl: [''],
  });
  secondFormGroup = this.formBuilder.group({
    secondCtrl: [''],
  });

  constructor(private formBuilder: FormBuilder, private scheduleService: ScheduleService, private dateManager: DateManager,
    private userService: UserService, private authenticationService: AuthenticationService, private snackBar: MatSnackBar) {
    this.availabilityForm = this.formBuilder.group({
      title: ['', Validators.required],
      hoursFrom: ['', Validators.required],
      hoursTo: ['', Validators.required],
      daysOfWeek: [''],
    });

    this.eventForm = this.formBuilder.group({
      title: ['', Validators.required],
      duration: ['', Validators.required],
      location: ['', Validators.required],
      locationDetails: ['', Validators.required],
      description: ['', Validators.required],
    });

    this.getCurrentUser();
  }

  public onChecked($event): void {
    const id = $event.target.value;
    const isChecked = $event.target.checked;

    this.mydaysOfWeek = this.mydaysOfWeek.map( d => {
      if(d.id == id) {
        d.select = isChecked;
        return d;
      } else {
        return d;
      }
    });

    this.onSaveDays();
  }

  private onSaveDays(): void {
    let daysArray: DaysOfWeek[] = [];
    this.mydaysOfWeek.map( d => {
      if(d.select === true) {
        daysArray.push(d.name as DaysOfWeek);
        this.selectedDays = daysArray;
        return d;
      } else {
        return d;
      }
    });
  }

  private getCurrentUser(): void {
    this.authenticationService.getPrincipal().subscribe(
      data => {
        this.userService.getUser(data).subscribe(data => {this.currentUser = data});
    });
  }

  public saveAvailability(data): void {
    const newAvailability: IAvailability = {
      title: data.title,
      hoursFrom: data.hoursFrom,
      hoursTo: data.hoursTo,
      daysOfWeek: this.selectedDays
    }

    this.myAvailability = newAvailability;
  }

  openSnackBar(message, action?, config?) {
    this.snackBar.open(message, action, config);
  }

  public saveEvent(data): void {
    const newEvent: IEvent = {
      title: data.title,
      duration: data.duration,
      location: data.location,
      locationDetails: data.locationDetails,
      description: data.description,
    }

    this.myEvent = newEvent;

    this.constructSchedule();

    // Update with reaction elements later
    this.scheduleService.createSchedule(this.mySchedule).subscribe(
      data => { 
        if(data.status.toString().startsWith('2')) { // 2xx status codes are success
          this.openSnackBar("Schedule successfully created!", "Dismiss");
        } else {
          this.openSnackBar("An error occured when saving your schedule.", "Dismiss")
        }
        console.log(data); 
      }
    );

  }

  private constructSchedule(): void {
    let listOfAppts: IRepeatingAppointment[] = [];


    let dayIndex = this.dateManager.getDayIndex(this.selectedDays[0]);
    let startDate = this.dateManager.nextDay(dayIndex).toISOString().split("T")[0];
    let startingTime = this.dateManager.toTime(this.myAvailability.hoursFrom);

    let durationToTime = this.dateManager.durationToTime(this.myEvent.duration);
    let endingTime = this.dateManager.addTime(startingTime, durationToTime);
    let finalTime  = this.dateManager.toTime(this.myAvailability.hoursTo);

    const theAppointment: IAppointment = {
      _id: uuidv4(),
      title: this.myEvent.title,
      startTime: startDate + "T" + startingTime,
      endTime: startDate + "T" + endingTime,
      location: this.myEvent.location + " " + this.myEvent.locationDetails,
      description: this.myEvent.description ? this.myEvent.description : "",
      cancelled: false,
      totalSlots: 2,
      participants: [this.currentUser.email],
    };

    this.newApptTitle = this.myEvent.title;

    const theRepeatingAppointment: IRepeatingAppointment = {
      id: uuidv4(),
      isEnabled: true,
      repeatsOn: this.selectedDays,
      appointment: theAppointment,
    };

    listOfAppts.push(theRepeatingAppointment);

    while( (endingTime != finalTime) && (this.dateManager.addTime(endingTime, durationToTime) < finalTime) ) {
      startingTime = endingTime;
      endingTime = this.dateManager.addTime(startingTime, durationToTime);

      const theAppointment: IAppointment = {
        _id: uuidv4(),
        title: this.myEvent.title,
        startTime: startDate + "T" + startingTime,
        endTime: startDate + "T" + endingTime,
        location: this.myEvent.location + " " + this.myEvent.locationDetails,
        description: this.myEvent.description ? this.myEvent.description : "",
        cancelled: false,
        totalSlots: 2,
        participants: [this.currentUser.email],
      };

      const theRepeatingAppointment: IRepeatingAppointment = {
        id: uuidv4(),
        isEnabled: true,
        repeatsOn: this.selectedDays,
        appointment: theAppointment,
      };

      listOfAppts.push(theRepeatingAppointment);
    }


    const theSchedule: ISchedule = {
      id: uuidv4(),
      name: this.myAvailability.title,
      isEnabled: true,
      appointments: listOfAppts,
    }

    this.mySchedule = theSchedule;

  }

  public isLocationEqualTo(location: string): boolean {
    return location === this.selectedLocationValue;
  }
}
