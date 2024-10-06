import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Store, select } from '@ngrx/store';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AppState } from '@store/app.state';
import { selectData } from '@store/app.state'; // Ajusta tu selector

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  
  constructor(private store: Store<AppState>, private router: Router) {}

  canActivate(): Observable<boolean> {
    return this.store.pipe(
      select(selectData), 
      map(data => {
        if (data && data.length > 0) {
          return true; 
        } else {
          return false; 
        }
      }),
      catchError(() => {
        this.router.navigate(['/error']); 
        return of(false);
      })
    );
  }
}
