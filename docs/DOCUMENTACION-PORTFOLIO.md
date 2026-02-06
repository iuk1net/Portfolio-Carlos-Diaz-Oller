# Documentación — Portfolio Social v2.5.1

Plataforma social de portfolios desarrollada con Spring Boot, PostgreSQL y Thymeleaf.

**Versión:** 2.5.1  
**Fecha:** 03/02/2026  
**Estado:** ✅ Hito 4 Completado

## Índice
1. [Documento de Requisitos](./01-requisitos-portfolio.md)
2. [Plan de Proyecto](./02-plan-proyecto-portfolio.md)
3. [Especificaciones Técnicas](./03-especificaciones-tecnicas-portfolio.md)
4. [Manual de Desarrollo](./04-manual-desarrollo-portfolio.md)
5. [Guía de Configuración y Despliegue](./05-guia-configuracion-despliegue.md)
6. [Modelo de Datos Completo](./06-modelo-datos-completo.md)
7. [Registro de Cambios (Changelog)](./CHANGELOG.md)

## Diagramas
- [Modelo Entidad-Relación (ER)](./Modelo%20Entidad%20Relacion.png) - Diagrama visual del modelo de datos
- [Diagrama de Clases UML](./UML.png) - Arquitectura de clases del sistema

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

## Testing y Calidad

### Tests Automatizados
- **44 tests implementados** con JUnit 5 y Spring Boot Test
- **Cobertura ~70%** en servicios críticos
- **100% exitosos** - 0 fallos, 0 errores

#### Tests por Servicio
| Servicio | Tests | Cobertura |
|----------|-------|-----------|
| VotoService | 10 | ~100% |
| ProyectoService | 10 | ~80% |
| UsuarioService | 12 | ~90% |
| FavoritoService | 11 | ~100% |
| **TOTAL** | **44** | **~70%** |

### Validaciones
- ✅ Reglas de negocio verificadas automáticamente
- ✅ Un voto por usuario/proyecto
- ✅ No votar propio proyecto
- ✅ Email único en usuarios
- ✅ Contraseñas cifradas con BCrypt
- ✅ Permisos de edición/eliminación
- ✅ Sistema de favoritos independiente

## Enlaces
- [Repositorio GitHub](https://github.com/iuk1net/Portfolio-Carlos-Diaz-Oller)
