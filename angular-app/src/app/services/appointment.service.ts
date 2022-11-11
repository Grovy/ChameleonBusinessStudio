import { Injectable } from '@angular/core';
import { IUser, UserRole } from '../models/interfaces/IUser';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, delay, Observable, tap, throwError } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  private readonly apiUrl = '/api';
  private allowedList =[UserRole.ADMIN,UserRole.ORGANIZER,UserRole.TALENT,"ADMIN","ORGANIZER"];
  constructor(private httpClient: HttpClient) { }

  /**
   * get all the appointment list
   * : for the Admins/Organizer/talent
   * checks if the user is allowed or not (only ADMIN Organizers Talent)
   * can view all appointments
   */

  getAllappointments(user: UserRole) : Observable<any>{
    console.log(`From getAllappointments: user Role is ${user} and type is ${typeof user}`);
        if(!this.allowedList.includes(user)){
      return throwError(()=>`NOT AUTHORIZED`)
  }
    var st = this.httpClient.get(`${this.apiUrl}/v1/appointments`)
                    .pipe(
                      catchError(this.handleError),
                      delay(4000),
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
                    catchError(this.handleError),
                    delay(4000),
                  )
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.log(error);
    return throwError(()=> `An error occured - Error code: ${error.status}`);
}


}


