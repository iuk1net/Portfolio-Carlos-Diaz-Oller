# Portfolio Carlos Díaz Oller

Portfolio web personal con backend (Spring Boot) y persistencia en base de datos, orientado a presentar mi perfil profesional, proyectos y tecnologías.

## 🎯 Estado del Proyecto
**Hito 4 Completado:** ✅ **APROBADO**  
**Versión:** 3.0.1  
**Fecha:** 06/02/2026

### 🚀 Novedad: LinkedIn Configurado
- ✅ Integración con LinkedIn completada
- ✅ Access Token obtenido y configurado
- ✅ Sistema listo para publicaciones reales en LinkedIn
- 📋 Próximo paso: Implementación del MVP (DÍA 1)

## 📚 Documentación
- **Índice completo**: [`docs/INDICE.md`](./docs/INDICE.md)
- **Documentación principal**: [`docs/DOCUMENTACION-PORTFOLIO.md`](./docs/DOCUMENTACION-PORTFOLIO.md)

### Documentos Técnicos
- Requisitos: [`docs/01-requisitos-portfolio.md`](./docs/01-requisitos-portfolio.md)
- Plan de proyecto: [`docs/02-plan-proyecto-portfolio.md`](./docs/02-plan-proyecto-portfolio.md)
- Especificaciones técnicas: [`docs/03-especificaciones-tecnicas-portfolio.md`](./docs/03-especificaciones-tecnicas-portfolio.md)
- Manual de desarrollo: [`docs/04-manual-desarrollo-portfolio.md`](./docs/04-manual-desarrollo-portfolio.md)
- **Guía de configuración y despliegue**: [`docs/05-guia-configuracion-despliegue.md`](./docs/05-guia-configuracion-despliegue.md)
- Modelo de datos: [`docs/06-modelo-datos-completo.md`](./docs/06-modelo-datos-completo.md)
- Changelog: [`docs/CHANGELOG.md`](./docs/CHANGELOG.md)

## 🎯 Objetivo
Presentar de forma clara y profesional mi perfil como desarrollador, demostrando capacidad para diseñar, desarrollar y desplegar una aplicación web completa siguiendo MVC, buenas prácticas y seguridad.

## 🚀 Características Principales
- ✅ Sistema de galería de imágenes con subida múltiple
- ✅ Votación AJAX sin recarga de página
- ✅ Sistema de favoritos independiente
- ✅ Publicación en redes sociales (LinkedIn, Twitter, Facebook)
- ✅ Gestión de CVs con múltiples formatos
- ✅ Ranking de proyectos con medallas visuales
- ✅ Autenticación y autorización con Spring Security
- ✅ CSRF habilitado (excepto APIs REST)
- ✅ Diseño responsive moderno

## 🛠️ Stack Tecnológico
- **Backend**: Java 17 + Spring Boot 3.4.1
- **Frontend**: Thymeleaf + Bootstrap 5 + JavaScript ES6
- **Base de Datos**: PostgreSQL 15+
- **Seguridad**: Spring Security + BCrypt + CSRF
- **Testing**: JUnit 5 + Spring Boot Test (44 tests, ~70% cobertura)

## 🧪 Testing y Calidad

### Tests Implementados
- ✅ **44 tests automatizados** con JUnit 5
- ✅ **Cobertura ~70%** en servicios críticos
- ✅ **0 fallos, 0 errores** - Build exitoso
- ✅ Validación de reglas de negocio
- ✅ Tests de integración con Spring Boot

```bash
# Ejecutar todos los tests
mvn test

# Ver resultados
# [INFO] Tests run: 44, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

**Servicios testeados:**
- VotoService (10 tests) - Sistema de votación
- ProyectoService (10 tests) - CRUD y permisos
- UsuarioService (12 tests) - Seguridad y gestión
- FavoritoService (11 tests) - Sistema de favoritos

## 📦 Instalación Rápida

### Requisitos Previos
- Java 17+
- PostgreSQL 15+
- Maven 3.6+

### Pasos Básicos

#### 1. Clonar y Preparar
```bash
git clone https://github.com/iuk1net/Portfolio-Carlos-Diaz-Oller.git
cd Portfolio-Carlos-Diaz-Oller
createdb portfolio
```

#### 2. Configurar Credenciales (Opcional)

**Sin configurar nada**, la aplicación usará valores por defecto:
- Usuario: `carlos` / Password: `postgre`
- Base de datos: `portfolio` en `localhost:5432`

**Para usar tus credenciales**, exporta las variables:
```bash
# Linux/Mac
export DB_USERNAME=tu_usuario
export DB_PASSWORD=tu_password

# Windows PowerShell
$env:DB_USERNAME="tu_usuario"
$env:DB_PASSWORD="tu_password"
```

#### 3. Ejecutar
```bash
./mvnw spring-boot:run
```

#### 4. Acceder
```
http://localhost:8089
```

### 📘 Documentación Completa

Para configuración avanzada, despliegue en producción, Docker, Nginx y más:
- **Guía completa**: [`docs/05-guia-configuracion-despliegue.md`](./docs/05-guia-configuracion-despliegue.md)
- **Manual de desarrollo**: [`docs/04-manual-desarrollo-portfolio.md`](./docs/04-manual-desarrollo-portfolio.md)

## 🔐 Seguridad

### Medidas Implementadas
- ✅ **CSRF habilitado** - Protección contra ataques Cross-Site Request Forgery
- ✅ **BCrypt** - Cifrado de contraseñas con factor 12
- ✅ **Variables de entorno** - Credenciales protegidas
- ✅ **Control de acceso por roles** - ADMIN y USER
- ✅ **Validaciones** - Frontend y Backend
- ✅ **SQL Injection** - Prevención mediante JPA parametrizado

### ⚠️ Antes de Producción

1. ✅ Configurar variables de entorno
2. ✅ Usar perfil `prod`: `--spring.profiles.active=prod`
3. ✅ Configurar HTTPS/SSL
4. ✅ Backup regular de la base de datos

📖 **Más información**: [`docs/05-guia-configuracion-despliegue.md`](./docs/05-guia-configuracion-despliegue.md)

## 📊 Métricas del Proyecto
- **Líneas de código**: ~5.000
- **Entidades**: 6 (Usuario, Proyecto, Voto, CV, PublicacionRRSS, Favorito)
- **Controllers**: 8
- **Services**: 7
- **Tests**: 44 (100% exitosos)
- **Cobertura**: ~70% en servicios críticos

## 🔗 Enlaces
- **Repositorio**: [GitHub](https://github.com/iuk1net/Portfolio-Carlos-Diaz-Oller)
- **Documentación completa**: [`docs/`](./docs/)
- **Diagramas**: [Modelo ER](./docs/Modelo%20Entidad%20Relacion.png) | [UML](./docs/UML.png)

