import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { AppRoutes } from './app/app.routes';
import { provideRouter } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { importProvidersFrom } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { environment } from './app/environments/environment';
import { appReducer } from '@app/store/app.reducer';
import { AppEffects } from '@app/store/app.effects';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(AppRoutes),
    importProvidersFrom(
      HttpClientModule,  
      StoreModule.forRoot({ app: appReducer }),
      EffectsModule.forRoot([AppEffects]),
      StoreDevtoolsModule.instrument({ maxAge: 25, logOnly: environment.production }),
    )
  ]
}).catch(err => console.error(err));
