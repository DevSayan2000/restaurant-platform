import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Restaurant, RestaurantApiService } from './restaurant-api.service';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root',
})
export class RestaurantService {
  private restaurantsSubject = new BehaviorSubject<Restaurant[]>([]);
  restaurants$ = this.restaurantsSubject.asObservable();

  constructor(
    private restaurantApiService: RestaurantApiService,
    private messageService: MessageService
  ) {}

  /** Get current restaurants */
  getRestaurants() {
    return this.restaurantsSubject.value;
  }

  /** Delete restaurant */
  deleteRestaurant(id: number) {
    this.restaurantApiService.deleteRestaurant(id).subscribe({
      next: (response) => {
        const filtered = this.restaurantsSubject.value.filter((r) => r.id !== id);
        this.restaurantsSubject.next(filtered);
        this.messageService.add({
          severity: 'success',
          summary: 'Restaurant Deleted',
          detail: response.message,
        });
      },
    });
  }

  /** Clear all restaurants */
  clearAll() {
    this.restaurantsSubject.next([]);
  }

  refresh() {
    this.restaurantApiService.getRestaurants().subscribe({
      next: (response) => {
        this.restaurantsSubject.next(response);
      },
    });
  }
}
