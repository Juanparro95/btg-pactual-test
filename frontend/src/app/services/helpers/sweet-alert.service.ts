import Swal from 'sweetalert2';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AlertService {

  // Alerta simple de error
  showError(title: string, message: string) {
    Swal.fire({
      title: title,
      text: message,
      icon: 'error',
      confirmButtonText: 'Ok',
      background: '#fff',
      confirmButtonColor: '#002855',
    });
  }

  // Alerta de éxito
  showSuccess(title: string, message: string) {
    Swal.fire({
      title: title,
      text: message,
      icon: 'success',
      confirmButtonText: 'Ok',
      background: '#fff',
      confirmButtonColor: '#002855',
    });
  }

  // Alerta de advertencia o confirmación
  showWarning(title: string, message: string) {
    return Swal.fire({
      title: title,
      text: message,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Si',
      background: '#fff',
      confirmButtonColor: '#002855',
    });
  }

  // Alerta de advertencia con opciones de notificación (email o SMS)
  showWarningWithNotificationOptions(title: string, message: string) {
    return Swal.fire({
      title: title,
      text: message,
      icon: 'warning',
      input: 'radio',
      inputOptions: {
        'email': 'Correo Electrónico',
        'sms': 'SMS'
      },
      inputValidator: (value) => {
        if (!value) {
          return 'Debe seleccionar un método de notificación';
        }
        return undefined;
      },
      showCancelButton: true,
      confirmButtonText: 'Suscribirse',
      cancelButtonText: 'Cancelar',
      background: '#fff',
      confirmButtonColor: '#003366',
      cancelButtonColor: '#FF0000',
    });
  }

  // Mostrar un loading mientras se espera
  showLoading(message: string) {
    Swal.fire({
      title: 'Por favor espera...',
      text: message,
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });
  }

  // Cerrar el loading manualmente cuando la operación se complete
  closeLoading() {
    Swal.close();
  }
}
