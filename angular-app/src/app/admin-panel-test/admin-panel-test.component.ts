import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {MatIconModule} from '@angular/material/icon';
import {MatTooltipModule} from "@angular/material/tooltip";

export interface UserData {
  name: string;
  email: string;
  phone: string;
  role: string;
}

/** Constants used to fill up our data base. */
const ROLES: string[] = [
  'Admin',
  'Employee',

];

const EMAIL: string[] = [
  'example@gmail.com',

];

const NAMES: string[] = [
  'Matt',
  'Daniel',
  'Daniel',
  'Ariel',
  'Nero',
  'Rozan',
  'Dave',
  'Theodore',
  'Isla',
  'Oliver',
  'Isabella',
  'Jasper',
  'Cora',
  'Levi',
  'Violet',
  'Arthur',
  'Mia',
  'Thomas',
  'Elizabeth',
];

@Component({
  selector: 'app-admin-panel-test',
  templateUrl: './admin-panel-test.component.html',
  styleUrls: ['./admin-panel-test.component.css']
})
export class AdminPanelTestComponent implements AfterViewInit {
  displayedColumns: string[] = ['name', 'email', 'phone', 'role', 'actions'];
  dataSource: MatTableDataSource<UserData>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor() {
    // Create 100 users
    const users = Array.from({length: 100}, (_, k) => createNewUser(k + 1));

    // Assign the data to the data source for the table to render
    this.dataSource = new MatTableDataSource(users);
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
}

/** Builds and returns a new User. */
function createNewUser(id: number): UserData {
  const name =
    NAMES[Math.round(Math.random() * (NAMES.length - 1))] +
    ' ' +
    NAMES[Math.round(Math.random() * (NAMES.length - 1))].charAt(0) +
    '.';

  return {
    name: name,
    email: EMAIL.toString(),
    phone: Math.floor(Math.random() * 10000000000).toString(),
    role: ROLES[Math.round(Math.random() * (ROLES.length - 1))],
  };
}
