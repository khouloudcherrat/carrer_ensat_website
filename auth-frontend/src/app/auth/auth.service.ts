import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }
  login(email: string, password: string) {
    return this.http.post<any>('http://localhost:8081/api/auth/login', {
      email,
      password
    });
  }

  signUp(data: any) {
    return this.http.post<any>('http://localhost:8081/api/auth/signup', data);
  }

  updatePassword(email: string, oldPassword: string, newPassword: string) {
    return this.http.post<any>('http://localhost:8081/api/auth/update-password', {
      email,
      oldPassword,
      newPassword
    });
  }
}
