import { Component } from '@angular/core';

/*
Spring Boot's implementation of "sign in with Google" handles the bulk of the
work.

Since the splash page will be so different from the rest of the website, we may
wind up putting the sign in button in a non-Angular page. For example, we may
just have a template HTML file that we fill out and serve to unauthenticated
users. If so, we'll just need to rework this component into a basic HTML one,
which shouldn't be too hard.
*/

@Component({
    selector: 'sign-in',
    templateUrl: './signin.component.html',
    styleUrls: ['./signin.component.css']
})
export class SignInComponent {
    // GNDN
}