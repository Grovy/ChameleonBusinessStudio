import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";


/**
 * interacts with the WebsiteAppearanceController in the backend via the
 * `/api/v1/config`
 * route
 */
@Injectable()
export class WebsiteAppearanceService {

    private readonly apiUrl = '/api/v1/config';

    constructor(private httpClient: HttpClient) {

    }

    /**
     * See https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/color
     * for how the banner color should be formatted.
     * 
     * @param color the new banner color
     * @returns an observable that must be subscribed to
     */
    public setBannerColor(color: string): Observable<any> {
        const formData = new FormData();
        formData.append("bannerColor", color);
        
        return this.httpClient.post<any>(this.route('banner-color'), formData)
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    /**
     * Sets the website logo in the backend.
     * 
     * @param file the new image file to use as a website logo
     * @returns an observable that must be subscribed to
     */
    public setLogo(file: File): Observable<any> {
        const formData = new FormData();
        formData.append("file", file); // backend requires this exact name "file"

        return this.httpClient.post<any>(this.route('logo'), formData)
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    /**
     * Sets the splash page content in the backend.
     * 
     * @param file an HTML file to use in the splash page
     * @returns an observable that must be subscribed to
     */
    public setSplashPage(file: File): Observable<any> {
        const formData = new FormData();
        formData.append("file", file); // backend requires this exact name "file"

        return this.httpClient.post<any>(this.route('splash'), formData)
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    /**
     * Sets the organization name in the backend.
     * 
     * @param name the name of your organization
     * @returns an observable that must be subscribed to
     */
    public setOrganizationName(name: string): Observable<any> {
        const formData = new FormData();
        formData.append("organizationName", name);

        return this.httpClient.post<any>(this.route('organization'), formData)
            .pipe(
                tap(console.log),
                catchError(this.handleError)
            );
    }

    private route(endpoint: string): string {
        return this.apiUrl  + '/' + endpoint;
    }

    private handleError(error: HttpErrorResponse): Observable<any> {
        console.error(error);
        return throwError(() => `An error occurred: ${error.message}`);
    }
}