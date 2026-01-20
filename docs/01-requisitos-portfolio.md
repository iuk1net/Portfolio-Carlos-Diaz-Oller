# Documento de Requisitos del Proyecto: Portfolio
- El proyecto se desarrolla y mantiene de manera individual, por lo que todas las actualizaciones y mejoras serán gestionadas por el autor.
- La aplicación funcionará correctamente en los navegadores y dispositivos compatibles definidos en los requisitos.
- Los archivos del CV y los contenidos del portfolio estarán disponibles y actualizados por el administrador.
- Los visitantes tendrán acceso a una conexión a Internet estable para visualizar correctamente el portfolio.
## 7. Suposiciones iniciales

- Debe cumplir los requisitos mínimos obligatorios: **MVC**, **Spring Boot**, seguridad y persistencia en base de datos.
- Se usarán únicamente herramientas gratuitas o disponibles en el entorno académico.
- El proyecto se realiza de manera individual, gestionando frontend, backend, base de datos y documentación.
- El desarrollo se realizará en un plazo aproximado de 1 mes.
- La aplicación debe ser responsiva y funcionar correctamente en móviles, tablets y ordenadores.
## 6. Restricciones

- Uso de estándares **A11y** para accesibilidad: estructura semántica de HTML, contraste de colores, soporte para lectores de pantalla.
- Diseño responsivo y navegación intuitiva.
### 5.4. Usabilidad

- Compatible con navegadores modernos (Chrome, Firefox, Edge, Safari).
- Aplicación web accesible desde dispositivos móviles, tablets y ordenadores.
### 5.3. Compatibilidad

- Recuperación y carga eficiente de proyectos y archivos CV.
- Respuesta de la aplicación en menos de 2 segundos en condiciones normales.
### 5.2. Rendimiento

- Uso de HTTP y cifrado básico de contraseñas.
- Protección de rutas sensibles y validación de datos introducidos por el administrador.
- Autenticación y autorización para acceso al panel de administración.
### 5.1. Seguridad

## 5. Requisitos no funcionales

- Gestión del envío o almacenamiento de los mensajes recibidos mediante el backend.
- Validación de los datos introducidos antes de su envío.
- Formulario de contacto accesible desde la parte pública del portfolio.
### 4.5. Formulario de contacto

- Mantenimiento de la integridad de los datos en operaciones de creación, modificación o eliminación.
- Recuperación dinámica de datos para su correcta visualización en la parte pública del portfolio.
- Almacenamiento de la información del portfolio y de los archivos del CV en la base de datos o en un sistema de almacenamiento vinculado al backend.
### 4.4. Persistencia y gestión de datos

- Protección de rutas internas mediante roles y control de acceso basado en **Spring Security**.
- Acceso restringido a las funcionalidades de administración únicamente para usuarios autorizados.
- Inicio de sesión para el administrador mediante usuario y contraseña.
### 4.3. Autenticación y autorización

- Gestión de otros contenidos del portfolio, incluyendo archivos del CV disponibles para descarga.
- Eliminación de proyectos.
- Edición y actualización de proyectos existentes.
- Creación de nuevos proyectos desde el panel de administración.
- Panel de administración protegido mediante autenticación y autorización.
### 4.2. Gestión de proyectos y contenido

- Opción para descargar el **CV** en distintos formatos (PDF, DOCX, TXT), accesible públicamente.
- Secciones adicionales de habilidades técnicas y experiencia formativa.
- Sección de proyectos desarrollados con título, descripción, tecnologías utilizadas y enlaces asociados.
- Página principal con información general del perfil profesional (foto, datos personales y resumen).
### 4.1. Visualización del portfolio

## 4. Requisitos funcionales

- Desplegar la aplicación en un entorno online accesible públicamente, permitiendo su correcta visualización y uso desde Internet.
- Implementar al menos dos requisitos transversales optativos, como el cumplimiento de requisitos legales básicos de una web y la atención a criterios de accesibilidad.
- Garantizar un nivel adecuado de **seguridad** en la aplicación, validando los datos introducidos y protegiendo los accesos restringidos.
- Utilizar herramientas de planificación y organización del proyecto, como **Trello**, para la gestión de tareas y seguimiento del desarrollo.
- Aplicar buenas prácticas de desarrollo y control de versiones mediante el uso de **GitHub** y un repositorio remoto compartido.
- Incorporar un sistema de **persistencia** mediante base de datos relacional para la gestión dinámica de proyectos, tecnologías y contenidos del portfolio.
- Implementar un sistema de **autenticación y autorización** de usuarios que proteja el acceso a las funcionalidades de administración del portfolio.
- Desarrollar el backend de la aplicación utilizando **Spring Boot**, aplicando una arquitectura clara y mantenible.
- Diseñar e implementar una aplicación web siguiendo el patrón de diseño **MVC**, separando correctamente la lógica de negocio, la capa de presentación y el acceso a datos.
### 3.2. Objetivos específicos

El proyecto tiene como finalidad demostrar la capacidad para **diseñar, desarrollar y desplegar** una aplicación web completa, cumpliendo requisitos de **seguridad**, organización del código y buenas prácticas propias de un entorno profesional.

Desarrollar un **portfolio web personal** basado en una arquitectura backend con **Spring Boot** y persistencia en **base de datos**, siguiendo el patrón de diseño **MVC**, que permita presentar de forma profesional mi perfil como desarrollador de aplicaciones.
### 3.1. Objetivo general

## 3. Objetivos del proyecto

Además de servir como carta de presentación profesional, este proyecto tiene como finalidad aplicar **buenas prácticas** de análisis, diseño, desarrollo y documentación de software, simulando un entorno de trabajo real. De este modo, no solo se muestra el resultado visual final, sino también la correcta estructuración, mantenimiento y gestión de una aplicación web completa.

El uso de un backend y una base de datos permite gestionar de forma **dinámica** la información del portfolio, como los proyectos, las tecnologías o los contenidos mostrados.

A través de esta aplicación se muestran los conocimientos adquiridos durante la formación en **Desarrollo de Aplicaciones Multiplataforma (DAM)**, junto con los proyectos realizados, las tecnologías utilizadas y la experiencia práctica obtenida. La aplicación está pensada como una herramienta de presentación dirigida a **empresas y reclutadores del sector tecnológico**, permitiendo evaluar tanto mis competencias técnicas como mi capacidad para diseñar y desarrollar aplicaciones completas.

El proyecto **“Portfolio Carlos Díaz Oller”** consiste en el desarrollo de un **portfolio web personal** con **backend** y **base de datos**, cuyo objetivo es presentar de forma clara y profesional mi perfil como **desarrollador de aplicaciones**.
## 2. Introducción

**Portfolio Carlos Díaz Oller**
## 1. Título del proyecto


