# CHANGELOG

Este archivo documenta de manera clara y ordenada las modificaciones y mejoras realizadas en el proyecto, facilitando el seguimiento del desarrollo, la identificación de errores y el historial de versiones.

## Propósito del registro de cambios
Documentar de manera clara y ordenada las modificaciones y mejoras realizadas en el proyecto.

## Formato del registro
Cada entrada del registro contiene:
- Fecha
- Versión (siguiendo versionado semántico: MAJOR.MINOR.PATCH)
- Cambios realizados (descripción breve)
- Autor

## Lineamientos
### Versionado semántico
- **MAJOR**: cambios importantes o que rompen compatibilidad con versiones anteriores.
- **MINOR**: nuevas funcionalidades que no afectan la compatibilidad.
- **PATCH**: correcciones de errores o mejoras menores.

### Buenas prácticas
- Escribir descripciones claras y precisas para cada cambio.
- Mantener el registro actualizado al finalizar cada iteración de desarrollo o mejora.
- Registrar siempre el autor del cambio.

## Herramientas recomendadas
- Control de versiones: Git (tags y commits)
- Automatización opcional: Conventional Commits, Release Drafter

---

## [2.1.3] - 2026-01-29
**Tipo:** PATCH - Optimización y corrección de bugs

**Cambios realizados:**
- 🔧 **Optimización VotoService**: Método privado `crearVotoInterno()` para evitar validación duplicada
- 🐛 **Corrección votacion.js**: Eliminados event listeners duplicados en vista detalle
  - Selector cambiado a `.vote-btn-ajax:not(#voteButton)`
  - Resuelto problema de 2 mensajes al votar en `/proyectos/{id}`

**Mejoras de performance:**
- ✅ Reducción de validaciones duplicadas en toggleVoto() (-50%)
- ✅ Un solo event listener por botón
- ✅ Mensajes únicos y claros

**Archivos modificados:**
- `VotoService.java` (+27 líneas)
- `votacion.js` (+1 línea)

**Autor:** Carlos Díaz Oller

## [2.1.2] - 2026-01-28
**Tipo:** PATCH - Corrección de bug en notificaciones

**Cambios realizados:**
- 🐛 **Corrección votación.js**: Eliminados mensajes duplicados al votar
  - Agregada verificación `response.ok` antes de procesar JSON
  - Mejora en manejo de errores HTTP (400, 500)
  - Ahora muestra un solo mensaje (éxito o error, no ambos)
- 🔒 **Validación reforzada en VotoService**: 
  - Validación de propietario en `toggleVoto()` para evitar procesamiento innecesario
  - Mantenida validación en `votar()` para llamadas directas desde otros endpoints

**Comportamiento corregido:**
- ✅ Votar proyecto ajeno: Solo mensaje "¡Voto registrado!"
- ✅ Quitar voto: Solo mensaje "Voto eliminado"
- ❌ Intentar votar propio proyecto: Solo mensaje "No puedes votar tu propio proyecto"

**Archivos modificados:**
- `votacion.js` (+4 líneas)
- `VotoService.java` (+4 líneas)

**Autor:** Carlos Díaz Oller

## [2.1.1] - 2026-01-28
**Tipo:** PATCH - Corrección de bug crítico

**Cambios realizados:**
- 🐛 **Corrección Sistema de Votación**: Corregido bug que permitía a usuarios votar sus propios proyectos.
  - Agregada validación en backend (`VotoService.votar()`) para rechazar votos propios
  - Modificado frontend en `proyectos/detalle.html` para mostrar mensaje informativo al propietario
  - Modificado frontend en `proyectos/lista.html` para ocultar botón de voto en proyectos propios
  - Mensaje de error claro: "No puedes votar tu propio proyecto"

**Reglas de negocio validadas:**
- ✅ Un usuario no puede votar dos veces el mismo proyecto
- ✅ **Un usuario no puede votar su propio proyecto** (NUEVO)
- ✅ Solo usuarios autenticados pueden votar
- ✅ El contador de votos se actualiza correctamente

**Impacto:**
- Seguridad mejorada: Validación en backend impide bypass desde API
- UX mejorada: Usuario ve por qué no puede votar
- Integridad de datos: Rankings ahora reflejan votos legítimos

**Archivos modificados:**
- `VotoService.java` (+4 líneas)
- `proyectos/detalle.html` (+8 líneas)
- `proyectos/lista.html` (+1 línea)

**Documentación:**
- Creado `CORRECCION-SISTEMA-VOTACION.md` con análisis completo

**Autor:** Carlos Díaz Oller

## [2.1.0] - 2026-01-28
**Cambios realizados:**
- ✅ **Gestión de CVs**: Sistema completo de subida, descarga y eliminación de currículums (PDF, DOCX, TXT).
- ✅ **Votación AJAX**: Sistema de votación sin recargar página con animaciones y notificaciones toast.
- ✅ **Perfil Público de Usuario**: Vista pública con estadísticas, proyectos y datos de contacto.
- ✅ **Edición de Perfil**: Formulario dinámico para actualizar datos personales y enlaces a redes sociales.
- ✅ **Publicación en RRSS**: Sistema para compartir proyectos en LinkedIn, Twitter, Facebook y GitHub con historial.
- ✅ **Ranking Visual**: Medallas animadas (🥇🥈🥉) para los top 3 proyectos más votados.
- ✅ **Galería de Imágenes**: Sistema completo de gestión de imágenes para proyectos:
  - Subida múltiple de imágenes (JPG, PNG, GIF, WEBP)
  - Primera imagen como imagen principal/carátula
  - Carrusel Bootstrap + Lightbox en vista de detalle
  - Imagen de portada en tarjetas de lista
  - Editor de galería en formulario de edición
  - Validaciones frontend y backend (máx 5MB por imagen)

**Mejoras técnicas:**
- Integración de Lightbox 2.11.4 para visualización de imágenes
- API REST completa para gestión de galería (`GaleriaImagenesController`)
- Configuración de archivos estáticos (`WebMvcConfig`)
- Redirección mejorada: crear proyecto → editar (permite agregar imágenes inmediatamente)

**Autor:** Carlos Díaz Oller

## [2.0.0] - 2026-01-20
**Cambios realizados:**
- Cambio de alcance del proyecto: de portfolio personal a **plataforma social de portfolios** (v2.0).
- Introducción de modelo **multiusuario** con roles (Admin/Usuario).
- Definición del sistema de **votación** (un voto por usuario y proyecto) y **ranking global**.
- Definición de la entidad **PublicacionRRSS** para el seguimiento de publicaciones en redes sociales.
- **Eliminación del sistema de mensajería / mensajes de contacto** de la arquitectura y de la documentación.
- Actualización completa de la documentación (requisitos, plan, especificaciones técnicas, manual, Hito 2 y Hito 3).

**Autor:** Carlos Díaz Oller

## [1.1.0] - 2026-01-20
**Cambios realizados:**
- Optimización del frontend con Thymeleaf para carga más rápida de contenidos.
- Corrección de errores menores de navegación y estilo.

**Autor:** Carlos Díaz Oller

## [1.0.1] - 2026-01-15
**Cambios realizados:**
- Añadida funcionalidad de descarga del CV en PDF y DOCX.
- Mejora del diseño responsivo y adaptación a móviles y tablets.

**Autor:** Carlos Díaz Oller

## [1.0.0] - 2026-01-10
**Cambios realizados:**
- Desarrollo inicial del portfolio.
- Implementación de la página de inicio y sección “Sobre mí”.
- Creación de la sección de proyectos con visualización básica.

**Autor:** Carlos Díaz Oller
