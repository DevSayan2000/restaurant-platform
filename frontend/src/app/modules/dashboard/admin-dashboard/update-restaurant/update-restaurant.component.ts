import { CommonModule } from "@angular/common";
import { Component, Input } from "@angular/core";
import { RestaurantApiService } from "app/core/services/restaurant-api.service";
import { RestaurantService } from "app/core/services/restaurant.service";
import { RestaurantFormComponent } from "app/modules/shared/restaurant-form/restaurant-form.component";
import { DialogModule } from "primeng/dialog";

@Component({
  selector: 'app-update-restaurant',
  standalone: true,
  imports: [CommonModule, DialogModule, RestaurantFormComponent],
  template: `
    <p-dialog
      [(visible)]="visible"
      header="Update Restaurant"
      [modal]="true"
      [style]="{ width: '450px' }"
    >
      <app-restaurant-form
        [initialData]="restaurant"
        (formSubmit)="update($event)"
        (cancel)="visible = false"
      >
      </app-restaurant-form>
    </p-dialog>
  `,
})
export class UpdateRestaurantComponent {
  @Input() restaurant: any;
  visible = false;

  constructor(private api: RestaurantApiService, private restaurantService: RestaurantService) {}

  update(payload: any) {
    this.api.updateRestaurant(this.restaurant.id, payload).subscribe(() => {
      this.restaurantService.refresh();
      this.visible = false;
    });
  }
}
