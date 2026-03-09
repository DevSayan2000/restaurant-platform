import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { SelectModule } from 'primeng/select';
import { RestaurantApiService } from 'app/core/services/restaurant-api.service';
import { RestaurantService } from 'app/core/services/restaurant.service';
import { RestaurantFormComponent } from 'app/modules/shared/restaurant-form/restaurant-form.component';

@Component({
  selector: 'app-create-restaurant',
  standalone: true,
  templateUrl: './create-restaurant.component.html',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    DialogModule,
    InputTextModule,
    SelectModule,
    ButtonModule,
    RestaurantFormComponent,
  ],
})
export class CreateRestaurantComponent {
  showDialog = false;
  restaurantForm: FormGroup;
  submitted = false;

  foodTypes = [
    { label: 'VEG', value: 'VEG' },
    { label: 'NON VEG', value: 'NON_VEG' },
    { label: 'JAIN', value: 'JAIN' },
    { label: 'VEGAN', value: 'VEGAN' },
  ];

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private restaurantApiService: RestaurantApiService,
    private restaurantService: RestaurantService,
    private router: Router,
  ) {
    this.restaurantForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      city: ['', Validators.required],
      cuisine: ['', Validators.required],
      foodType: ['', Validators.required],
    });
  }

  get f() {
    return this.restaurantForm.controls;
  }

  openDialog() {
    this.showDialog = true;
  }

  closeDialog() {
    this.showDialog = false;
    this.restaurantForm.reset();
    this.submitted = false;
  }

  createRestaurant(payload: any) {
    this.restaurantApiService.createRestaurant(payload).subscribe({
      next: (response) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Restaurant Created',
          detail: `${payload.name} added successfully. You can now add menu items.`,
        });

        this.restaurantService.refresh();
        this.closeDialog();

        // Navigate to restaurant details so admin can add menu items
        if (response.id) {
          this.router.navigate(['/restaurants', response.id]);
        }
      },
    });
  }
}
