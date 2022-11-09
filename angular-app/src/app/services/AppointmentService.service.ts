import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";
import { catchError, Observable, tap, throwError } from 'rxjs';
import { IAppointment } from "../models/interfaces/IAppointment";

@Injectable()
export class AppointmentService {

    private readonly apiUrl = '/api/v1/appointments';

    constructor(private httpClient: HttpClient, private snackBar: MatSnackBar) { }

    public getAllAppointments(): Observable<any> {
        return this.httpClient.get(`${this.apiUrl}/`)
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    public getMyAppointments(): Observable<any> {
        return this.httpClient.get(`${this.apiUrl}/mine`)
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    public createAppointment(appointment: IAppointment) {
        return this.httpClient.post(`${this.apiUrl}/`, appointment)
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    public bookCurrentUser(apptId: string) {
        return this.httpClient.post(`${this.apiUrl}/book-me/${apptId}`, {})
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    public bookOtherUser(apptId: string, email: string) {
        return this.httpClient.post(`${this.apiUrl}/book-them/${apptId}?email=${email}`, {})
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    public unbookCurrentUser(apptId: string) {
        return this.httpClient.post(`${this.apiUrl}/unbook-me/${apptId}`, {})
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }
    
    public unbookOtherUser(apptId: string, email: string) {
        return this.httpClient.post(`${this.apiUrl}/unbook-them/${apptId}?email=${email}`, {})
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    private handleError(error: HttpErrorResponse): Observable<never> {
        console.log(error);
        return throwError(`An error occured - Error code: ${error.status}`);
    }

}