import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@app/environments/environment';
import { Transaction } from '@app/models/transaction.model';
import { map, Observable, switchMap } from 'rxjs';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private baseUrl = `${environment.apiUrl}/api/transactions`;  

  constructor(private http: HttpClient, private userService: UserService) {}

  // Obtener transacciones por fondo
  getTransactionsByFund(fundId: string): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.baseUrl}/fund/${fundId}`);
  }

  // Obtener transacciones por usuario
  getTransactionsByUser(emailUser: string): Observable<Transaction[]> {
    return this.userService.getUserByEmail(emailUser).pipe(
      switchMap((user) => {
        if (!user || !user.id) {
          throw new Error('User not found');
        }
        return this.http.get<Transaction[]>(`${this.baseUrl}/user/${user.id}`);
      })
    );
  }
}
