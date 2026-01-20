# Manual de Desarrollo: Portfolio

## 1. Objetivo del manual de desarrollo
Proporcionar guías y procedimientos claros para el desarrollo del portfolio, asegurando la calidad del código, la correcta organización de los archivos, la integridad de los datos y un despliegue eficiente de la aplicación.

## 2. Procedimientos

### 2.1. Creación de ramas
- Naming convention: las ramas deben seguir la estructura `tipo/descripcion`.
  - Ejemplos:
    - `feature/añadir-proyecto`
    - `bugfix/corregir-error-descarga-cv`
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
- Clases: nombres en **PascalCase** (ej.: `ProyectoController`, `CVService`).
- Métodos y variables: **camelCase** (ej.: `obtenerProyectos`, `rutaArchivoCV`).
- Constantes: todo en mayúsculas con guiones bajos (ej.: `TIEMPO_MAXIMO_DESCARGA`).

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
- Ramas de características (`feature`): se crean para desarrollar nuevas funcionalidades o cambios específicos (ej.: `feature/añadir-proyecto`).
- Fusión de ramas: una vez que la funcionalidad está lista y probada, se fusiona a `main`.

### 4.2. Buenas prácticas en Git
- Realizar commits frecuentes con mensajes claros y descriptivos.
- Formato sugerido: `[tipo]: descripción breve`.
  - Ej.: `feat: añadir sección de descarga de CV`.
- Mantener el repositorio actualizado sincronizando `main` con las ramas de desarrollo antes de fusionar cambios.
- Usar tags para marcar versiones importantes del proyecto (ej.: `v1.0`, `v1.1`).

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
- Utilizar etiquetas (tags) para identificar versiones significativas del proyecto (e.g., `v1.0.0`, `v1.1.0`).

### 6.2. Integración y despliegue continuo
- Configurar pipelines automáticos para pruebas y despliegue con GitHub Actions.
- Realizar despliegues en entornos de pruebas antes de pasar a producción.

## 7. Indicadores de calidad
- Cobertura de pruebas: mantener pruebas unitarias y de integración con una cobertura mínima del 80% en las funcionalidades críticas (backend y lógica de descarga del CV).
- Conflictos de fusión: minimizar los conflictos al trabajar con ramas de funcionalidades, idealmente menos del 5%.
- Revisiones de código: aunque es un proyecto individual, asegurarse de que el código revisado cumpla con los estándares de codificación y no presente errores importantes antes de fusionar a `main`.
- Estabilidad de la aplicación: todas las funcionalidades visibles en el portfolio (proyectos, descarga de CV, panel de administración) deben funcionar correctamente en al menos 95% de los casos de uso.

