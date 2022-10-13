import { IAppointment } from 'src/app/models/interfaces/IAppointment';
import { AfterViewInit, Component, Input, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
/*
we'll need to change this component a bit once we allow listing unavailable
appointments.
*/

@Component({
    selector: 'appointment-list',
    templateUrl: './appointment-list.component.html',
    styleUrls: ['./appointment-list.component.css']
})

export class AppointmentListComponent implements AfterViewInit{

    // needs to be nullable, as it cannot initialize in the constructor
    @Input() appointments?: IAppointment[];
    displayedColumns: string[] = ['position', 'date', 'title','startTime', 'endTime','totalSlots'];

    dataSource: MatTableDataSource<IAppointment>;


    @ViewChild(MatPaginator) paginator:MatPaginator;
    constructor(){
      this.dataSource = new MatTableDataSource(this.appointments);
    }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
  }


    public padTo2Digits(num: number) {
      return num.toString().padStart(2, '0');
    }

    public formatDate(date: Date) {
      console.log(date);
      return (
        [
          date.getFullYear(),
          this.padTo2Digits(date.getMonth() + 1),
          this.padTo2Digits(date.getDate()),
        ].join('-')
      );
    }

    public formatTime(date: Date){
      return (
        [
          this.padTo2Digits(date.getHours()),
          this.padTo2Digits(date.getMinutes()),
          this.padTo2Digits(date.getSeconds()),
        ].join(':')
      );
    }
}
