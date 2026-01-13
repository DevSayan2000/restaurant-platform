import { Component, OnInit } from '@angular/core';
import { AnalyticsCardsComponent } from '../components/analytics-cards/analytics-cards.component';
import { UsersTableComponent } from '../components/users-table/users-table.component';
import { RestaurantsTableComponent } from '../components/restaurants-table/restaurants-table.component';
import { TabsModule } from 'primeng/tabs';
import { CardModule } from 'primeng/card';
import { UserApiService } from 'app/core/services/user-api.service';
import { User } from 'app/core/auth/auth.service';
import { Restaurant, RestaurantApiService } from 'app/core/services/restaurant-api.service';

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
  ],
})
export class SuperAdminDashboardComponent implements OnInit {
  users: User[] = [];
  restaurants: Restaurant[] = [];
  activeTab: number = 0;

  constructor(
    private userApiService: UserApiService,
    private restaurantAPiService: RestaurantApiService
  ) {}

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData() {
    this.userApiService.getUsers().subscribe((response) => (this.users = response));
    this.restaurantAPiService
      .getRestaurants()
      .subscribe((response) => (this.restaurants = response));
  }

  deleteRestaurant(id: number) {
    // this.service.deleteRestaurant(id).subscribe(() => {
    //   this.restaurants = this.restaurants.filter(x => x.id !== id);
    // });
  }

  onTabChange(event: any) {
    this.activeTab = event;
  }
}
