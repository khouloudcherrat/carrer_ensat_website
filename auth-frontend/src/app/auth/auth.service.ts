import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { SignUpRequest } from '../core/models/sign-up-request.model';
import { Partner } from '../core/models/partner.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private API_BASE_URL = 'http://localhost:8081/api/auth';
  constructor(private http: HttpClient) { }
  login(email: string, password: string) {
    return this.http.post<any>(`${this.API_BASE_URL}/login`, {
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
    return this.http.post<any>(`${this.API_BASE_URL}/signup`, data);
  }

  updatePassword(data: { email: string; oldPassword: string; newPassword: string }) {
    return this.http.post<any>(`${this.API_BASE_URL}/update-password`, data);
  }

  getSignUpRequests(): Observable<SignUpRequest[]> {
    return this.http.get<SignUpRequest[]>(`${this.API_BASE_URL}/admin/sign-up-requests`);
  }

  approveSignUpRequest(id: string) {
    return this.http.post<any>(`${this.API_BASE_URL}/admin/sign-up-requests/${id}/approve`, {});
  }

  rejectSignUpRequest(id: string) {
    return this.http.post<any>(`${this.API_BASE_URL}/admin/sign-up-requests/${id}/reject`, {});
  }

  getAllPartners(): Observable<Partner[]> {
    return this.http.get<Partner[]>(`${this.API_BASE_URL}/admin/all-partners`, {});
  }

  getUnregisteredPartners(): Observable<Partner[]> {
    return this.http.get<Partner[]>(`${this.API_BASE_URL}/admin/unregistered-partners`, {});
  }
  
  sendPartnerCredentials(id: string): Observable<any> {
    return this.http.post(`${this.API_BASE_URL}/admin/partners/${id}/send-credentials`, {});
  }
}
