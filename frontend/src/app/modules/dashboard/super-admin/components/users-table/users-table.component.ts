import { Component, Input } from '@angular/core';
import { User } from 'app/core/auth/auth.service';
import { Role } from 'app/core/enums/role.enum';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';

@Component({
  selector: 'app-users-table',
  standalone: true,
  templateUrl: './users-table.component.html',
  imports: [TableModule, CardModule, TagModule]
})
export class UsersTableComponent {
  @Input() users: User[] = [];
  role = Role
}
