import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { SignUpRequest } from './models/sign-up-request.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }
  login(email: string, password: string) {
    return this.http.post<any>('http://localhost:8081/api/auth/login', {
      email,
      password
    }).pipe(
      tap(response => {
        if (response.token) {
          localStorage.setItem('authToken', response.token);
        }
      })
    );
  }

  signUp(data: any) {
    return this.http.post<any>('http://localhost:8081/api/auth/signup', data);
  }

  updatePassword(data: { email: string; oldPassword: string; newPassword: string }) {
    return this.http.post<any>('http://localhost:8081/api/auth/update-password', data);
  }

  getSignUpRequests(): Observable<SignUpRequest[]> {
    return this.http.get<SignUpRequest[]>('http://localhost:8081/api/auth/admin/sign-up-requests');
  }

  approveSignUpRequest(id: string) {
    return this.http.post<any>(`http://localhost:8081/api/auth/admin/sign-up-requests/${id}/approve`, {});
  }

  rejectSignUpRequest(id: string) {
    return this.http.post<any>(`http://localhost:8081/api/auth/admin/sign-up-requests/${id}/reject`, {});
  }
}
