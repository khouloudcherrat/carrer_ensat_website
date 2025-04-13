import { ApplicationConfig, provideZoneChangeDetection, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

import { routes } from './app.routes';
import { HTTP_INTERCEPTORS, provideHttpClient } from '@angular/common/http';
import { TokenInterceptor } from './auth/token.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(),
    importProvidersFrom(ReactiveFormsModule),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ]
};
