# Documento Plan de Proyecto: Portfolio

## 1. Título del proyecto
**Plataforma Social de Portfolios — Carlos Díaz Oller (v2.0)**

## 2. Objetivo del proyecto
Planificar y organizar el desarrollo de una **plataforma social de portfolios** con backend y base de datos, asegurando una ejecución eficiente dentro del tiempo disponible, cumpliendo los requisitos mínimos del curso y aplicando buenas prácticas de desarrollo profesional.

## 3. Cronograma del proyecto
El desarrollo del proyecto se realizará en **1 mes**, organizado en tres fases concentradas:

### Fase 1: Desarrollo (10 días)
- Definir la estructura de la plataforma: entidades principales (usuarios, proyectos, votos) y secciones de la interfaz.
- Diseñar la arquitectura MVC y planificar la base de datos para el modelo multiusuario.
- Implementar el backend con Spring Boot y la persistencia en base de datos relacional.
- Implementar el registro/inicio de sesión y la autorización por roles (Admin/Usuario).
- Desarrollar el listado público de proyectos y el ranking global por votos.
- Implementar la gestión de perfil (datos de contacto y enlaces a redes sociales).
- Implementar la gestión de proyectos por usuario (crear/editar/eliminar) y la gestión de imágenes/enlaces.
- Implementar la subida y descarga de CV.
- Implementar el formulario de contacto y la relación del mensaje con el usuario destinatario.

### Fase 2: Pruebas (15 días)
- Realizar pruebas funcionales de registro/login y flujos principales del usuario.
- Verificar autorización y control de acceso por rol (acciones de usuario y administración).
- Validar reglas del sistema de votación: un voto por usuario y proyecto, contabilización y retirada.
- Comprobar ranking global (ordenación por votos) y estabilidad en condiciones normales.
- Ajustar rendimiento y compatibilidad con dispositivos y navegadores.

### Fase 3: Despliegue (5 días)
- Desplegar la aplicación en un entorno online accesible públicamente.
- Revisar y finalizar la documentación para entrega.

## 4. Hitos del proyecto
- Día 5: Finalización del diseño de la arquitectura y del modelo de datos multiusuario.
- Día 15: Desarrollo del backend y base de datos con registro/login y primeras vistas públicas.
- Día 22: Implementación del sistema de votación, ranking global y panel de administración básico.
- Día 30: Pruebas finales, corrección de errores, despliegue online y revisión de la documentación.

## 4.1. Hito 2: Entrega del primer prototipo (MVP) de la aplicación

### Introducción
En este segundo hito se realizará la presentación y entrega del **primer prototipo funcional** del proyecto **“Portfolio Carlos Díaz Oller (v2.0)”**, concebido como **Producto Mínimo Viable (MVP)**. El objetivo principal de esta fase es disponer de una versión inicial operativa que permita **validar la viabilidad técnica** de la plataforma social y recopilar **retroalimentación temprana** sobre su uso y comprensión.

### Alcance y objetivos de la entrega

#### Funcionalidades clave
El prototipo incluirá las funcionalidades imprescindibles definidas en los requisitos del proyecto para demostrar el valor principal de la plataforma:
- **Registro e inicio de sesión** de usuarios.
- **Gestión básica del perfil** del usuario (datos personales de contacto y enlaces a redes sociales).
- **Publicación y gestión de proyectos** por parte de cada usuario (crear, editar, eliminar), incluyendo tecnologías y enlace web.
- **Listado público de proyectos** y **ranking global** ordenado por número de votos.
- **Sistema de votación** (likes) con la regla de **un voto por usuario y proyecto**, con posibilidad de retirar el voto.
- **Mensajes de contacto** dirigidos al usuario propietario del portfolio.
- **Gestión de CV**: subida y descarga en formatos PDF, DOCX y TXT.
- **Acceso de administración** (rol Admin) para gestión de usuarios y moderación básica de proyectos.

#### Diseño preliminar
Se entregará un diseño preliminar de la interfaz basado en una estructura web **responsiva**, priorizando la claridad de navegación y la coherencia entre secciones. El prototipo incluirá las vistas mínimas para:
- navegación pública (listado de proyectos y ranking),
- navegación por portfolio de usuario,
- formularios de registro/login,
- y un acceso diferenciado para administración.

#### Pruebas y validaciones iniciales
Se realizarán pruebas funcionales básicas orientadas a detectar errores críticos y verificar:
- Registro/login y control de acceso por rol.
- Gestión de proyectos propios por usuario.
- Regla de votación (sin duplicidad) y actualización del ranking.
- Envío y recepción de mensajes de contacto por usuario destinatario.
- Subida/descarga de CV.

### Resultados esperados
- Mejor comprensión de los requerimientos reales del proyecto a partir de reacciones e impresiones de usuarios y partes interesadas.
- Documento de retroalimentación para ajustar alcance y priorizar mejoras.
- Validación de supuestos iniciales sobre dirección técnica y funcional del sistema.

## 5. Recursos del proyecto

### 5.1. Humanos
**Desarrollador/Autor**: Responsable de todo el desarrollo del proyecto, incluyendo frontend, backend, base de datos, seguridad, funcionalidades sociales, administración y documentación.

### 5.2. Tecnológicos
- Lenguajes: HTML, CSS, JavaScript, Java (Spring Boot)
- Frameworks/Bibliotecas:
  - Thymeleaf para el frontend y la integración con Spring Boot
  - Spring Boot para backend y lógica del sistema
  - Spring Security para autenticación y autorización
- Gestión y control de versiones: Git/GitHub
- Planificación: Trello
- Infraestructura: Servicios gratuitos de despliegue online (Heroku, Vercel, Netlify, etc.)

### 5.3. Económicos
Presupuesto: Limitado a recursos gratuitos y herramientas disponibles en el entorno académico.

## 6. Roles y responsabilidades
**Desarrollador / Autor (Carlos Díaz Oller)**:
- Diseñar y construir la interfaz de usuario (Thymeleaf, HTML, CSS y JavaScript).
- Implementar el backend con Spring Boot y la persistencia en base de datos.
- Diseñar e implementar el modelo multiusuario y el sistema de votación/ranking.
- Garantizar la seguridad, control de acceso por roles y protección de datos.
- Realizar pruebas funcionales, corregir errores y optimizar rendimiento.
- Gestionar planificación, cronograma y documentación final.

## 7. Indicadores de éxito
- Plataforma funcional con registro/login, proyectos y votación dentro del plazo de 1 mes.
- Ranking global operando correctamente y reflejando votos sin duplicidad.
- Acceso de administración operativo para gestión de usuarios y moderación básica.
- Aplicación responsiva y compatible con dispositivos móviles, tablets y ordenadores.
- Cumplimiento de requisitos de seguridad y control de acceso.
- Documentación completa actualizada a la versión 2.0.

## 8. Suposiciones y dependencias

### Suposiciones
- El despliegue contará con configuración segura y sin credenciales expuestas en el repositorio.
- Los usuarios dispondrán de conexión estable para usar la plataforma.

### Dependencias
- Disponibilidad de un entorno de pruebas local.
- Acceso a un servicio de base de datos PostgreSQL.
- Integración correcta de Spring Boot (MVC, Security, Data JPA) con Thymeleaf y la base de datos.
