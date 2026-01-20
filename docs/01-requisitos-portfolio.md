# Documento de Requisitos del Proyecto: Portfolio

## 1. Título del proyecto
**Plataforma Social de Portfolios — Carlos Díaz Oller (v2.0)**

## 2. Introducción
El proyecto **“Portfolio Carlos Díaz Oller”** evoluciona a una **plataforma social de portfolios**, orientada a que **múltiples usuarios** puedan registrarse, crear y gestionar su propio portfolio, publicar proyectos y participar en un sistema de votación que genera un **ranking público** de proyectos.

La finalidad académica del proyecto es aplicar, en un contexto más cercano a un sistema real, buenas prácticas de análisis, diseño, desarrollo y documentación de software. Se desarrolla una aplicación web completa con backend y persistencia en base de datos, con requisitos de seguridad, organización del código y mantenimiento.

## 3. Objetivos del proyecto

### 3.1. Objetivo general
Desarrollar una **plataforma social de portfolios** basada en una arquitectura backend con **Spring Boot**, persistencia en **base de datos relacional** y patrón **MVC**, que permita a los usuarios registrarse, publicar proyectos, votar proyectos de otros usuarios y consultar un ranking global, cumpliendo requisitos de seguridad y buenas prácticas propias de un entorno profesional.

### 3.2. Objetivos específicos
- Diseñar e implementar la aplicación siguiendo el patrón **MVC**, separando correctamente lógica de negocio, presentación y acceso a datos.
- Desarrollar el backend con **Spring Boot**, con una arquitectura clara y mantenible.
- Implementar un sistema de **autenticación y autorización** para usuarios y administración, con control de acceso por roles.
- Incorporar persistencia en base de datos relacional para gestionar usuarios, portfolios, proyectos y votos.
- Implementar un sistema de **votación** (likes) sobre proyectos y un **ranking** público en función de las votaciones.
- Aplicar buenas prácticas de control de versiones con **Git/GitHub** y planificación con **Trello**.
- Garantizar un nivel adecuado de seguridad: cifrado de contraseñas, validación de datos y protección de accesos.
- Implementar requisitos transversales optativos: accesibilidad y requisitos legales básicos de una web.
- Desplegar la aplicación en un entorno online accesible públicamente.

## 4. Requisitos funcionales

### 4.1. Visualización de portfolios y proyectos
- Listado público de proyectos publicados en la plataforma.
- Acceso al portfolio completo del autor desde un proyecto.
- Visualización de información del usuario propietario del portfolio.
- Listado de proyectos ordenado por número de votos (ranking global).

### 4.2. Gestión de usuarios y perfil
- Registro de nuevos usuarios.
- Inicio de sesión y cierre de sesión.
- Gestión del perfil del usuario (datos de contacto y enlaces a redes sociales).
- Estados de usuario: activo, deshabilitado y bloqueado.

### 4.3. Gestión de proyectos (por usuario)
- Creación, edición y eliminación de proyectos propios.
- Inclusión de enlace web del proyecto.
- Gestión de galería de imágenes asociadas a proyectos.

### 4.4. Sistema de votación y ranking
- Los usuarios pueden votar/dar like a proyectos.
- Restricción: un usuario solo puede votar una vez por proyecto.
- Posibilidad de quitar el voto.
- Cálculo y visualización del total de votos por proyecto.

### 4.5. Gestión de CV
- Subida de archivos de CV al servidor.
- Descarga del CV.
- Soporte para múltiples formatos: PDF, DOCX y TXT.

### 4.6. Mensajes de contacto
- Formulario de contacto en cada portfolio.
- Envío de mensajes al propietario del portfolio.
- Bandeja/listado de mensajes recibidos para el usuario destinatario.

### 4.7. Panel de administración (rol Admin)
- CRUD completo de usuarios.
- Habilitar/deshabilitar/bloquear usuarios.
- Moderación de proyectos.
- Consulta de estadísticas globales básicas de la plataforma.

## 5. Requisitos no funcionales

### 5.1. Seguridad
- Autenticación y autorización con control de permisos por roles (Admin / Usuario).
- Cifrado de contraseñas con algoritmo seguro (p. ej. BCrypt).
- Protección de rutas sensibles.
- Validación de datos de entrada.

### 5.2. Rendimiento
- Respuesta de la aplicación en menos de 2 segundos en condiciones normales.
- Carga del ranking en menos de 1 segundo en escenarios de uso habitual.

### 5.3. Compatibilidad
- Aplicación web accesible desde móviles, tablets y ordenadores.
- Compatible con navegadores modernos (Chrome, Firefox, Edge, Safari).

### 5.4. Usabilidad
- Diseño responsivo y navegación intuitiva.
- Uso de estándares A11y: estructura semántica de HTML, contraste de colores y soporte para lectores de pantalla.

## 6. Restricciones
- La aplicación debe ser responsiva y funcionar correctamente en móviles, tablets y ordenadores.
- El proyecto se realiza de manera individual.
- Se usarán únicamente herramientas gratuitas o disponibles en el entorno académico.
- Debe cumplir requisitos mínimos obligatorios: **MVC**, **Spring Boot**, seguridad y persistencia en base de datos.

## 7. Suposiciones iniciales
- Los usuarios tendrán acceso a conexión a Internet estable.
- Los usuarios publicarán y mantendrán actualizados sus portfolios y proyectos.
- El sistema de votación se basará en la regla de **un voto por usuario y proyecto**.
- La aplicación funcionará en los navegadores y dispositivos compatibles definidos en los requisitos.
