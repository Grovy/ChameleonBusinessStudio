import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError, map, delay } from 'rxjs';
import { IAppointment } from "../models/interfaces/IAppointment";

@Injectable()
export class AppointmentService {

    private readonly apiUrl = '/api/v1/appointments';

    constructor(private httpClient: HttpClient) { }

  /**
   *
   * @returns  All the appointments, Used by Admins
   */
    public getAllAppointments(): Observable<any> {
        return this.httpClient.get(`${this.apiUrl}/`)
            .pipe(
                tap(console.log),
                delay(4000),
                catchError(this.handleError)
            );
    }
    /**
     *
     * @returns the appointments of the current User
     */
    public getMyAppointments(): Observable<any> {
        return this.httpClient.get(`${this.apiUrl}/mine`)
            .pipe(
                tap(console.log),
                delay(4000),
                catchError(this.handleError)
            );
    }
    /**
     * Used to generate appointments
     * @param appointment
     * @returns
     */
    public createAppointment(appointment: IAppointment) {
        return this.httpClient.post(`${this.apiUrl}/`, appointment)
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    public bookCurrentUser(apptId: string) {
        return this.httpClient.post(`${this.apiUrl}/book-me/${apptId}`, {}, { observe: 'response' })
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    public bookOtherUser(apptId: string, email: string) {
        return this.httpClient.post(`${this.apiUrl}/book-them/${apptId}?email=${email}`, {}, { observe: 'response' })
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    public unbookCurrentUser(apptId: string) {
        return this.httpClient.post(`${this.apiUrl}/unbook-me/${apptId}`, {}, { observe: 'response' })
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    public unbookOtherUser(apptId: string, email: string) {
        return this.httpClient.post(`${this.apiUrl}/unbook-them/${apptId}?email=${email}`, {}, { observe: 'response' })
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    public getAppointmentById(apptId: string): Observable<IAppointment> {
        return this.httpClient.get(`${this.apiUrl}/${apptId}`)
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
