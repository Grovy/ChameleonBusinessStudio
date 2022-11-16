import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { IUser } from '../models/interfaces/IUser';

@Injectable()
export class UserService {

    private readonly apiUrl = '/api/v1/users';

    constructor(private httpClient: HttpClient) { }

    // Function to fetch all the users from Vendia
    public getAllUsers(): Observable<any> {
        return this.httpClient.get(`${this.apiUrl}/`)
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    // Function to create a new user and save in Vendia
    public createUser(user: IUser): Observable<any> {
        return this.httpClient.post(`${this.apiUrl}`, user)
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    // Function to get the user object from Vendia
    public getUser(email: string): Observable<any> {
        return this.httpClient.get(`${this.apiUrl}/${email}`)
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    // Function to update an existing user in Vendia
    public updateUser(user: IUser): Observable<any> {
        return this.httpClient.put(`${this.apiUrl}/`, user, { observe: 'response' })
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