import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SseService {
  private eventSource: EventSource | null = null;

connect(url: string, onMessage: (data: any) => void): void {
  const token = localStorage.getItem('authToken');
  const urlWithToken = `${url}?token=${token}`;
  this.eventSource = new EventSource(urlWithToken);

  this.eventSource.addEventListener('update', (event: MessageEvent) => {
    onMessage(event.data);
  });

  this.eventSource.onerror = (error) => {
    console.error('SSE Error:', error);
    this.eventSource?.close();
  };
}

  disconnect(): void {
    this.eventSource?.close();
  }
}