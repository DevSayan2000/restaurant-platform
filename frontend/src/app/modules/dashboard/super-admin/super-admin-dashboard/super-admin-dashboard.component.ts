import { Component, OnInit } from '@angular/core';
import { AnalyticsCardsComponent } from '../components/analytics-cards/analytics-cards.component';
import { UsersTableComponent } from '../components/users-table/users-table.component';
import { RestaurantsTableComponent } from '../components/restaurants-table/restaurants-table.component';
import { TabsModule } from 'primeng/tabs';
import { CardModule } from 'primeng/card';
import { UserApiService } from 'app/core/services/user-api.service';
import { User } from 'app/core/auth/auth.service';
import { Restaurant, RestaurantApiService } from 'app/core/services/restaurant-api.service';
import { MessageService } from 'primeng/api';
import { AnalyticsService } from 'app/core/services/analytics.service';
import { Analytics } from 'app/core/services/analytic-api.service';
import { Subject, takeUntil } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-super-admin-dashboard',
  standalone: true,
  templateUrl: './super-admin-dashboard.component.html',
  imports: [
    AnalyticsCardsComponent,
    UsersTableComponent,
    RestaurantsTableComponent,
    TabsModule,
    CardModule,
    CommonModule
],
})
export class SuperAdminDashboardComponent implements OnInit {
  users: User[] = [];
  restaurants: Restaurant[] = [];
  activeTab: number = 0;
  analytics: Analytics | null = null;

  private destroy$ = new Subject<void>();

  constructor(
    private userApiService: UserApiService,
    private restaurantAPiService: RestaurantApiService,
    private messageService: MessageService,
    private analyticService: AnalyticsService,
  ) {}

  ngOnInit(): void {
    this.fetchData();
    this.analyticService.getAnalytics();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  fetchData() {
    this.userApiService.getUsers().subscribe((response) => (this.users = response.userResponses));
    this.restaurantAPiService
      .getRestaurants()
      .subscribe((response) => (this.restaurants = response.restaurantResponses));

    this.analyticService.analytics$.pipe(takeUntil(this.destroy$)).subscribe((value) => {
      this.analytics = value;
    });
  }

  deleteRestaurant(id: number) {
    this.restaurantAPiService.deleteRestaurant(id).subscribe({
      next: (response) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Restaurant Deleted',
          detail: response.message,
        });
        this.restaurants = this.restaurants.filter((x) => x.id !== id);
        this.analyticService.getAnalytics();
      },
    });
  }

  deleteUser(id: number) {
    this.userApiService.deleteUser(id).subscribe({
      next: (response) => {
        this.messageService.add({
          severity: 'success',
          summary: 'User Deleted',
          detail: response.message,
        });
        this.users = this.users.filter((x) => x.id !== id);
        this.analyticService.getAnalytics();
      },
    });
  }

  onTabChange(event: any) {
    this.activeTab = event;
  }
}
