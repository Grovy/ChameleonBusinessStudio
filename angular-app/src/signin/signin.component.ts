import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface GoogleClientData {
    google_client_id: string;
};

@Component({
    selector: 'sign-in',
    templateUrl: './signin.component.html',
    styleUrls: ['./signin.component.css']
})
export class SignInComponent {
    googleClientData: GoogleClientData;
    constructor(private http: HttpClient) {
        this.googleClientData = {
            google_client_id: "not found"
        };
        
        // don't want to hard code this URL
        // queries the Spring Boot server 
        http.get<GoogleClientData>("http://localhost:8080/auth/credentials").subscribe((data: GoogleClientData)=>{
            this.googleClientData = data;
        });
    }
}
