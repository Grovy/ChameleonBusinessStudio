import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';



@Component({
    selector: 'site-header',
    templateUrl: './site-header.component.html',
    styleUrls: []
})
export class SiteHeaderComponent {
    name="";
    
    constructor(private http: HttpClient){
        http.get<{name: string}>("/custom/organization").subscribe((obj: {name: string})=>{
            this.name = obj.name;
        });
        http.get<{color: string}>("/custom/banner").subscribe((obj: {color: string})=>{
            const h = <HTMLElement>document.querySelector("#site-header");
            h.style.backgroundColor = obj.color;
        });
    }
}