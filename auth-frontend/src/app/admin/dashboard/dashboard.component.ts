import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { CommonModule, NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, NgFor],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  signUpRequests: any[] = [];

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.loadRequests();
  }

  private loadRequests() {
    this.authService.getSignUpRequests().subscribe(requests => {
      this.signUpRequests = requests;
    });
  }

  approve(id: string){
    this.authService.approveSignUpRequest(id).subscribe(() => {
      this.loadRequests();
    });
  }

  reject(id: string){
    this.authService.rejectSignUpRequest(id).subscribe(() => {
      this.loadRequests();
    });
  }
}
