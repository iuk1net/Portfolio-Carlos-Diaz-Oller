# Documentación — Portfolio Carlos Díaz Oller (v2.0)

Este documento agrupa la documentación entregable del proyecto y enlaza a cada sección.

## Uso (repositorio público / LinkedIn)
- Este repositorio está pensado para ser **enlazado públicamente** (por ejemplo, desde LinkedIn).
- Evita incluir **credenciales** o **datos sensibles** en el código o en la documentación.

## Índice
1. [Documento de Requisitos](./01-requisitos-portfolio.md)
2. [Plan de Proyecto](./02-plan-proyecto-portfolio.md)
   - Hito 2: Entrega del primer prototipo (MVP) — ver sección **4.1**
3. [Especificaciones Técnicas](./03-especificaciones-tecnicas-portfolio.md) ⭐ **ACTUALIZADO**
   - Incluye API y Endpoints implementados
   - Servicios y métodos disponibles
4. [Manual de Desarrollo](./04-manual-desarrollo-portfolio.md)
5. [Hito 3 — Entrega Esquemática](./05-hito-3-entrega-esquematica.md)
6. [Modelo de Datos Completo](./06-modelo-datos-completo.md)
7. [Diagramas ER y UML (Mermaid)](./diagramas-mermaid.md)
8. [Registro de Cambios (Changelog)](./CHANGELOG.md)

## Diagramas
- [Modelo Entidad-Relación (ER) y Diagrama de Clases UML](./Modelo%20entidad%20relacion%20y%20UML.png)

## Estructura del Proyecto

### Entidades Principales
- **Usuario**: gestión de perfiles con datos de contacto y enlaces RRSS
- **Proyecto**: portfolios con galerías de imágenes y sistema de votación
- **CV**: almacenamiento de currículums en servidor
- **Voto**: sistema de likes con restricción única (un voto por usuario/proyecto)
- **PublicacionRRSS**: historial de publicaciones en redes sociales

### Entidades Complementarias
- **Favorito**: sistema de marcado de proyectos favoritos

### Tecnologías
- **Backend**: Java 17+ con Spring Boot 3.x
- **Frontend**: Thymeleaf + Bootstrap 5 + JavaScript
- **Base de Datos**: PostgreSQL 15+
- **Seguridad**: Spring Security con BCrypt

## Estado del Proyecto

✅ **Implementación completa y verificada**
- Modelo de datos: 100% implementado
- Repositorios: 6/6 funcionales
- Servicios: 6/6 con todos los métodos UML
- Controladores: Endpoints verificados
- Reglas de negocio: Todas implementadas

## Enlaces Útiles
- [Repositorio GitHub](https://github.com/iuk1net/Portfolio-Carlos-Diaz-Oller)
- [Tablero Trello](https://trello.com/b/TjZDy1Jx/portfolio-carlos-diaz-oller)
