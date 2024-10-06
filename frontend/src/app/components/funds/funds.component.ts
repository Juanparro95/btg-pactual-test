import { Component, OnInit } from '@angular/core';
import { Fund } from '@app/models/fund.model';
import { UserService } from '@app/services/user.service';
import { FundService } from '@app/services/fund.service';
import { AlertService } from '@app/services/helpers/sweet-alert.service';
import { SweetAlertResult } from 'sweetalert2';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-funds',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './funds.component.html',
  styleUrl: './funds.component.scss'
})
export class FundsComponent implements OnInit {
  funds: Fund[] = [];
  subscribedFunds: Fund[] = [];
  clientBalance = 500000;
  emailUser = sessionStorage.getItem('email') || '';

  constructor(private fundService: FundService, private alertService: AlertService, private userService: UserService) {}

  ngOnInit(): void {
    this.loadFunds();
    this.loadUserBalance();
    this.loadSubscribedFunds();
  }

  loadUserBalance(): void {
    this.fundService.getUserBalance(this.emailUser).subscribe(
      (balance) => {
        this.clientBalance = balance;
      },
      (error) => {
        this.alertService.showError('Error', 'Error al cargar el balance del usuario');
      }
    );
  }

  markSubscribedFunds(): void {
    if (this.subscribedFunds.length > 0 && this.funds.length > 0) {
      this.funds = this.funds.map(fund => {
        const isSubscribed = this.subscribedFunds.some(subscribedFund => subscribedFund.id === fund.id);
        return { ...fund, isSubscribed };
      });
    }
  }  

  loadSubscribedFunds(): void {
    this.fundService.getSubscribedFunds(this.emailUser).subscribe(
      (funds) => {
        this.subscribedFunds = funds;
        this.markSubscribedFunds();
      },
      (error) => this.alertService.showError('Error', 'Error cargando los fondos suscritos')
    );
  }

  loadFunds(): void {
    this.fundService.getAllFunds().subscribe(
      funds => this.funds = funds,
      error => this.alertService.showError('Error', 'Error cargando los fondos')
    );
  }

  subscribe(fund: Fund): void {
    this.alertService.showWarningWithNotificationOptions('Confirmar Suscripción', `¿Estás seguro que deseas suscribirte al fondo ${fund.name}?`).then((result: SweetAlertResult) => {
      if (result.isConfirmed) {
        const selectedMethod = result.value;  // 'email' o 'sms'
  
        if (this.clientBalance >= fund.minimumAmount) {
          // Mostrar loading
          this.alertService.showLoading('Procesando tu suscripción...');
  
          this.fundService.subscribeToFund(fund.id, this.emailUser, selectedMethod).subscribe(
            response => {
              this.loadFunds();
              this.loadSubscribedFunds();
              this.loadUserBalance();
  
              // Cerrar el loading
              this.alertService.closeLoading();
  
              this.alertService.showSuccess('Suscripción Exitosa', `Te has suscrito al fondo: ${fund.name}. Notificación por: ${selectedMethod}`);
              this.clientBalance -= fund.minimumAmount;
            },
            error => {
              // Cerrar el loading
              this.alertService.closeLoading();
              this.alertService.showError('Error', 'No se pudo realizar la suscripción');
            }
          );
        } else {
          this.alertService.showWarning('Saldo Insuficiente', `No tiene saldo suficiente para suscribirse al fondo ${fund.name}`);
        }
      }
    });
  }
  
  unsubscribe(fund: Fund): void {
    this.alertService.showWarning('Confirmar Desuscripción', `¿Estás seguro que deseas desvincularte del fondo ${fund.name}?`).then((result: SweetAlertResult) => {
      if (result.isConfirmed) {
        // Mostrar loading
        this.alertService.showLoading('Procesando tu desuscripción...');
  
        this.fundService.unsubscribeToFund(fund.id, this.emailUser).subscribe(
          response => {
            this.loadFunds();
            this.loadSubscribedFunds();
            this.loadUserBalance();
  
            // Cerrar el loading
            this.alertService.closeLoading();
  
            this.alertService.showSuccess('Desuscripción Exitosa', `Te has desvinculado del fondo: ${fund.name}`);
            this.clientBalance += fund.minimumAmount;
          },
          error => {
            // Cerrar el loading
            this.alertService.closeLoading();
            this.alertService.showError('Error', 'No se pudo realizar la desuscripción');
          }
        );
      }
    });
  }
}