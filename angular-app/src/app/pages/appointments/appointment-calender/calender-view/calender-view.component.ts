/**
 * This component is created with the help of Angular Calendar library
 *
 * https://github.com/mattlewis92/angular-calendar
 * THis component is used by appointment component
 *
 */



import { Component, OnInit,
  ChangeDetectorRef,Injectable,ViewEncapsulation,
  ChangeDetectionStrategy,ViewChild,TemplateRef, Input, ElementRef, SimpleChanges, OnChanges } from '@angular/core';

import {CalendarEvent,CalendarView,CalendarEventTitleFormatter} from 'angular-calendar'
import { WeekViewHourSegment } from 'calendar-utils';
import { fromEvent, Subject } from 'rxjs';
import { finalize,takeUntil } from 'rxjs/operators';


import { startOfDay,setHours,setMinutes,differenceInMinutes,startOfHour} from 'date-fns';
import { IAppointment } from 'src/app/models/interfaces/IAppointment';
import { DateManager } from 'src/app/services/DateManager';


@Component({
  selector: 'app-calender-view',
  changeDetection:ChangeDetectionStrategy.OnPush,
  templateUrl: './calender-view.component.html',
  styleUrls: ['./calender-view.component.css',],
  providers:[{
    provide: CalendarEventTitleFormatter,
    useClass:CalendarEventTitleFormatter,
  }

  ],
  encapsulation: ViewEncapsulation.None,
})
export class CalenderViewComponent implements OnInit,OnChanges {
  @Input() appointments: IAppointment[] =[];
  @ViewChild('scrollContainer') scrollContainer: ElementRef<HTMLElement>;
  view: CalendarView = CalendarView.Week;
  viewDate = new Date();
  events: CalendarEvent[] =[{
    start: setHours(setMinutes(new Date(), 20), 15),
    end: setHours(setMinutes(new Date(), 40), 17),
    title: `Hair cut `,
  }];



  constructor(private cdr: ChangeDetectorRef) {

   }


  ngOnChanges(changes: SimpleChanges): void {
    //if the appointment changes
    if(changes['appointments'].currentValue!=changes['appointments'].previousValue){
      //add it to the calender view
      // Notes:
      //1. might need to add only appointments that have changed
      this.appointments?.forEach((data)=>{

        let eve: CalendarEvent = {
          title:data.title,
          start:<Date> data.startTime,
          end :<Date> data.endTime,

        };
        this.events.push(eve);
    });
    }
    console.log("From ngOnChanges");
    console.log(this.events);
    this.cdr.detectChanges();

  }

  //  private refresh() {
  //     this.events = [...this.events];
  //     this.cdr.detectChanges();
  // }
  ngOnInit(): void {



  }


}
