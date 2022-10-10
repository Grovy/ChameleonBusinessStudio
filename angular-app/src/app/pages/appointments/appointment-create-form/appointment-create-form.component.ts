import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import {
    AbstractControl,
    FormBuilder,
    ValidationErrors,
    ValidatorFn,
    Validators
} from '@angular/forms';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { IAppointment } from '../../../models/interfaces/IAppointment';

/*
    reactive form

    https://angular.io/guide/reactive-forms
    https://angular.io/guide/form-validation#reactive-component-class
*/

@Component({
    selector: 'app-appointment-create-form',
    templateUrl: './appointment-create-form.component.html',
    styleUrls: ['./appointment-create-form.component.css']
})
export class AppointmentCreateFormComponent {
    appointmentForm = this.fb.group({

        title: ['', Validators.compose([
            Validators.required,
            Validators.minLength(1)
        ])],
        location: ['online', Validators.compose([
            Validators.required,
            Validators.minLength(1)
        ])],
        description: ['', Validators.compose([
          Validators.maxLength(250)
        ])],
        totalSlots: [1, Validators.compose([
            Validators.required,
            Validators.min(1)
        ])],
        startDate: [new Date(), this.startMustBeAfterToday()],
        startTime: ["12:00", this.startMustBeAfterToday()],
        endDate: [new Date(), this.endMustBeAfterStart()],
        endTime: ["12:00", this.endMustBeAfterStart()]
        // todo tags
    });

    isSubmitting = false;
    message = "";


    constructor(private fb: FormBuilder, private http: HttpClient) {

    }


    startMustBeAfterToday(): ValidatorFn {
        return (control: AbstractControl): ValidationErrors | null => {
            if(!this.appointmentForm){
                return null; // validator is running as object is instantiated
            }

            const today = new Date();
            const start = this.makeDateTime("start");

            const isValid = today < start;

            return (isValid) ? null : {invalidDate: {value: start}};
        }
    }

    endMustBeAfterStart(): ValidatorFn {
        return (control: AbstractControl): ValidationErrors | null => {
            if(!this.appointmentForm){
                return null; // validator is running as object is instantiated
            }

            const start = this.makeDateTime("start");
            const end = this.makeDateTime("end");
            const isValid = start < end;
            return (isValid) ? null : {invalidDate: {value: end}};
        }
    }

    // prefix is either "start" or "end"
    makeDateTime(prefix: string): Date {
        if(!this.appointmentForm){
            return new Date();
        }

        const date = this.appointmentForm.get(prefix + "Date")?.value;
        const time = this.appointmentForm.get(prefix + "Time")?.value;
        console.log(`date is ${date} and time is ${time}`);

        const iso8601Time = `${date}T${time}`;
        // console.log(iso8601Time);
        return new Date(iso8601Time);
    }

    submit(){
        this.isSubmitting = true;
        this.message = "creating your appointment...";

        // property names must match those used by the backend
        // isn't working. Form type wrong?
        // changed to RequestBody, should fix, need to test
        const dto = {
            startTime: this.makeDateTime("start"),
            endTime: this.makeDateTime("end"),
            title: this.appointmentForm.get("title")?.value,
            location: this.appointmentForm.get("location")?.value,
            description: this.appointmentForm.get("description")?.value,
            totalSlots: this.appointmentForm.get("totalSlots")?.value,
            tags: []
        };

        //console.log(dto);
        setTimeout(()=>{
            this.http.post<IAppointment>("/api/v1/appointments", dto)
            .pipe(catchError(this.handleError))
            .subscribe(response => {
                //console.log(response);
                this.isSubmitting = false;
                this.message = "Created successfully!";
            });
        }, 2000); // waits 2 seconds before posting so the user can see it work

    }

    private handleError(error: HttpErrorResponse): Observable<any>{
        console.error(error);
        this.isSubmitting = false;
        this.message = "Something went wrong!!";
        return throwError(() => error);
    }
}
