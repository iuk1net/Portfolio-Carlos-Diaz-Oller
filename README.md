<p align="center">
  <h1 align="center">🎨 Portfolio Social - Carlos Díaz Oller</h1>
  <p align="center">
    <strong>Plataforma social de portfolios profesionales</strong><br>
    Desarrollada con Spring Boot, PostgreSQL y Thymeleaf
  </p>
  <p align="center">
    <img src="https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=openjdk" alt="Java 17+">
    <img src="https://img.shields.io/badge/Spring_Boot-3.4.1-green?style=flat-square&logo=springboot" alt="Spring Boot">
    <img src="https://img.shields.io/badge/PostgreSQL-15+-blue?style=flat-square&logo=postgresql" alt="PostgreSQL">
    <img src="https://img.shields.io/badge/Tests-44_passed-success?style=flat-square" alt="Tests">
    <img src="https://img.shields.io/badge/Cobertura-70%25-yellow?style=flat-square" alt="Cobertura">
  </p>
</p>

---

## 📋 Índice

- [Descripción](#-descripción)
- [Características](#-características)
- [Stack Tecnológico](#️-stack-tecnológico)
- [Instalación](#-instalación-rápida)
- [Testing](#-testing)
- [Documentación](#-documentación)
- [Seguridad](#-seguridad)
- [Autor](#-autor)

---

## 🎯 Descripción

**Portfolio Social** es una plataforma web completa que permite a los usuarios crear y gestionar portfolios profesionales, publicar proyectos y participar en un sistema de votación con ranking global.

El proyecto demuestra competencias en:
- ✅ Arquitectura MVC con Spring Boot
- ✅ Seguridad con Spring Security
- ✅ Persistencia con JPA/Hibernate
- ✅ Testing automatizado con JUnit 5
- ✅ Diseño responsive con Bootstrap 5

> **Estado:** ✅ Completado | **Versión:** 3.0.1 | **Fecha:** Febrero 2026

---

## 🚀 Características

### Gestión de Usuarios
| Funcionalidad | Estado |
|---------------|--------|
| Registro con verificación de email | ✅ |
| Recuperación de contraseña | ✅ |
| Autenticación con Spring Security | ✅ |
| Control de acceso por roles (ADMIN/USER) | ✅ |
| Perfil público personalizable | ✅ |

### Gestión de Proyectos
| Funcionalidad | Estado |
|---------------|--------|
| CRUD completo con permisos | ✅ |
| Galería de imágenes (subida múltiple) | ✅ |
| Sistema de votación AJAX | ✅ |
| Sistema de favoritos | ✅ |
| Ranking con medallas (🥇🥈🥉) | ✅ |

### Funcionalidades Adicionales
| Funcionalidad | Estado |
|---------------|--------|
| Compartir en redes sociales | ✅ |
| Gestión de CVs (PDF, DOCX, TXT) | ✅ |
| Diseño responsive | ✅ |
| Protección CSRF | ✅ |

---

## 🛠️ Stack Tecnológico

```
┌─────────────────────────────────────────────────────────┐
│                     FRONTEND                            │
│  Thymeleaf • Bootstrap 5 • JavaScript ES6 • Lightbox   │
├─────────────────────────────────────────────────────────┤
│                     BACKEND                             │
│  Java 17 • Spring Boot 3.4.1 • Spring Security • JPA   │
├─────────────────────────────────────────────────────────┤
│                    DATABASE                             │
│               PostgreSQL 15+                            │
├─────────────────────────────────────────────────────────┤
│                     TESTING                             │
│           JUnit 5 • Spring Boot Test                    │
└─────────────────────────────────────────────────────────┘
```

---

## 📦 Instalación Rápida

### Requisitos
- Java 17+
- PostgreSQL 15+
- Maven 3.6+

### Pasos

```bash
# 1. Clonar repositorio
git clone https://github.com/iuk1net/Portfolio-Carlos-Diaz-Oller.git
cd Portfolio-Carlos-Diaz-Oller

# 2. Crear base de datos
createdb portfolio

# 3. Configurar credenciales (opcional)
# Por defecto: usuario=carlos, password=postgre

# Windows PowerShell
$env:DB_USERNAME="tu_usuario"
$env:DB_PASSWORD="tu_password"

# Linux/Mac
export DB_USERNAME=tu_usuario
export DB_PASSWORD=tu_password

# 4. Ejecutar
./mvnw spring-boot:run

# 5. Acceder
# http://localhost:8080
```

---

## 🧪 Testing

```bash
# Ejecutar tests
mvn test
```

### Resultados
```
Tests run: 44, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

| Servicio | Tests | Cobertura |
|----------|-------|-----------|
| VotoService | 10 | ~100% |
| ProyectoService | 10 | ~80% |
| UsuarioService | 12 | ~90% |
| FavoritoService | 11 | ~100% |
| **TOTAL** | **44** | **~70%** |

---

## 📚 Documentación

| Documento | Descripción |
|-----------|-------------|
| [📘 Documentación Principal](./docs/DOCUMENTACION-PORTFOLIO.md) | Visión general del proyecto |
| [📋 Requisitos](./docs/01-requisitos-portfolio.md) | Requisitos funcionales y no funcionales |
| [⚙️ Especificaciones Técnicas](./docs/03-especificaciones-tecnicas-portfolio.md) | Arquitectura y diseño |
| [🔧 Manual de Desarrollo](./docs/04-manual-desarrollo-portfolio.md) | Guía para desarrolladores |
| [🚀 Guía de Despliegue](./docs/05-guia-configuracion-despliegue.md) | Configuración y producción |
| [🗄️ Modelo de Datos](./docs/06-modelo-datos-completo.md) | Entidades y relaciones |
| [📝 Changelog](./docs/CHANGELOG.md) | Historial de cambios |

### Diagramas
- [Modelo Entidad-Relación](./docs/Modelo%20Entidad%20Relacion.png)
- [Diagrama UML](./docs/UML.png)

---

## 🔐 Seguridad

| Medida | Implementación |
|--------|----------------|
| Autenticación | Spring Security |
| Contraseñas | BCrypt (factor 12) |
| CSRF | Habilitado (excepto APIs REST) |
| SQL Injection | JPA parametrizado |
| Validaciones | Frontend + Backend |
| Credenciales | Variables de entorno |

---

## 📊 Métricas

| Métrica | Valor |
|---------|-------|
| Líneas de código | ~6.000 |
| Entidades | 7 |
| Controllers | 11 |
| Services | 10 |
| Tests | 44 |
| Cobertura | ~70% |

---

## 👤 Autor

**Carlos Díaz Oller**

- 📧 Contacto disponible en la plataforma
- 🔗 [Repositorio GitHub](https://github.com/iuk1net/Portfolio-Carlos-Diaz-Oller)

---

<p align="center">
  <sub>Desarrollado como proyecto de la 2ª Evaluación — FEMPA 2026</sub>
</p>
