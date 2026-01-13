import { Routes } from '@angular/router';
import { RestaurantDetailsComponent } from './restaurant-details.component';

export default [
  {
    path: ':restaurantId',
    component: RestaurantDetailsComponent,
  },
] as Routes;
