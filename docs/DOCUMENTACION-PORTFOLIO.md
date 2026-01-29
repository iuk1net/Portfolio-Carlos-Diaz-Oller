# Documentación — Portfolio Social v2.0

Plataforma social de portfolios desarrollada con Spring Boot, PostgreSQL y Thymeleaf.

## Índice
1. [Documento de Requisitos](./01-requisitos-portfolio.md)
2. [Plan de Proyecto](./02-plan-proyecto-portfolio.md)
3. [Especificaciones Técnicas](./03-especificaciones-tecnicas-portfolio.md)
4. [Manual de Desarrollo](./04-manual-desarrollo-portfolio.md)
5. [Modelo de Datos Completo](./06-modelo-datos-completo.md)
6. [Registro de Cambios (Changelog)](./CHANGELOG.md)

## Diagramas
- [Modelo Entidad-Relación (ER) y Diagrama de Clases UML](./Modelo%20entidad%20relacion%20y%20UML.png)

## Arquitectura del Sistema

### Stack Tecnológico
- **Backend**: Java 17+ con Spring Boot 3.x (MVC, Security, Data JPA)
- **Frontend**: Thymeleaf + Bootstrap 5 + JavaScript ES6
- **Base de Datos**: PostgreSQL 15+
- **Seguridad**: Spring Security con BCrypt

### Entidades Principales
- **Usuario**: gestión de perfiles con autenticación, roles (ADMIN/USER) y datos de contacto
- **Proyecto**: portfolios con galería de imágenes, sistema de votación y ranking
- **CV**: almacenamiento de currículums con soporte para PDF, DOCX y TXT
- **Voto**: sistema de likes con restricción única (un voto por usuario/proyecto)
- **PublicacionRRSS**: historial de publicaciones en redes sociales
- **Favorito**: sistema de marcado de proyectos favoritos

## Funcionalidades Implementadas

### Gestión de Usuarios
- Registro y autenticación con Spring Security
- Control de acceso por roles (ADMIN/USER)
- Perfil público con datos de contacto y enlaces a redes sociales
- Edición de perfil con validación de datos

### Gestión de Proyectos
- CRUD completo con control de permisos
- Galería de imágenes: subida múltiple, imagen principal, carrusel con Lightbox
- Publicación en redes sociales (LinkedIn, Twitter, Facebook, GitHub)
- Sistema de favoritos independiente del sistema de votación
- Filtrado y búsqueda en listados

### Sistema de Votación y Ranking
- Un voto por usuario y proyecto (constraint en BD)
- Votación AJAX sin recarga de página
- Contador en tiempo real con feedback visual
- Ranking global ordenado por totalLikes
- Medallas visuales para top 3 (🥇🥈🥉)

### Gestión de CVs
- Subida de múltiples versiones con drag & drop
- Formatos soportados: PDF, DOCX, TXT
- Descarga protegida (solo propietario)
- Validaciones de formato y tamaño (máx 10MB)

### Publicación en Redes Sociales
- Compartir proyectos con texto personalizado
- Estados: pendiente, publicado, error
- Historial de publicaciones
- Reintentar publicaciones fallidas

## Enlaces
- [Repositorio GitHub](https://github.com/iuk1net/Portfolio-Carlos-Diaz-Oller)
