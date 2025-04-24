import { Component, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './contact.component.html',
})
export class ContactComponent {
  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    message: ['', [Validators.required, Validators.maxLength(300)]],
  });
  submitted = false;
  success = signal(false);

  constructor(private fb: FormBuilder) {}

  get f() {
    return this.form.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.form.invalid) {
      return;
    }
    this.success.set(true);
    this.form.reset();
    this.submitted = false;
  }
}
