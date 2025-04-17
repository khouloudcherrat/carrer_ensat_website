import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SseService {
  private eventSource: EventSource | null = null;

  connect(url: string, onMessage: (data: any) => void): void {
    this.eventSource = new EventSource(url);
  
    this.eventSource.addEventListener('update', (event: MessageEvent) => {
      onMessage(event.data); // or JSON.parse(event.data) if needed
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