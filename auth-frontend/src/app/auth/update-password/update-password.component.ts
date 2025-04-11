import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-update-password',
  templateUrl: './update-password.component.html',
  imports: [CommonModule, ReactiveFormsModule]
})
export class UpdatePasswordComponent {
  updateForm: FormGroup;
  message: string = '';
  error: string = '';

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.updateForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      oldPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.updateForm.invalid) return;

    this.authService.updatePassword(this.updateForm.value).subscribe({
      next: (res) => {
        this.message = 'Mot de passe mis à jour avec succès';
        this.error = '';
      },
      error: (err) => {
        this.message = '';
        this.error = err.error || 'Erreur inconnue';
      }
    });
  }
}