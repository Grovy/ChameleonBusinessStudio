import { Component, OnInit } from '@angular/core';
import { ScheduleService } from 'src/app/services/ScheduleService.service';
import { IPage } from 'src/app/models/interfaces/IPage';
import { DateManager } from 'src/app/services/DateManager';
import { DomSanitizer } from "@angular/platform-browser";
import { IAppointment } from 'src/app/models/interfaces/IAppointment';
import { MatIconRegistry } from "@angular/material/icon";

@Component({
  selector: 'app-event-widget',
  templateUrl: './event-widget.component.html',
  styleUrls: ['./event-widget.component.css']
})
export class EventWidgetComponent implements OnInit {

  myPage: IPage;
  appointmentName;
  eventName;
  eventDuration;
  hasEvent = false;

  constructor(private scheduleService: ScheduleService, private dateManager: DateManager, private matIconRegistry: MatIconRegistry, private domSanitizer : DomSanitizer) {
    this.matIconRegistry.addSvgIcon(
      "eventIcon",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/images/eventIcon.svg")
    );
    
    this.getEventInformation();
  }

  ngOnInit(): void {
  }

  private getEventInformation() {
    this.scheduleService.getAllSchedules().subscribe(
      data => {
        this.myPage = data;
        this.parsePage(this.myPage);
        this.getEventDuration(this.myPage);
      }
    );
  }

  // This function is only supporting 1 schedule right now
  private parsePage(page: IPage) {
    this.eventName = page.content[0].name;
    if (this.eventName == '') {
      this.hasEvent = false;
    } else {
      this.hasEvent = true;
      this.appointmentName = page.content[0].appointments[0].appointment.title;
    }
  }

  private getEventDuration(page: IPage) {
    // Subtract end time from start time
    let timeA = page.content[0].appointments[0].appointment.endTime;
    let timeB = page.content[0].appointments[0].appointment.startTime;

    let timeAasISO = this.dateManager.arrayToISOString(timeA).split("T")[1];
    let timeBasISO = this.dateManager.arrayToISOString(timeB).split("T")[1];

    let subTime = this.dateManager.subtractTime(timeAasISO, timeBasISO);
    this.eventDuration = this.dateManager.timeToDuration(subTime);
    console.log(timeA);
    console.log(timeB);
    console.log(timeAasISO);
    console.log(timeBasISO);
    console.log("Duration: " + subTime);

  }

}