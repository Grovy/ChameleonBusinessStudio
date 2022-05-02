import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Appointment, AppointmentPage} from '../appointment/model';

/*
The booking page is currently responsible for rendering a list of available
appointments and presenting options to page & sort them. As we design other 
types of appointment lists (such as a user's booked-appointments list and the 
scheduling page for admins / organizers) we'll probably need to push some of 
this component's functionality into the appointment-list component.
*/

@Component({
    selector: 'booking-page',
    templateUrl: './booking-page.component.html',
    styleUrls: ['./booking-page.component.css']
})
export class BookingPageComponent implements OnInit {
    days: number = 7;
    size: number = 5;
    page: number = 0;
    sortAttr: string = 'startTime';
    sortDir: string = 'asc';
    appointments: Appointment[];
    hasNextPage: boolean = false;
    hasPrevPage: boolean = false;
    
    constructor(private http: HttpClient){
        this.appointments = [];
    }
    
    ngOnInit(): void {
        this.update();
    }
    
    public prevPage(): void {
        if(this.page > 0){   
            --this.page;
            this.update();
        }
    }
    
    public nextPage(): void {
        if(this.hasNextPage){
            ++this.page;
            this.update();
        }
    }
    
    public resetPageThenUpdate(): void {
        this.page = 0; // go back to first page on changing page size
        this.update();
    }
    
    public update(): void {
        const params = [
            ["days", this.days],
            ["size", this.size],
            ["page", this.page],
            ["sort", `${this.sortAttr},${this.sortDir}`]
        ].map((pair)=>pair.join('=')).join('&');
        const url = `/api/v1/appointments/available?${params}`;
        this.http.get<AppointmentPage>(url).subscribe((data)=>{
            this.appointments = data._embedded.appointmentEntities;
            this.hasNextPage = 'next' in data._links;
            this.hasPrevPage = 'prev' in data._links;
        });
    }
}