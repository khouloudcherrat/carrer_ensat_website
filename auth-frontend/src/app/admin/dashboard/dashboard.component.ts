import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { SseService } from '../../shared/sse.service';
import { Partner } from '../../models/partner.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, NgFor, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  signUpRequests: any[] = [];
  partners: Partner[] = [];
  selectedPartnerFilter: 'all' | 'unregistered' = 'all';
  isLoading = false;
  sendingId: string | null = null;

  constructor(private authService: AuthService, private sseService: SseService) {}

  ngOnInit() {
    this.refreshData();
    this.sseService.connect('http://localhost:8081/api/sse/updates', this.refreshData.bind(this));
  }
  
  private refreshData() {
    this.loadPartners();
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


  loadPartners() {
    this.isLoading = true;
    const observable = this.selectedPartnerFilter === 'unregistered'
      ? this.authService.getUnregisteredPartners()
      : this.authService.getAllPartners();
  
    observable.subscribe({
      next: (res) => this.partners = res,
      error: (err) => console.error(err),
      complete: () => this.isLoading = false
    });
  }

  sendCredentials(id: string) {
    this.sendingId = id;
    this.authService.sendPartnerCredentials(id).subscribe({
      next: () => {
        alert("Identifiants envoyés au partenaire.");
        this.loadPartners();
      },
      error: (err) => console.error(err),
      complete: () => this.sendingId = null
    });
  }
}
