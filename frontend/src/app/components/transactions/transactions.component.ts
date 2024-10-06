import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Transaction } from '@app/models/transaction.model';
import { TransactionService } from '@app/services/transaction.service';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './transactions.component.html',
  styleUrl: './transactions.component.scss'
})
export class TransactionsComponent {

  transactions: Transaction[] = [];
  displayedColumns: string[] = ['date', 'amount', 'type', 'fundName'];  
  emailUser = sessionStorage.getItem('email') || '';

  constructor(private transactionService: TransactionService) {}

  ngOnInit(): void {
    // Fetch transactions for logged-in user
    this.transactionService.getTransactionsByUser(this.emailUser).subscribe((data) => {
      this.transactions = data;
    });
  }

}
