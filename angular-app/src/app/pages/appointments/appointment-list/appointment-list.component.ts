import { IAppointment } from 'src/app/models/interfaces/IAppointment';
import { AfterViewInit, ChangeDetectorRef, Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import format from 'date-fns/format';
import { IUser, UserRole } from 'src/app/models/interfaces/IUser';
import { ChangeDetectionStrategy } from '@angular/compiler';
/*
we'll need to change this component a bit once we allow listing unavailable
appointments.
*/

@Component({
    selector: 'appointment-list',
    templateUrl: './appointment-list.component.html',
    styleUrls: ['./appointment-list.component.css']
})

export class AppointmentListComponent implements OnInit, AfterViewInit{

    // needs to be nullable, as it cannot initialize in the constructor
    @Input() appointments: IAppointment[] =[];
    @Input() currentUser?: IUser;
    displayedColumns: string[] = ['position', 'date', 'title','startTime', 'endTime','totalSlots'];


    appLength: number  = 0;
    dataSource: MatTableDataSource<IAppointment>;

    @ViewChild(MatPaginator) paginator: MatPaginator;

    constructor(private cdt: ChangeDetectorRef){
      }

  ngAfterViewInit(): void {
    //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
    //Add 'implements AfterViewInit' to the class.
    this.dataSource.paginator = this.paginator;
    // this.appLength = this.appointments.length;
  }
  ngOnInit(): void {
    this.dataSource = new MatTableDataSource(this.appointments);
      if(this.currentUser?.role === UserRole.PARTICIPANT || this.currentUser?.role== 'PARTICIPANT'){

        const pos = this.displayedColumns.indexOf('totalSlots');
        if(pos!==-1){
            this.displayedColumns.splice(pos,1);
        }
      }


  }

    public padTo2Digits(num: number) {
      return num.toString().padStart(2, '0');
    }

    public formatDate(date: Date) {

      return (
        [
          date.getFullYear(),
          this.padTo2Digits(date.getMonth() + 1),
          this.padTo2Digits(date.getDate()),
        ].join('-')

      );
    }

    /**
     *
     * @param date : Date provided of appointments
     *
     * @returns : formated date in format 02:30 AM/PM
     */

    public formatTime(date: Date){


      var formatedDate = [format(date,"h"),format(date,"mm")].join(":");
      formatedDate= formatedDate+" "+(format(date,"aaa"));

      return formatedDate;
    }


    public formatSlots(num: number){
      if(num == null) return "N/A";
      else return num.toString();
    }


    public formatTitle(s: string){
      if(s.length > 25){
          return s.substring(0,25)+"...";
      } else{
        return s;
      }
    }

    isValid(){
      return this.appointments.length>0;
    }
    // public isAdmin(){
    //   return this.role == UserRole.ADMIN
    //                  || this.role == UserRole.ORGANIZER || this.role == UserRole.TALENT;
    // }
}

