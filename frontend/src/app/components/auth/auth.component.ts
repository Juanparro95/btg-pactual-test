import { Component } from '@angular/core';
import { User } from '@app/models/user.model';
import { AlertService } from '@app/services/helpers/sweet-alert.service';
import { UserService } from '@app/services/user.service';
import { AuthService } from '@app/services/auth.service'; // Import AuthService
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent {

  name: string = '';
  email: string = '';
  isSubmitting: boolean = false;  // Para evitar doble envío

  constructor(
    private userService: UserService,
    private router: Router,
    private alertService: AlertService,
    private authService: AuthService
  ) {}

  onSubmit() {
    if (!this.name || !this.email) {
      this.alertService.showError('Error', 'Por favor, ingrese su nombre y correo electrónico.');
      return;
    }

    // Evitar doble envío
    if (this.isSubmitting) {
      return;
    }

    this.isSubmitting = true;

    const user: User = { name: this.name, email: this.email, totalTransactions: 500000 };

    this.userService.getUserByEmail(this.email).subscribe({
      next: (existingUser) => {
        if (!existingUser) {
          // Si el usuario no existe, lo creamos
          this.userService.createUser(user).subscribe({
            next: (newUser) => {
              this.authService.login();
              this.storeUserInSession(newUser);
              this.alertService.showSuccess('Registro exitoso', 'Usuario creado y logueado correctamente.');
              this.isSubmitting = false;  // Habilitar envío nuevamente
            },
            error: (error) => {
              this.handleError(error);
            }
          });
        } else {
          // Si el usuario ya existe
          this.authService.login();
          this.storeUserInSession(existingUser);
          this.alertService.showSuccess('Bienvenido', 'Usuario logueado correctamente.');
          this.isSubmitting = false;
        }
      },
      error: (error) => {
        this.handleError(error);
      }
    });    
  }

  storeUserInSession(user: User) {
    sessionStorage.setItem('name', user.name);
    sessionStorage.setItem('email', user.email);
    this.router.navigate(['/home']);
  }

  handleError(error: any) {

    const user: User = { name: this.name, email: this.email, totalTransactions: 500000 };

    this.userService.createUser(user).subscribe({
      next: (newUser) => {
        this.authService.login();
        this.storeUserInSession(newUser);
        this.alertService.showSuccess('Registro exitoso', 'Usuario creado y logueado correctamente.');
        this.isSubmitting = false;  
      },
      error: (error) => {
        this.alertService.showError('Error', 'Error al crear el usuario.');
        this.isSubmitting = false;
      }
    });
    
  }
}
