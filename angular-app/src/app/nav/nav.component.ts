import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {ThemePalette} from '@angular/material/core';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent {

  navLinks: any[];
  activeLinkIndex = -1;
  background: ThemePalette = undefined;

  constructor(private router: Router) {
    this.navLinks = [
      {
        label: 'Setup Wizard',
        link: './setup-wizard',
        index: 0,
      }, {
        label: 'Admin Configuration',
        link: './admin-configuration',
        index: 1,
      } , {
        label: 'Booking Page',
        link: './booking-page',
        index: 2,
      }, {
        label: 'Admin Panel',
        link: './admin-panel',
        index: 3,
      }, {
        label: 'Admin Panel Test',
        link: './admin-panel-test',
        index: 4,
      }, {
        label: 'Landing Page Configuration',
        link: './landing-page-configuration',
        index: 5,
      }
    ]
  }

  ngOnInit(): void {
    this.router.events.subscribe((res) => {
      this.activeLinkIndex = this.navLinks.indexOf(this.navLinks.find(tab => tab.link === '.' + this.router.url))
    })
  }

}
