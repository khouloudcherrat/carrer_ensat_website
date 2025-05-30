import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { UpdatePasswordComponent } from './auth/update-password/update-password.component';
import { ThankYouComponent } from './auth/thank-you/thank-you.component';
import { DashboardComponent } from './admin/dashboard/dashboard.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'update-password', component: UpdatePasswordComponent },
  { path: 'admin/dashboard', component: DashboardComponent },
  { path: 'thank-you', component: ThankYouComponent }
];