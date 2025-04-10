import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.value;
      console.log('Form submitted with', email, password);
      this.authService.login(email, password).subscribe({
        next: (res) => {
          console.log('Login réussi 🎉', res);
          // ici, tu pourrais stocker le token ou rediriger
        },
        error: (err) => {
          console.error('Erreur de connexion ❌', err);
        }
      });
    }
  }
}
