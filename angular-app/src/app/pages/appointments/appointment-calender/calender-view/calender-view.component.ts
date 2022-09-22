import { Component, OnInit,
  ChangeDetectorRef,Injectable,ViewEncapsulation,
  ChangeDetectionStrategy,ViewChild,TemplateRef } from '@angular/core';

import {CalendarEvent,CalendarEventTitleFormatter} from 'angular-calendar'
import { WeekViewHourSegment } from 'calendar-utils';
import { fromEvent } from 'rxjs';
import { finalize,takeUntil } from 'rxjs/operators';


import { startOfDay, endOfDay,endOfWeek,subDays,setHours,setMinutes,addDays,endOfMonth,isSameDay,isSameMonth,addHours, addMinutes } from 'date-fns';


// function floorToNearest(amount: number, precision: number) {
//   return Math.floor(amount / precision) * precision;
// }

// function ceilToNearest(amount: number, precision: number) {
//   return Math.ceil(amount / precision) * precision;
// }

@Injectable()
export class CustomEventTitleFormatter extends CalendarEventTitleFormatter {

  override weekTooltip(event: CalendarEvent, title: string) {

    if (!event.meta.tmpEvent) {
      return super.weekTooltip(event, title);
    }
    return " ";
  }

  override dayTooltip(event: CalendarEvent, title: string) {
    if (!event.meta.tmpEvent) {
      return super.dayTooltip(event, title);
    }
    return " ";
  }
}

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

  viewDate = new Date();
  events: CalendarEvent[] =[{
    start: setHours(setMinutes(new Date(), 20), 15),
    end: setHours(setMinutes(new Date(), 40), 17),

      title: `Hair cut `,
  }];
  dragToCreateActive = false;
  weekStartsOn: 0 =0;

  constructor(private cdr: ChangeDetectorRef) { }

  startDragToCreate(
    segment: WeekViewHourSegment,
    mouseDownEvent: MouseEvent,
    segmentElement: HTMLElement
   ) {
  //   const dragToSelectEvent: CalendarEvent = {
  //     id: this.events.length,
  //     title: 'New event',
  //     start: segment.date,
  //     meta: {
  //       tmpEvent: true,
  //     },
  //   };
  //   this.events = [...this.events, dragToSelectEvent];
  //   const segmentPosition = segmentElement.getBoundingClientRect();
  //   this.dragToCreateActive = true;
  //   const endOfView = endOfWeek(this.viewDate, {
  //     weekStartsOn: this.weekStartsOn,
  //   });

  //   fromEvent(document, 'mousemove')
  //     .pipe(
  //       finalize(() => {
  //         delete dragToSelectEvent.meta.tmpEvent;
  //         this.dragToCreateActive = false;
  //         this.refresh();
  //       }),
  //       takeUntil(fromEvent(document, 'mouseup'))
  //     )
  //     .subscribe((mouseMoveEvent:any) => {
  //       const minutesDiff = ceilToNearest(
  //         mouseMoveEvent.clientY - segmentPosition.top,
  //         30
  //       );

  //       const daysDiff =
  //         floorToNearest(
  //           mouseMoveEvent.clientX - segmentPosition.left,
  //           segmentPosition.width
  //         ) / segmentPosition.width;

  //       const newEnd = addDays(addMinutes(segment.date, minutesDiff), daysDiff);
  //       if (newEnd > segment.date && newEnd < endOfView) {
  //         dragToSelectEvent.end = newEnd;
  //       }
  //       this.refresh();
  //     });
  }

  private refresh() {
      this.events = [...this.events];
      this.cdr.detectChanges();
  }

  ngOnInit(): void {
  }

}
