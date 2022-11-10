import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';



@Component({
    selector: 'site-header',
    templateUrl: './site-header.component.html',
    styleUrls: ['./site-header.component.css']
})
export class SiteHeaderComponent {
    name="";
    
    constructor(private http: HttpClient){
        http.get<{name: string}>("/api/v1/config/organization").subscribe((obj: {name: string})=>{
            this.name = obj.name;
        });
        http.get<{color: string}>("/api/v1/config/banner-color").subscribe((obj: {color: string})=>{
            const h = <HTMLElement>document.querySelector("#site_header");
            h.style.backgroundColor = obj.color;
        });
    }
}