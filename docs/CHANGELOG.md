# CHANGELOG

Registro de cambios siguiendo versionado semántico (MAJOR.MINOR.PATCH).

---

## [2.2.1] - 2026-01-29
**Corrección crítica: Error en guardado de galería de imágenes**
- Corregido LazyInitializationException al acceder al usuario del proyecto
- Añadido método `findByIdWithUsuario()` con JOIN FETCH en ProyectoRepository
- Inicialización segura de lista `galeriaImagenes` cuando es null
- Uso consistente de email en verificaciones de permisos
- Simplificación de GaleriaImagenesController
- Mejor manejo de errores con mensajes descriptivos

---

## [2.2.0] - 2026-01-29
**Documentación profesionalizada**
- Reescritura completa de documentos con lenguaje técnico
- Verificación UML: 100% implementado (6 entidades, 28 métodos)
- Especificaciones técnicas actualizadas con arquitectura detallada
- Manual de desarrollo expandido con guías prácticas

---

## [2.1.3] - 2026-01-29
**Optimización y corrección de bugs**
- Optimización VotoService: eliminada validación duplicada
- Corrección votacion.js: eliminados event listeners duplicados
- Mejora de performance: reducción de validaciones duplicadas (-50%)

---

## [2.1.2] - 2026-01-28
**Corrección de notificaciones**
- Eliminados mensajes duplicados al votar
- Validación reforzada en VotoService
- Mejora en manejo de errores HTTP

---

## [2.1.1] - 2026-01-28
**Corrección de bug crítico**
- Corregido bug: usuarios podían votar sus propios proyectos
- Validación en backend y frontend
- Mejora de seguridad e integridad de datos

---

## [2.1.0] - 2026-01-28
**Nuevas funcionalidades principales**
- Sistema completo de gestión de CVs (PDF, DOCX, TXT)
- Votación AJAX sin recarga de página
- Perfil público de usuario con estadísticas
- Publicación en redes sociales con historial
- Ranking visual con medallas para top 3
- Galería de imágenes completa con Lightbox

---

## [2.0.0] - 2026-01-20
**Evolución a plataforma social**
- De portfolio personal a plataforma multiusuario
- Sistema de autenticación con roles (ADMIN/USER)
- Sistema de votación con constraint único
- Ranking global por totalLikes
- 6 entidades principales implementadas

---

## [1.1.0] - 2026-01-20
**Optimizaciones**
- Optimización del frontend con Thymeleaf
- Correcciones menores de navegación y estilo

---

## [1.0.1] - 2026-01-15
**Mejoras menores**
- Descarga de CV en PDF y DOCX
- Diseño responsivo mejorado

---

## [1.0.0] - 2026-01-10
**Lanzamiento inicial**
- Desarrollo inicial del portfolio personal

