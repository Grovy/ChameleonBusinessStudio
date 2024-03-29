import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ThemePalette } from '@angular/material/core';

@Component({
  selector: 'app-admin-nav',
  templateUrl: './admin-nav.component.html',
  styleUrls: ['./admin-nav.component.css']
})
export class AdminNavComponent {

  navLinks: any[];
  activeLinkIndex = -1;
  background: ThemePalette = undefined;

  constructor(private router: Router) {
    this.navLinks = [
      {
        label: 'Admin Configuration',
        link: './admin-configuration',
        index: 0,
      }, {
        label: 'Admin Panel',
        link: './admin-panel',
        index: 1,
      }, {
        label: 'Appointment Details',
        link: './appointment-details',
        index: 2,
      }
    ]
  }

  ngOnInit(): void {
    this.router.events.subscribe((res) => {
      this.activeLinkIndex = this.navLinks.indexOf(this.navLinks.find(tab => tab.link === '.' + this.router.url))
    })
  }

}
