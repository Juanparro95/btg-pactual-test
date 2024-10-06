# Plataforma de Gestión de Inversiones - BTG Pactual Fondos Voluntarios

Este proyecto es una plataforma desarrollada para la gestión de inversiones en fondos voluntarios de BTG Pactual. 

## Requisitos Previos

Antes de usar la plataforma, debes instalar las siguientes herramientas:

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
git clone https://github.com/Juanparro95/btg-pactual-test.git
cd btg-pactual-test
```

### 2. Crear Base de Datos en MongoDB

Inicia MongoDB y crea la base de datos llamada btgdb, si te pide crear una collection, escribe funds

```bash
mongo
use btgdb
```

## Backend

### 1. Configuración de Variables de Entorno

Asegúrate de configurar las siguientes variables de entorno para conectar con MongoDB y para el envío de correos electrónicos:

Ingresa a backend/src/main/resources/application.properties para ver la configuración

**NOTA:** He dejado una clave temporal para el envio de emails, se que es inseguro, pero es solo para este test.

### 2. Ejecutar el Proyecto

Una vez configures las variables de entorno, puedes construir y ejecutar la aplicación:

*Dentro de la carpeta backend ejecuta los siguientes comandos*

```bash
./mvnw clean install
./mvnw spring-boot:run
```

La aplicación backend estará disponible en http://localhost:8081.

Para ver la documentación de swagger, ingresa a: http://localhost:8081/swagger-ui/index.html

## Frontend

### 1. Instalar dependencias

```bash
cd frontend -- Primero ingresa a la carpeta
npm install -- Ejecuta este codigo para instalar las dependencias
ng serve -- Iniciar el proyecto de angular, por defecto el http://localhost:4200
```

# **Tener en cuenta**

Funcionalidades del Proyecto

### 1. Login (Sin Contraseña)
El login no requiere de una contraseña. El único propósito del login es validar al usuario y enviarle una notificación al correo electrónico registrado.

### 2. Envío de Correo Electrónico y Notificación SMS
Aunque no se envían SMS directamente, en el correo electrónico de confirmación se indicará si el usuario ha seleccionado el método de notificación por SMS o via correo electrónico

### 3. Gestión de Inversiones
Adquirir Fondos de Inversión: El usuario puede adquirir fondos que se adapten a sus objetivos financieros.
Historial de Transacciones: El usuario puede revisar su historial de inversiones y movimientos financieros.
