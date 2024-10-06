# Plataforma de Gestión de Inversiones - BTG Pactual Fondos Voluntarios

Este proyecto es una plataforma desarrollada para la gestión de inversiones en fondos voluntarios de BTG Pactual. 

## Requisitos Previos

Asegúrate de cumplir con los siguientes requisitos antes de empezar:

1. **Java JDK 17**  
   [Descargar JDK 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)

2. **Apache Maven**  
   Necesario para la gestión de dependencias y construcción del proyecto.  
   [Guía de instalación de Maven](https://maven.apache.org/install.html)

3. **MongoDB**  
   Base de datos utilizada por el proyecto.  
   [Instalar MongoDB](https://docs.mongodb.com/manual/installation/)

## Instalación y Configuración

Sigue estos pasos para instalar y ejecutar la aplicación:

### 1. Clonar el Repositorio

Clona el repositorio en tu máquina local:

```bash
git clone https://github.com/tuusuario/btg-fondos-voluntarios.git
cd btg-fondos-voluntarios
```

### 2. Crear Base de Datos en MongoDB

Inicia MongoDB y crea la base de datos llamada btgdb:

```bash
mongo
use btgdb
```

### 3. Configuración de Variables de Entorno

Asegúrate de configurar las siguientes variables de entorno para conectar con MongoDB y para el envío de correos electrónicos:

SPRING_DATASOURCE_URL: URL de conexión a MongoDB.
MAIL_HOST, MAIL_PORT, MAIL_USERNAME, MAIL_PASSWORD: Configuración del servidor SMTP.

```bash
export SPRING_DATASOURCE_URL=mongodb://localhost:27017/btg_fondos
export MAIL_HOST=smtp.example.com
export MAIL_PORT=587
export MAIL_USERNAME=tuemail@example.com
export MAIL_PASSWORD=tucontraseña
```

### 4. Ejecutar el Proyecto

Una vez configuradas las variables de entorno, puedes proceder a construir y ejecutar la aplicación:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

La aplicación backend estará disponible en http://localhost:8081.

Funcionalidades del Proyecto

### 1. Login (Sin Contraseña)
El login no requiere de una contraseña. El único propósito del login es validar al usuario y enviarle una notificación al correo electrónico registrado.

### 2. Envío de Correo Electrónico y Notificación SMS
Aunque no se envían SMS directamente, en el correo electrónico de confirmación se indicará si el usuario ha seleccionado el método de notificación por SMS o via correo electrónico

### 3. Gestión de Inversiones
Adquirir Fondos de Inversión: El usuario puede adquirir fondos que se adapten a sus objetivos financieros.
Historial de Transacciones: El usuario puede revisar su historial de inversiones y movimientos financieros.
