import { IAppointment } from 'src/app/models/interfaces/IAppointment';
import { AfterViewInit, Component, Input, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
/*
we'll need to change this component a bit once we allow listing unavailable
appointments.
*/

@Component({
    selector: 'appointment',
    templateUrl: './appointment.component.html',
    styleUrls: ['./appointment.component.css']
})

export class AppointmentComponent implements AfterViewInit{

    // needs to be nullable, as it cannot initialize in the constructor
    @Input() appts?: IAppointment[];
    displayedColumns: string[] = ['position', 'date', 'title','startTime', 'endTime','totalSlots'];

    dataSource: MatTableDataSource<IAppointment>;


    @ViewChild(MatPaginator) paginator:MatPaginator;
    constructor(){
      this.dataSource = new MatTableDataSource(this.appts);
    }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }

    public formatTime(time: string): string {
        let dayStr = new Date(time).toLocaleString();
        return dayStr;
    }
}
