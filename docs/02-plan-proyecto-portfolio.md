# Plan de Proyecto

## 1. Título del Proyecto
**Plataforma Social de Portfolios v2.0**

## 2. Objetivo
Planificar y ejecutar el desarrollo de una plataforma social de portfolios cumpliendo requisitos académicos y aplicando buenas prácticas de desarrollo profesional.

## 3. Cronograma

El proyecto se desarrolla en **3 fases** durante 1 mes:

### Fase 1: Desarrollo (10 días)
- Diseño de arquitectura MVC y modelo de datos
- Implementación de entidades JPA y repositorios
- Desarrollo de servicios con lógica de negocio
- Configuración de Spring Security
- Implementación de controladores y vistas Thymeleaf
- Sistema de votación y favoritos
- Gestión de archivos (imágenes y CVs)
- Publicación en redes sociales

### Fase 2: Pruebas (15 días)
- Pruebas funcionales de registro y autenticación
- Validación de reglas de negocio (votación, permisos)
- Pruebas de integración de servicios
- Verificación de seguridad (autorización, CSRF)
- Testing de compatibilidad (navegadores, dispositivos)
- Optimización de rendimiento

### Fase 3: Despliegue (5 días)
- Configuración de entorno de producción
- Despliegue de aplicación
- Configuración de base de datos
- Verificación en producción
- Documentación final

## 4. Hitos

| Día | Hito | Entregable |
|-----|------|------------|
| 5 | Arquitectura completada | Modelo ER y UML |
| 10 | Backend funcional | Entidades, servicios, repositorios |
| 15 | Autenticación implementada | Login, registro, roles |
| 20 | Funcionalidades principales | CRUD proyectos, votación, favoritos |
| 25 | Testing completado | Casos de prueba ejecutados |
| 30 | Despliegue y documentación | Aplicación en producción |

## 5. Recursos

### 5.1. Tecnológicos
**Backend:**
- Java 17+
- Spring Boot 3.x (MVC, Security, Data JPA)
- PostgreSQL 15+
- Maven 3.x

**Frontend:**
- Thymeleaf
- Bootstrap 5
- JavaScript ES6
- Lightbox 2.11.4

**Herramientas:**
- Git/GitHub (control de versiones)
- IntelliJ IDEA (IDE)
- DBeaver (gestión BD)
- Postman (testing API)

### 5.2. Humanos
**Desarrollador Full-Stack**: Responsable de todas las fases del proyecto (análisis, diseño, desarrollo, testing, despliegue, documentación).

## 6. Indicadores de Éxito
- ✅ Plataforma funcional con todas las características especificadas
- ✅ Sistema de votación operativo sin duplicidad
- ✅ Autenticación y autorización correctas por roles
- ✅ Aplicación responsive en todos los dispositivos
- ✅ Cumplimiento de requisitos de seguridad
- ✅ Documentación técnica completa
- ✅ Despliegue exitoso en producción

## 7. Gestión de Riesgos

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|--------------|---------|------------|
| Retrasos en desarrollo | Media | Alto | Priorización de funcionalidades MVP |
| Problemas de seguridad | Baja | Crítico | Revisión de código y testing exhaustivo |
| Incompatibilidad de navegadores | Baja | Medio | Testing en múltiples navegadores |
| Problemas de rendimiento | Media | Alto | Optimización de consultas e índices |

## 8. Metodología
- **Desarrollo iterativo**: Implementación por funcionalidades
- **Control de versiones**: Git con commits frecuentes y descriptivos
- **Testing continuo**: Validación después de cada feature
- **Documentación progresiva**: Actualización en cada fase

