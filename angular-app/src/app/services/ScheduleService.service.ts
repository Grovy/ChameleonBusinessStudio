import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { ISchedule } from '../models/interfaces/ISchedule';
import { catchError, Observable, tap, throwError } from 'rxjs';

@Injectable()
export class ScheduleService {

    private readonly apiUrl = '/api/v1/schedules';

    constructor(private httpClient: HttpClient) { }

    // Function to create a new schedule
    public createSchedule(schedule: ISchedule): Observable<any> {
        return this.httpClient.post(`${this.apiUrl}`, schedule)
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    // Function to get all the schedules
    public getAllSchedules(): Observable<any> {
        return this.httpClient.get(`${this.apiUrl}`)
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