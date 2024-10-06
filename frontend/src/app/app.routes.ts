import { Routes } from '@angular/router';

export const AppRoutes: Routes = [
  { 
    path: '', 
    redirectTo: '/auth', 
    pathMatch: 'full' 
  },
  { 
    path: 'auth', 
    loadComponent: () => import('./components/auth/auth.component').then(m => m.AuthComponent) 
  },
  { 
    path: 'home', 
    loadComponent: () => import('./components/home/home.component').then(m => m.HomeComponent) 
  },
  { 
    path: 'funds', 
    loadComponent: () => import('./components/funds/funds.component').then(m => m.FundsComponent) 
  },
  { 
    path: 'transactions', 
    loadComponent: () => import('./components/transactions/transactions.component').then(m => m.TransactionsComponent) 
  }
];