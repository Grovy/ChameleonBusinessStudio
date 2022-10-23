import { FormBuilder, FormGroup } from '@angular/forms';
import { AvailableTimes } from 'src/app/models/mock/mock-availabile-times';
import { Component } from '@angular/core';
import { DaysOfWeek, IAvailability } from 'src/app/models/interfaces/IAvailability';
import { IEvent } from 'src/app/models/interfaces/IEvent';
import { IAppointment } from 'src/app/models/interfaces/IAppointment';
import { IRepeatingAppointment } from 'src/app/models/interfaces/IRepeatingAppointment';
import { ISchedule } from 'src/app/models/interfaces/ISchedule';


@Component({
  selector: 'app-schedule-configuration',
  templateUrl: './schedule-configuration.component.html',
  styleUrls: ['./schedule-configuration.component.css']
})
export class ScheduleConfigurationComponent {
  availableTimes: AvailableTimes[] = AvailableTimes;
  selectedLocationValue = '';
  selectedTimeFrame: number;
  description: string;
  locationValues: string[] = ["In Person", "Phone", "Zoom", "Discord", "Google Hangouts", "Microsoft Teams"];
  timeFrameValues: number[] = [15, 30, 45, 60]; // Should only support increments of 15 mins
  isLinear = false;
  availabilityFormGroup: FormGroup;
  availabilityForm: FormGroup;
  eventFormGroup: FormGroup;
  eventForm: FormGroup;
  myEvent: IEvent = {
    title: "",
    duration: "",
    location: "",
    description: "",
  };

  myAvailability: IAvailability = {
    title: "",
    hoursFrom: "",
    hoursTo: "",
    daysOfWeek: [],
  }

  mySchedule: ISchedule = {
    title: "",
    isEnabled: true,
    appointments: []
  }
  
  daysOfTheWeek = this.formBuilder.group({
    monday: false,
    tuesday: false,
    wednesday: false,
    thursday: false,
    friday: false,
    saturday: false,
    sunday: false,
  })

  constructor(private formBuilder: FormBuilder) {
    this.availabilityForm = this.formBuilder.group({
      title: [''],
      hoursFrom: [''],
      hoursTo: [''],
      daysOfWeek: [''],
    });

    this.eventForm = this.formBuilder.group({
      title: [''],
      duration: [''],
      location: [''],
      locationDetails: [''],
      description: [''],
    });

  }

  public saveAvailability(data): void {
    const newAvailability: IAvailability = {
      title: data.title,
      hoursFrom: data.hoursFrom,
      hoursTo: data.hoursTo,
      daysOfWeek: data.daysOfTheWeek,
    }

    this.myAvailability = newAvailability;

    console.log("Generated a new availability");
    console.log(
      "Title: " + newAvailability.title + " " + 
      "Hours From: " + newAvailability.hoursFrom + " " + 
      "Hours To: " + newAvailability.hoursTo + " " +
      "Days of the Week: " + newAvailability.daysOfWeek
    );
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

    console.log("Generated a new event/service");
    console.log(
      "Title: " + newEvent.title + " " + 
      "Duration: " + newEvent.duration + " " + " minutes " + 
      "Location: " + newEvent.location + " " + newEvent.locationDetails + " " + 
      "Description: " + newEvent.description 
    );

    this.constructSchedule();

    console.log("Schedule Constructed with the following properties: ");
    console.log(this.mySchedule.title + " " + this.mySchedule.appointments);
    console.log(this.mySchedule.appointments[0].appointment.title);

  }

  public constructSchedule(): void {
    const theAppointment: IAppointment = {
      startTime: "2022-10-25T09:00:00",
      endTime: "2022-10-25T09:30:00",
      title: this.myEvent.title,
      location: this.myEvent.location + " " + this.myEvent.locationDetails,
      description: this.myEvent.description ? this.myEvent.description : "",
      registeredUsers: [],
    };

    const theRepeatingAppointment: IRepeatingAppointment = {
      isEnabled: true,
      repeatsOn: ["TUESDAY" as DaysOfWeek, "THURSDAY" as DaysOfWeek],
      appointment: theAppointment,
    }

    const theSchedule: ISchedule = {
      title: this.myAvailability.title,
      isEnabled: true,
      appointments: [theRepeatingAppointment],
    }

    this.mySchedule = theSchedule;

  }

  public isLocationEqualTo(location: string): boolean {
    return location === this.selectedLocationValue;
  }
}