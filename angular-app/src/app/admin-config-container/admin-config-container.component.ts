import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-admin-config-container',
  templateUrl: './admin-config-container.component.html',
  styleUrls: ['./admin-config-container.component.css']
})
export class AdminConfigContainerComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  url="./assets/images/SacState.jpg"
  onselectFile(e:any) {
    if(e.target.files) {
      var reader = new FileReader();
      reader.readAsDataURL(e.target.files[0]);
      reader.onload=(event:any)=>{
        this.url=event.target.result;
      }
    }
  }

}
