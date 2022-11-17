import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ThemePalette } from '@angular/material/core';

@Component({
  selector: 'app-talent-nav',
  templateUrl: './talent-nav.component.html',
  styleUrls: ['./talent-nav.component.css']
})
export class TalentNavComponent {

  navLinks: any[];
  activeLinkIndex = -1;
  background: ThemePalette = undefined;

  constructor(private router: Router) {
    this.navLinks = [
      {
        label: 'Your Dashboard',
        link: './',
        index: 0,
      }, {
        label: 'Edit Schedule',
        link: './schedule-configuration',
        index: 1,
      }
    ]
  }

  ngOnInit(): void {
    this.router.events.subscribe((res) => {
      this.activeLinkIndex = this.navLinks.indexOf(this.navLinks.find(tab => tab.link === '.' + this.router.url))
    })
  }

}