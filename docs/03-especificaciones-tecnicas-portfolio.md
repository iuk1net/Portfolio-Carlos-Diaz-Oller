# Especificaciones Técnicas del Proyecto: Portfolio

## 1. Arquitectura
La aplicación estará basada en una arquitectura **cliente-servidor** para una **plataforma social de portfolios**, con backend centralizado y persistencia en base de datos.

### Cliente
- Interfaz web responsiva accesible desde navegadores de escritorio, tablets y móviles.
- Vistas renderizadas en servidor con **Thymeleaf**, integradas con Spring Boot para mostrar contenido dinámico.
- Interactividad mediante **JavaScript** para acciones como votación, filtrado y navegación sin recarga completa cuando sea aplicable.

### Servidor
- Backend desarrollado en **Java** con **Spring Boot**, siguiendo el patrón **MVC**.
- Gestión de la lógica de negocio: registro/login, perfiles, publicación de proyectos, votos y ranking.
- Implementación de autenticación y control de acceso por roles (Admin / Usuario).
- Integración con una base de datos relacional **PostgreSQL** para almacenar usuarios, proyectos, votos y contenidos asociados.
- Gestión de archivos en servidor para CV e imágenes (subida/descarga) con validación de tipos.

## 2. Tecnologías seleccionadas

### Frontend
- Lenguajes: **HTML5**, **CSS3**, **JavaScript (ES6)**.
- Framework/Biblioteca: **Thymeleaf** para generar vistas dinámicas integradas con Spring Boot.
- Estilos: uso de **Bootstrap** para diseño responsivo y compatibilidad con dispositivos.

### Backend
- Lenguaje: **Java 11 o superior**.
- Framework principal: **Spring Boot** (Spring MVC y Spring Data JPA).
- Seguridad: **Spring Security** para autenticación y autorización.

### Base de datos
- Motor: **PostgreSQL**.
- ORM: **Hibernate**.

### Infraestructura
- Control de versiones: **Git** con repositorio en **GitHub**.
- Despliegue: servicios gratuitos o educativos en la nube.
- Contenedores: **Docker** (opcional).

## 3. Estándares

### Arquitectura MVC y rutas
- La comunicación entre frontend (Thymeleaf) y backend se realiza mediante **controladores Spring MVC**.
- Las rutas se diseñarán de forma consistente para cubrir: autenticación, perfil, gestión de proyectos, votación y ranking.

### Seguridad
- Autenticación y control de acceso mediante **Spring Security**.
- Cifrado de contraseñas con un algoritmo seguro (p. ej. **BCrypt**).
- Validación de datos de entrada para formularios y endpoints.
- Prevención de vulnerabilidades comunes: inyección SQL (mediante JPA/consultas parametrizadas) y XSS (escape de salida y validación).

### Desarrollo
- Convenciones de codificación: aplicar **Java Code Conventions**.
- Pruebas: pruebas unitarias e integración con **JUnit**.
- Control de versiones: commits frecuentes y descriptivos.

## 4. Interfaz de usuario

### Diseño web responsivo
- Adaptación a diferentes resoluciones y dispositivos.
- Navegación clara entre: ranking, proyectos, perfiles y administración.

### Componentes clave
- **Listado público de proyectos**: con imagen destacada, título, tecnologías y totalLikes.
- **Detalle de proyecto**: galería de imágenes, descripción completa, enlace web, y botón de votación.
- **Ranking global**: proyectos ordenados por `totalLikes` (descendente) con paginación.
- **Perfil de usuario público**: muestra nombre, email profesional, whatsapp, teléfono, enlaces a redes sociales (LinkedIn, GitHub, Twitter, etc.), y proyectos creados.
- **Formularios de registro/inicio de sesión**: con validación frontend y backend.
- **Panel de administración** (rol Admin): gestión de usuarios (bloquear, eliminar), moderación de proyectos, y estadísticas globales.

## 5. Seguridad

### Autenticación
- Inicio de sesión y control de sesión mediante Spring Security.
- Credenciales almacenadas con hash seguro.

### Autorización
- Acceso diferenciado por roles:
  - Usuario: gestión de su perfil y proyectos; votación; consulta del ranking.
  - Admin: gestión completa de usuarios y moderación de proyectos.

### Protección de datos
- Políticas básicas de privacidad sobre la información visible públicamente.
- Evitar exposición de datos sensibles de usuarios.

## 6. Escalabilidad y rendimiento

### Escalabilidad
- Diseño orientado a crecimiento moderado de usuarios y proyectos.
- Estructura de base de datos preparada para consultas de ranking.

### Optimización del rendimiento
- Consultas optimizadas para el ranking (ordenación por votos).
- Uso de paginación en listados.
- Optimización básica de recursos estáticos (CSS/JS) y control de peso de imágenes.

## 7. Indicadores de rendimiento (KPIs)
- Tiempo de carga de páginas: < 2 segundos en condiciones normales.
- Tiempo de respuesta en ranking/listados: < 1 segundo en escenarios habituales.
- Disponibilidad: > 95% (según limitaciones del despliegue).
- Consistencia de votos: sin duplicidad (un voto por usuario y proyecto).
- Compatibilidad y responsividad: correcto funcionamiento en ordenadores, tablets y móviles.
