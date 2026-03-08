import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { CheckboxModule } from 'primeng/checkbox';
import { MenuCategory, MenuCategoryLabels } from 'app/core/enums/menu-category.enum';
import { CreateMenuItemPayload, UpdateMenuItemPayload, MenuItemResponse } from 'app/core/services/restaurant-api.service';

@Component({
  selector: 'app-menu-item-form',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    DialogModule,
    ButtonModule,
    InputTextModule,
    InputNumberModule,
    TextareaModule,
    SelectModule,
    CheckboxModule,
  ],
  template: `
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <div class="flex flex-col gap-4">
        <!-- Name -->
        <div class="flex flex-col gap-1">
          <label class="font-medium text-sm text-zinc-700 dark:text-zinc-300">Name *</label>
          <input pInputText formControlName="name" placeholder="e.g. Paneer Tikka" />
        </div>

        <!-- Description -->
        <div class="flex flex-col gap-1">
          <label class="font-medium text-sm text-zinc-700 dark:text-zinc-300">Description</label>
          <textarea pTextarea formControlName="description" rows="2" placeholder="Brief description"></textarea>
        </div>

        <!-- Price -->
        <div class="flex flex-col gap-1">
          <label class="font-medium text-sm text-zinc-700 dark:text-zinc-300">Price *</label>
          <p-inputNumber formControlName="price" mode="currency" currency="INR" locale="en-IN"
                         [minFractionDigits]="2" [min]="0.01" placeholder="0.00" />
        </div>

        <!-- Category -->
        <div class="flex flex-col gap-1">
          <label class="font-medium text-sm text-zinc-700 dark:text-zinc-300">Category *</label>
          <p-select formControlName="category" [options]="categoryOptions" optionLabel="label" optionValue="value"
                    placeholder="Select category" />
        </div>

        <!-- Vegetarian & Available -->
        <div class="flex gap-6">
          <div class="flex items-center gap-2">
            <p-checkbox formControlName="vegetarian" [binary]="true" inputId="vegetarian" />
            <label for="vegetarian" class="text-sm text-zinc-700 dark:text-zinc-300">Vegetarian</label>
          </div>
          <div class="flex items-center gap-2">
            <p-checkbox formControlName="available" [binary]="true" inputId="available" />
            <label for="available" class="text-sm text-zinc-700 dark:text-zinc-300">Available</label>
          </div>
        </div>

        <!-- Actions -->
        <div class="flex justify-end gap-2 mt-2">
          <button pButton type="button" label="Cancel" class="p-button-text" (click)="cancel.emit()"></button>
          <button pButton type="submit" [label]="editItem ? 'Update' : 'Add'" [disabled]="form.invalid"></button>
        </div>
      </div>
    </form>
  `,
})
export class MenuItemFormComponent implements OnChanges {
  @Input() editItem: MenuItemResponse | null = null;
  @Output() submitForm = new EventEmitter<CreateMenuItemPayload | UpdateMenuItemPayload>();
  @Output() cancel = new EventEmitter<void>();

  form: FormGroup;

  categoryOptions = Object.values(MenuCategory).map((cat) => ({
    label: MenuCategoryLabels[cat],
    value: cat,
  }));

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      description: [''],
      price: [null, [Validators.required, Validators.min(0.01)]],
      category: [null, Validators.required],
      vegetarian: [false],
      available: [true],
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['editItem'] && this.editItem) {
      this.form.patchValue({
        name: this.editItem.name,
        description: this.editItem.description || '',
        price: this.editItem.price,
        category: this.editItem.category,
        vegetarian: this.editItem.vegetarian,
        available: this.editItem.available,
      });
    } else if (changes['editItem'] && !this.editItem) {
      this.form.reset({ vegetarian: false, available: true });
    }
  }

  onSubmit() {
    if (this.form.valid) {
      this.submitForm.emit(this.form.value);
    }
  }
}

