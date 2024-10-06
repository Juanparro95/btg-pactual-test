import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, of, switchMap } from 'rxjs';
import { Fund } from '@app/models/fund.model';
import { UserService } from '@app/services/user.service';
import { environment } from '@app/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class FundService {
  private apiUrl = `${environment.apiUrl}/api/funds`;

  constructor(private http: HttpClient, private userService: UserService) {}

  getSubscribedFunds(emailUser: string): Observable<Fund[]> {
    return this.userService.getUserByEmail(emailUser).pipe(
      switchMap((user) => {
        if (!user || !user.id) {
          throw new Error('Usuario no encontrado');
        }
        return this.http.get<Fund[]>(`${this.apiUrl}/subscribed/${user.id}`);
      })
    );
  }

  getUserBalance(emailUser: string): Observable<number> {
    return this.userService.getUserByEmail(emailUser).pipe(
      switchMap((user) => {
        if (!user || !user.id) {
          throw new Error('Usuario no encontrado');
        }
        // Aquí deberías retornar el balance o total_transactions del usuario.
        return of(user.totalTransactions); // Suponiendo que totalTransactions contiene el balance
      })
    );
  }

  // Obtener todos los fondos
  getAllFunds(): Observable<Fund[]> {
    return this.http.get<Fund[]>(this.apiUrl);
  }

  // Suscribirse a un fondo
  subscribeToFund(fundId: number, emailUser: string, methodSelect: string): Observable<string> {
    return this.userService.getUserByEmail(emailUser).pipe(
      switchMap((user) => {
        if (!user || !user.id) {
          throw new Error('User not found');
        }
        return this.http.post<{ message: string }>(`${this.apiUrl}/subscribe/${fundId}/${user.id}`, { methodSelect: methodSelect})
          .pipe(
            map(response => response.message) 
          );
      })
    );
  }

  // Desuscribirse de un fondo
  unsubscribeToFund(fundId: number, emailUser: string): Observable<string> {
    return this.userService.getUserByEmail(emailUser).pipe(
      switchMap((user) => {
        if (!user || !user.id) {
          throw new Error('User not found');
        }
        return this.http.post<{ message: string }>(`${this.apiUrl}/unsubscribe/${fundId}/${user.id}`, {})
          .pipe(
            map(response => response.message)
          );
      })
    );
  }
}
