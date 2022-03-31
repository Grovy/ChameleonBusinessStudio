import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

/*
I can remove a lot of this if we just stick to Spring Boot's implementation of
sign in with Google

https://developers.google.com/identity/gsi/web/guides/overview



Good GRAVY Angular does NOT want to work with Google libraries.

Angular only loads script tags in the document head

In order for Sign in with Google to work, the following must occur in this order:
1. the Angular App reads the google credentials. Since fs is not allowed, it
   must query the Spring Boot backend.
2. the Angular App must insert the contents of signin.component.html into the
   webpage
3. load the Google Auth Library, it finds the elements inserted by step 2, then
   creates the login button.

However, Angular loads the HTML page BEFORE components (which makes sense from a
topological iteration) so step 3 would occur before even step 1 if the google
library were in the HTML source. Solution: dynamically insert the script tag, as
seen below
*/

interface GoogleClientData {
    google_client_id: string;
};

@Component({
    selector: 'sign-in',
    templateUrl: './signin.component.html',
    styleUrls: ['./signin.component.css']
})
export class SignInComponent {
    // GNDN
}

/*
old implementation using the minimal "sign in with google"
I really like how this one looks, but I'm not sure if it's compatible with
Spring, as I couldn't find any way of setting the Principal or having Spring
mark the request as "Authenticated" using this method
*/
class SignInComponentOld {
    // Google Authentication library
    SCRIPT_URL: string = "https://accounts.google.com/gsi/client";
    googleClientData: GoogleClientData;
    
    constructor(private http: HttpClient) {
        this.googleClientData = {
            google_client_id: "waiting for response from Spring Boot server..."
        };
        
        // I don't like hard coding this URL
        // queries the Spring Boot server 
        http.get<GoogleClientData>("http://localhost:8080/auth/credentials").subscribe((data: GoogleClientData)=>{
            this.googleClientData = data;
            if(!this.isScriptLoaded()){
                this.loadScript();
            }
        });
    }
    
    // https://stackoverflow.com/questions/44204417/dynamically-load-external-javascript-file-from-angular-component
    private isScriptLoaded(){
        const scriptTags = document.getElementsByTagName("script");
        return Array.from(scriptTags).some((st)=>{
            return st.src === this.SCRIPT_URL;
        });
    }
    
    private loadScript(){
        // Google Authentication library
        const tag = document.createElement("script");
        tag.src = this.SCRIPT_URL;
        tag.type = "text/javascript";
        document.getElementsByTagName("head")[0].appendChild(tag);
    }
}
