import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  role: string = '';

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      cinCard: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      role: ['', Validators.required],
      branch: ['', Validators.required] // Moved branch field here
    });
  }

  onRoleChange(role: string) {
    this.role = role;

    if (role === 'student') {
      this.registerForm.addControl('yearOfAdmission', this.fb.control('', Validators.required));
      this.registerForm.addControl('educationalLevel', this.fb.control('', Validators.required));

      // Supprimer les champs alumni
      this.registerForm.removeControl('graduationYear');
      this.registerForm.removeControl('professionalStatus');
    } else if (role === 'alumni') {
      this.registerForm.addControl('graduationYear', this.fb.control('', Validators.required));
      this.registerForm.addControl('professionalStatus', this.fb.control('', Validators.required));

      // Supprimer les champs student
      this.registerForm.removeControl('yearOfAdmission');
      this.registerForm.removeControl('educationalLevel');
    }
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const formValue = this.registerForm.value;
      const payload = {
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        cinCard: formValue.cinCard,
        email: formValue.email,
        role: formValue.role,
        branch: formValue.branch,
        formDetails: {}
      };

      if (formValue.role === 'student') {
        payload.formDetails = {
          yearOfAdmission: formValue.yearOfAdmission,
          educationalLevel: formValue.educationalLevel
        };
      } else if (formValue.role === 'alumni') {
        payload.formDetails = {
          graduationYear: formValue.graduationYear,
          professionalStatus: formValue.professionalStatus
        };
      }

      console.log('Form Submitted ✅', payload);

      this.authService.signUp(payload).subscribe({
        next: response => {
          console.log('Inscription réussie ✅', response);
          this.router.navigate(['/thank-you']);
        },
        error: err => {
          console.error('Erreur d’inscription ❌', err);
        }
      });
    } else {
      console.warn('Invalid form ❌');
    }
  }
}