# Documento de Requisitos

## 1. Título del Proyecto
**Plataforma Social de Portfolios v2.0**

## 2. Introducción
Plataforma web que permite a múltiples usuarios registrarse, crear y gestionar portfolios profesionales, publicar proyectos y participar en un sistema de votación que genera un ranking público. El proyecto aplica buenas prácticas de desarrollo, arquitectura MVC y cumple con estándares de seguridad profesionales.

## 3. Objetivos

### 3.1. Objetivo General
Desarrollar una plataforma social de portfolios con arquitectura Spring Boot, persistencia en base de datos relacional y patrón MVC, permitiendo registro, publicación de proyectos, sistema de votación y ranking global.

### 3.2. Objetivos Específicos
- Implementar arquitectura MVC con separación clara de capas
- Desarrollar backend con Spring Boot siguiendo principios SOLID
- Implementar autenticación y autorización con control de acceso por roles
- Gestionar persistencia con JPA/Hibernate y PostgreSQL
- Implementar sistema de votación con restricción única por usuario/proyecto
- Aplicar control de versiones con Git/GitHub
- Garantizar seguridad: cifrado BCrypt y validación de datos
- Cumplir estándares de accesibilidad web (WCAG)

## 4. Requisitos Funcionales

### 4.1. Visualización de Portfolios y Proyectos
- Listado público de proyectos con paginación
- Vista detallada de proyectos con galería de imágenes
- Acceso a perfil público del autor
- Ranking global ordenado por votos

### 4.2. Gestión de Usuarios
- Registro con validación de email único
- Autenticación con Spring Security
- Gestión de perfil (datos personales y enlaces a RRSS)
- Roles: ADMIN y USER
- Estados: activo, bloqueado

### 4.3. Gestión de Proyectos
- CRUD completo con control de permisos
- Subida múltiple de imágenes (JPG, PNG, GIF, WEBP)
- Imagen principal/carátula
- Enlaces a repositorios y demos
- Tecnologías utilizadas

### 4.4. Sistema de Votación
- Un voto por usuario y proyecto (constraint BD)
- Posibilidad de quitar voto
- Actualización automática del contador
- Votación AJAX sin recarga

### 4.5. Sistema de Favoritos
- Marcar/desmarcar proyectos como favoritos
- Listado personal de favoritos
- Independiente del sistema de votación

### 4.6. Gestión de CV
- Subida de archivos (PDF, DOCX, TXT)
- Descarga protegida (solo propietario)
- Múltiples versiones por usuario
- Tamaño máximo: 10MB

### 4.7. Publicación en Redes Sociales
- Compartir en LinkedIn, Twitter, Facebook, GitHub
- Estados: pendiente, publicado, error
- Historial de publicaciones
- Reintentar publicaciones fallidas

### 4.8. Panel de Administración
- CRUD de usuarios
- Bloquear/desbloquear usuarios
- Moderación de proyectos
- Estadísticas globales

## 5. Requisitos No Funcionales

### 5.1. Seguridad
- Autenticación con Spring Security
- Cifrado de contraseñas con BCrypt (factor 12)
- Protección CSRF
- Validación de entrada en frontend y backend
- Prevención de SQL Injection (JPA/parametrizadas)

### 5.2. Rendimiento
- Tiempo de respuesta < 2 segundos
- Carga de ranking < 1 segundo
- Paginación en listados
- Optimización de consultas con índices

### 5.3. Compatibilidad
- Diseño responsive (móvil, tablet, escritorio)
- Navegadores: Chrome, Firefox, Edge, Safari
- Accesibilidad WCAG 2.1 nivel AA

### 5.4. Usabilidad
- Interfaz intuitiva y consistente
- Feedback visual en acciones
- Notificaciones toast para mensajes
- Validación en tiempo real en formularios

## 6. Restricciones
- Desarrollo individual
- Herramientas gratuitas o académicas
- Requisitos mínimos: MVC, Spring Boot, seguridad, persistencia
- Responsive obligatorio

## 7. Reglas de Negocio
- Un usuario no puede votar su propio proyecto
- Un usuario solo puede votar una vez por proyecto
- Solo el propietario puede editar su proyecto
- Los administradores pueden moderar cualquier contenido
- Email único por usuario
- Las contraseñas deben cumplir criterios mínimos de seguridad

