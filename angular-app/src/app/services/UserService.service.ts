import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';

@Injectable()
export class UserService {

    private readonly apiUrl = 'http://localhost:8080/api';

    constructor(private httpClient: HttpClient) { }

    // Function to fetch all the users from Vendia
    public getAllUsers(): Observable<any> {
        return this.httpClient.get(`${this.apiUrl}/users/getAllUsers`)
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