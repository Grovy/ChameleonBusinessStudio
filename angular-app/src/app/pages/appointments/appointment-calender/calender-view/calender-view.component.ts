/**
 * This component is created with the help of Angular Calendar library
 *
 * https://github.com/mattlewis92/angular-calendar
 * THis component is used by appointment component
 *
 */



import { Component, OnInit,
  ChangeDetectorRef,Injectable,ViewEncapsulation,
  ChangeDetectionStrategy,ViewChild,TemplateRef, Input, ElementRef } from '@angular/core';

import {CalendarEvent,CalendarView,CalendarEventTitleFormatter} from 'angular-calendar'
import { WeekViewHourSegment } from 'calendar-utils';
import { fromEvent } from 'rxjs';
import { finalize,takeUntil } from 'rxjs/operators';


import { startOfDay,setHours,setMinutes,differenceInMinutes,startOfHour} from 'date-fns';
import { IAppointment } from 'src/app/models/interfaces/IAppointment';


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
export class CalenderViewComponent implements OnInit {
  @Input() appointments?: IAppointment[];
  @ViewChild('scrollContainer') scrollContainer: ElementRef<HTMLElement>;
  view: CalendarView = CalendarView.Week;
  viewDate = new Date();
  events: CalendarEvent[] =[{
    start: setHours(setMinutes(new Date(), 20), 15),
    end: setHours(setMinutes(new Date(), 40), 17),

      title: `Hair cut `,
  }];
  dragToCreateActive = false;
  weekStartsOn: 0 =0;

  constructor(private cdr: ChangeDetectorRef) {

   }

   private refresh() {
      this.events = [...this.events];
      this.cdr.detectChanges();
  }
  ngAfterViewInit(){
    this.scrollToCurrentView();

  }
  viewChanged(){
    this.cdr.detectChanges();
    this.scrollToCurrentView();
  }
  private scrollToCurrentView() {
    if (this.view === CalendarView.Week || CalendarView.Day) {
      // each hour is 60px high, so to get the pixels to scroll it's just the amount of minutes since midnight
      const minutesSinceStartOfDay = differenceInMinutes(
        startOfHour(new Date()),
        startOfDay(new Date())
      );
      const headerHeight = this.view === CalendarView.Week ? 60 : 0;
      this.scrollContainer.nativeElement.scrollTop =
        minutesSinceStartOfDay + headerHeight;
    }
  }

  ngOnInit(): void {
    if(this.appointments!= undefined){
    this.appointments?.forEach((data)=>{

      this.events.push({
        title:data.title,
        start:data.startTime,
        end:data.endTime,

      })
    })}

  }

}
