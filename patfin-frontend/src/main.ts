import { bootstrapApplication } from '@angular/platform-browser';
import { registerLocaleData } from '@angular/common';
import localePl from '@angular/common/locales/pl';
import { appConfig } from './app/app.config';
import { App } from './app/app';

// Rejestruj polską lokalizację
registerLocaleData(localePl);

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
