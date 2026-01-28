# Manual de Desarrollo: Portfolio

## 1. Objetivo del manual de desarrollo
Proporcionar guías y procedimientos claros para el desarrollo de la **plataforma social de portfolios (v2.0)**, asegurando la calidad del código, la correcta organización de los archivos, la integridad de los datos y un despliegue eficiente de la aplicación.

## 2. Procedimientos

### 2.1. Creación de ramas
- Naming convention: las ramas deben seguir la estructura `tipo/descripcion`.
  - Ejemplos:
    - `feature/registro-usuarios`
    - `feature/sistema-votos`
    - `feature/ranking-global`
    - `bugfix/regla-un-voto-por-proyecto`
    - `hotfix/ajuste-seguridad`
- Flujo de trabajo:
  1. Crear una nueva rama a partir de `main` para trabajar en cada funcionalidad.
  2. Realizar commits frecuentes con mensajes claros y descriptivos para documentar los cambios.
  3. Fusionar la rama con `main` una vez completada y probada la funcionalidad.

### 2.2. Revisión de código
- Aunque es un proyecto individual, se recomienda revisar cada cambio antes de fusionarlo a `main`.
- Verificar que el código cumpla con los estándares de codificación y que la funcionalidad se comporte correctamente.
- Documentar cualquier ajuste o corrección en los commits para mantener un historial claro.

## 3. Estándares de codificación

### 3.1. Convenciones de nombres
- Clases: nombres en **PascalCase** (ej.: `UsuarioController`, `ProyectoController`, `VotoService`).
- Métodos y variables: **camelCase** (ej.: `registrarUsuario`, `listarRanking`, `darVoto`).
- Constantes: todo en mayúsculas con guiones bajos (ej.: `MAX_TAMANO_ARCHIVO`, `ROL_ADMIN`).

### 3.2. Estilo de código
- Formato:
  - Indentación de 4 espacios.
  - Líneas de máximo 120 caracteres.
- Comentarios:
  - Usar `//` para comentarios cortos.
  - Usar `/** */` para documentación extensa de clases y métodos.
- Buenas prácticas:
  - Evitar código duplicado, creando métodos reutilizables.
  - Utilizar nombres claros y descriptivos para clases, métodos y variables.
  - Mantener la organización del proyecto clara y coherente.

## 4. Uso de Git

### 4.1. Flujo de trabajo GitHub
- Rama principal (`main`): contiene siempre el código estable y probado.
- Ramas de características (`feature`): se crean para desarrollar nuevas funcionalidades o cambios específicos.
- Fusión de ramas: una vez que la funcionalidad está lista y probada, se fusiona a `main`.

### 4.2. Buenas prácticas en Git
- Realizar commits frecuentes con mensajes claros y descriptivos.
- Formato sugerido: `[tipo]: descripción breve`.
  - Ej.: `feat: añadir ranking global por votos`.
- Mantener el repositorio actualizado sincronizando `main` con las ramas de desarrollo antes de fusionar cambios.
- Usar tags para marcar versiones importantes del proyecto (ej.: `v2.0.0`, `v2.1.0`).

## 5. Resolución de conflictos

### 5.1. Estrategias
- Antes de fusionar:
  - Actualizar la rama local con los últimos cambios desde la rama de destino (e.g., `main` o `develop`).
  - Resolver los conflictos localmente antes de crear un PR.
- Durante la revisión:
  - Utilizar herramientas como las interfaces de GitHub o GitKraken para identificar y resolver conflictos.
- Buena práctica:
  - Comentar los cambios realizados para resolver conflictos, indicando el motivo de las decisiones tomadas.

## 6. Gestor de versiones y CI/CD

### 6.1. Control de versiones
- Utilizar etiquetas (tags) para identificar versiones significativas del proyecto (e.g., `v2.0.0`, `v2.0.1`).

### 6.2. Integración y despliegue continuo
- Configurar pipelines automáticos para pruebas y despliegue con GitHub Actions.
- Realizar despliegues en entornos de pruebas antes de pasar a producción.

## 7. Indicadores de calidad
- Cobertura de pruebas: mantener pruebas unitarias y de integración con una cobertura mínima del 80% en funcionalidades críticas (registro/login, votación y ranking).
- Conflictos de fusión: minimizar los conflictos al trabajar con ramas de funcionalidades, idealmente menos del 5%.
- Revisiones de código: asegurar que el código revisado cumpla con los estándares y no presente errores importantes antes de fusionar a `main`.
- Estabilidad de la aplicación: las funcionalidades principales (registro/login, gestión de proyectos, votación y ranking) deben funcionar correctamente en al menos el 95% de los casos de uso.

## 8. Gestión de Archivos y Galería de Imágenes

### 8.1. Galería de Imágenes en Proyectos

La aplicación permite a los usuarios subir múltiples imágenes para cada proyecto, con la primera imagen funcionando como **imagen principal o carátula**.

#### 8.1.1. Ubicación de la Funcionalidad

**NO disponible en `/proyectos/nuevo`**
- Razón: El proyecto debe existir en BD (tener ID) antes de asociarle imágenes.
- Flujo: Crear proyecto → Redirige a editar → Subir imágenes.

**Disponible en `/proyectos/{id}/editar`**
- Editor completo de galería después del formulario principal.
- Permite: subir, eliminar y establecer imagen principal.

**Visualización en `/proyectos/{id}`**
- Carrusel Bootstrap con navegación (anterior/siguiente).
- Lightbox 2.11.4 para ver imágenes en tamaño completo.
- Badge "⭐ Principal" en la primera imagen.

**Carátula en `/proyectos/lista`**
- La primera imagen aparece como portada de la tarjeta.
- Hover effect con zoom suave.

#### 8.1.2. Características Técnicas

**Formatos soportados**: JPG, JPEG, PNG, GIF, WEBP  
**Tamaño máximo**: 5 MB por imagen  
**Cantidad**: Ilimitada (recomendado: 3-10 imágenes)

**Validaciones**:
- Frontend: JavaScript valida tipo y tamaño antes de subir.
- Backend: Java valida extensión y tamaño en servidor.
- Feedback: Notificaciones toast informan resultados.

**Almacenamiento**:
```
uploads/images/
├── {proyecto_id}/
│   ├── {timestamp}.jpg
│   ├── {timestamp}.png
│   └── ...
```

#### 8.1.3. Flujo de Trabajo

1. **Crear Proyecto**:
   - Usuario crea proyecto en `/proyectos/nuevo`
   - Sistema guarda proyecto y genera ID
   - Redirige automáticamente a `/proyectos/{id}/editar`

2. **Subir Imágenes**:
   - Usuario hace scroll hasta "Galería de Imágenes"
   - Click en "📤 Subir Imágenes"
   - Selecciona una o más imágenes (máx 5MB c/u)
   - Primera imagen seleccionada = automáticamente principal
   - Sistema valida y sube las imágenes
   - Muestra preview durante la carga

3. **Gestionar Galería**:
   - Cambiar principal: Click en "⭐ Principal" de cualquier imagen
   - Eliminar: Click en "🗑️ Eliminar" (con confirmación)
   - Las imágenes se actualizan automáticamente en lista y detalle

4. **Visualizar**:
   - En lista: Primera imagen como carátula de tarjeta
   - En detalle: Carrusel navegable + Lightbox para zoom
   - Click en imagen → Vista fullscreen con navegación

#### 8.1.4. Componentes Implementados

**Backend**:
- `GaleriaImagenesController`: API REST para CRUD de imágenes
- `WebMvcConfig`: Configuración para servir archivos estáticos
- Endpoints:
  - `POST /api/proyectos/{id}/imagenes` - Subir imagen
  - `DELETE /api/proyectos/{id}/imagenes/{index}` - Eliminar
  - `PUT /api/proyectos/{id}/imagenes/{index}/principal` - Establecer principal

**Frontend**:
- `galeria.js`: Manager JavaScript para gestión
- Bootstrap 5 Carousel: Navegación de imágenes
- Lightbox 2.11.4: Visualización fullscreen
- jQuery 3.6.0: Requerido por Lightbox

### 8.2. Gestión de CVs

Los usuarios pueden subir múltiples versiones de su CV en formatos PDF, DOCX o TXT.

**Ubicación**: `/usuario/cv/lista`  
**Tamaño máximo**: 10 MB por archivo  
**Formatos permitidos**: PDF, DOCX, TXT

**Funcionalidades**:
- Subida con drag & drop o selección tradicional
- Descarga protegida (solo propietario)
- Eliminación segura
- Historial de versiones

**Almacenamiento**:
```
uploads/cvs/
├── {usuario_id}/
│   ├── {timestamp}.pdf
│   ├── {timestamp}.docx
│   └── ...
```


