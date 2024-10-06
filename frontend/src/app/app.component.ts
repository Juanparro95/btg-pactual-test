import { Component, OnInit, Inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common'; // Para verificar si estamos en el navegador
import { PLATFORM_ID } from '@angular/core';
import { AuthService } from './services/auth.service'; // Importa AuthService

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'funds-app';
  isLoggedIn: boolean = false;
  name: string = '';
  email: string = '';
  avatarUrl: string = '';

  constructor(private router: Router, private authService: AuthService) {}

   ngOnInit() {
    this.name = sessionStorage.getItem('name') || '';
    this.email = sessionStorage.getItem('email') || '';
    
    // Generar avatar dinámico basado en el nombre del usuario
    this.avatarUrl = this.generateAvatarUrl(this.name);

    this.authService.isLoggedIn$.subscribe((status) => {
      this.isLoggedIn = status;
    });

    if (!this.isLoggedIn) {
      this.router.navigate(['/auth']);
    }
  }

  // Función para generar el URL del avatar basado en el nombre del usuario
  generateAvatarUrl(name: string): string {
    const formattedName = encodeURIComponent(name.trim());
    return `https://ui-avatars.com/api/?name=${formattedName}&background=003366&color=fff&size=128`;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/auth']);
  }
}
