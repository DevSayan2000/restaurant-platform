import { Component, EventEmitter, Input, Output } from '@angular/core';
import { User } from 'app/core/auth/auth.service';
import { Role } from 'app/core/enums/role.enum';
import { ConfirmationService } from 'app/core/services/confirmation.service';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';

@Component({
  selector: 'app-users-table',
  standalone: true,
  templateUrl: './users-table.component.html',
  imports: [TableModule, CardModule, TagModule, ButtonModule],
})
export class UsersTableComponent {
  @Input() users: User[] = [];
  @Output() delete = new EventEmitter<number>();

  role = Role;

  constructor(private confirm: ConfirmationService) {}

  onDeleteClick(id: number) {
    this.confirm
      .confirmDelete('Delete User?', 'Are you sure you want to delete this user?')
      .subscribe((yes) => {
        if (yes) {
          this.delete.emit(id);
        }
      });
  }
}
