import { Component, Input, OnChanges, OnDestroy, SimpleChanges } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { TabsModule } from 'primeng/tabs';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { Subject, takeUntil } from 'rxjs';
import {
  RestaurantApiService,
  MenuItemResponse,
  MenuResponse,
  CreateMenuItemPayload,
  UpdateMenuItemPayload,
} from 'app/core/services/restaurant-api.service';
import { MenuItemFormComponent } from '../menu-item-form/menu-item-form.component';
import { Role } from 'app/core/enums/role.enum';
import { AuthService, User } from 'app/core/auth/auth.service';

@Component({
  selector: 'app-menu-section',
  standalone: true,
  imports: [
    CommonModule,
    ButtonModule,
    DialogModule,
    TagModule,
    TabsModule,
    ConfirmDialogModule,
    MenuItemFormComponent,
    CurrencyPipe,
  ],
  providers: [ConfirmationService],
  templateUrl: './menu-section.component.html',
})
export class MenuSectionComponent implements OnChanges, OnDestroy {
  @Input() restaurantId: string = '';
  @Input() isOwner: boolean = false;

  menuData: Record<string, MenuItemResponse[]> = {};
  menuCategories: string[] = [];
  totalItems = 0;

  showAddDialog = false;
  showEditDialog = false;
  editingItem: MenuItemResponse | null = null;

  user: User | null = null;
  role = Role;

  private destroy$ = new Subject<void>();

  constructor(
    private restaurantApi: RestaurantApiService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private auth: AuthService
  ) {
    this.auth.user$.pipe(takeUntil(this.destroy$)).subscribe((u) => (this.user = u));
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['restaurantId'] && this.restaurantId) {
      this.loadMenu();
    }
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadMenu() {
    this.restaurantApi
      .getMenu(this.restaurantId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (res: MenuResponse) => {
          this.menuData = res.menu;
          this.menuCategories = Object.keys(res.menu);
          this.totalItems = res.totalItems;
        },
      });
  }

  onAddItem(payload: CreateMenuItemPayload | UpdateMenuItemPayload) {
    this.restaurantApi
      .addMenuItem(this.restaurantId, payload as CreateMenuItemPayload)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Menu item added' });
          this.showAddDialog = false;
          this.loadMenu();
        },
      });
  }

  openEditDialog(item: MenuItemResponse) {
    this.editingItem = { ...item };
    this.showEditDialog = true;
  }

  onUpdateItem(payload: UpdateMenuItemPayload) {
    if (!this.editingItem) return;
    this.restaurantApi
      .updateMenuItem(this.restaurantId, this.editingItem.id, payload)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Menu item updated' });
          this.showEditDialog = false;
          this.editingItem = null;
          this.loadMenu();
        },
      });
  }

  confirmDelete(item: MenuItemResponse) {
    this.confirmationService.confirm({
      message: `Are you sure you want to delete "${item.name}"?`,
      header: 'Delete Menu Item',
      icon: 'pi pi-exclamation-triangle',
      acceptButtonStyleClass: 'p-button-danger p-button-sm',
      rejectButtonStyleClass: 'p-button-text p-button-sm',
      accept: () => {
        this.restaurantApi
          .deleteMenuItem(this.restaurantId, item.id)
          .pipe(takeUntil(this.destroy$))
          .subscribe({
            next: () => {
              this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Menu item deleted' });
              this.loadMenu();
            },
          });
      },
    });
  }
}



