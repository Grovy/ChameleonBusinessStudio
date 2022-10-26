import { catchError, Observable, tap, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class AuthenticationService {

    private readonly apiUrl = '/api/v1/auth';

    constructor (private httpClient: HttpClient) { }

    // Returns the email address of the authenticated user
    public getPrincipal(): Observable<string> {
        return this.httpClient.get(`${this.apiUrl}/principal`, { responseType: 'text'})
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    // Checks Vendia to see if the authenticated user exists in the User table
    public isUserRegistered(): Observable<any> {
        return this.httpClient.get(`${this.apiUrl}/isUserRegistered`)
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    // Returns true if the user is authenticated
    public isAuthenticated(): Observable<any> {
        return this.httpClient.get(`${this.apiUrl}/isAuthenticated`, {responseType: 'text'})
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