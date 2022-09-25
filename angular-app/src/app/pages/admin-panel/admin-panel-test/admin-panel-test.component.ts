import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { IUser } from 'src/app/models/interfaces/IUser';
import { AdminPanelTestUsers } from 'src/app/models/mock/mock-admin-panel-users';

@Component({
  selector: 'app-admin-panel-test',
  templateUrl: './admin-panel-test.component.html',
  styleUrls: ['./admin-panel-test.component.css']
})
export class AdminPanelTestComponent implements AfterViewInit {
  displayedColumns: string[] = ['name', 'email', 'phone', 'role', 'actions'];
  dataSource: MatTableDataSource<IUser>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor() {
    // Create 100 users
    // const users = Array.from({length: 100}, (_, k) => createNewUser(k + 1));
    const myUsers = AdminPanelTestUsers;
    console.log(myUsers);

    // Assign the data to the data source for the table to render
    this.dataSource = new MatTableDataSource(myUsers);
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
