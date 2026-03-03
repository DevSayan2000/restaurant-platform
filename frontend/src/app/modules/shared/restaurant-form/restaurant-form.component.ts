import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-restaurant-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, InputTextModule, SelectModule, ButtonModule],
  templateUrl: './restaurant-form.component.html',
})
export class RestaurantFormComponent implements OnChanges {
  @Input() initialData: any = null; // for edit mode
  @Input() loading = false;
  @Output() formSubmit = new EventEmitter<any>();
  @Output() cancel = new EventEmitter<void>();

  submitted = false;

  foodTypes = [
    { label: 'VEG', value: 'VEG' },
    { label: 'NON VEG', value: 'NON_VEG' },
    { label: 'JAIN', value: 'JAIN' },
    { label: 'VEGAN', value: 'VEGAN' },
  ];

  restaurantForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.restaurantForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      city: ['', Validators.required],
      cuisine: ['', Validators.required],
      foodType: ['', Validators.required],
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['initialData'] && this.initialData) {
      this.restaurantForm.patchValue(this.initialData);
    }
  }

  get f() {
    return this.restaurantForm.controls;
  }

  submit() {
    this.submitted = true;

    if (this.restaurantForm.invalid) return;

    this.formSubmit.emit(this.restaurantForm.value);
  }

  reset() {
    this.restaurantForm.reset();
    this.submitted = false;
  }
}
