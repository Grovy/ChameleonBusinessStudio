import { Injectable } from '@angular/core';
import { IUser, UserRole } from '../models/interfaces/IUser';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  private readonly apiUrl = '/api';
  private allowedList =[UserRole.ADMIN,UserRole.ORGANIZER,UserRole.TALENT]
  constructor(private httpClient: HttpClient) { }

  /**
   * get all the appointment list
   * : for the Admins/Organizer/talent
   * checks if the user is allowed or not (only ADMIN Organizers Talent)
   * can view all appointments
   */

  getAllappointments(user: UserRole) : Observable<any>{
    if(this.allowedList.includes(user)){
      return throwError(()=>`NOT AUTHORIZED`)
  }
    var st = this.httpClient.get(`${this.apiUrl}/v1/appointments`)
                    .pipe(
                      tap(console.log),
                      catchError(this.handleError)
                    );

    return st;
  }

  /**
   * get the appointment of the user (PARTICIPANT)
   * @param user
   */

  getappointmentsbyUser():Observable<any>{
      return this.httpClient.get(`${this.apiUrl}/v1/appointments/mine`)
                  .pipe(
                    catchError(this.handleError)
                  )
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.log(error);
    return throwError(()=> `An error occured - Error code: ${error.status}`);
}


}


