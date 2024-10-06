import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
    private isLoggedInSubject = new BehaviorSubject<boolean>(false);
    public isLoggedIn$ = this.isLoggedInSubject.asObservable();
  
    constructor() {
      // Verifica si hay datos en sessionStorage al iniciar el servicio
      const name = sessionStorage.getItem('name');
      const email = sessionStorage.getItem('email');
      if (name && email) {
        this.isLoggedInSubject.next(true); // Si hay datos, establece el estado como autenticado
      } else {
        this.isLoggedInSubject.next(false);
      }
    }
  
    login() {
      this.isLoggedInSubject.next(true);
    }
  
    logout() {
      sessionStorage.removeItem('name');
      sessionStorage.removeItem('email');
      this.isLoggedInSubject.next(false);
    }
  
    isAuthenticated(): boolean {
      return this.isLoggedInSubject.value;
    }
  }