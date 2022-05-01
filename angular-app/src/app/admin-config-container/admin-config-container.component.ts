import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";


@Component({
  selector: 'app-admin-config-container',
  templateUrl: './admin-config-container.component.html',
  styleUrls: ['./admin-config-container.component.css']
})
export class AdminConfigContainerComponent implements OnInit {

  name="";
  url="";


  constructor(private http: HttpClient) {
    http.get<{name: string}>("/custom/organization").subscribe((obj: {name: string})=>{
      this.name = obj.name;
    });
    http.get<{color: string}>("/custom/banner").subscribe((obj: {color: string})=>{
      const h = <HTMLElement>document.querySelector("#site-header");
      h.style.backgroundColor = obj.color;
    });
  }

  ngOnInit(): void {
  }


}
