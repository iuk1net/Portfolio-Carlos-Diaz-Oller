# Documento Plan de Proyecto: Portfolio

## 1. Título del proyecto
**Portfolio Carlos Díaz Oller**

## 2. Objetivo del proyecto
Planificar y organizar el desarrollo de un **portfolio web personal** con backend y base de datos, asegurando una ejecución eficiente dentro del tiempo disponible, cumpliendo los requisitos mínimos del curso y aplicando buenas prácticas de desarrollo profesional.

## 3. Cronograma del proyecto
El desarrollo del proyecto se realizará en **1 mes**, organizado en tres fases concentradas:

### Fase 1: Desarrollo (10 días)
- Definir la estructura del portfolio y secciones principales.
- Diseñar la arquitectura MVC y planificar la base de datos.
- Planificar la funcionalidad de descarga del CV y el panel de administración.
- Implementar el backend con Spring Boot y la persistencia en base de datos.
- Desarrollar la interfaz web responsiva y mostrar proyectos.
- Implementar el panel de administración para gestionar proyectos y archivos del CV.
- Integrar seguridad para acceso al panel de administración.
- Gestionar archivos del CV en distintos formatos para descarga.

### Fase 2: Pruebas (15 días)
- Realizar pruebas funcionales de todas las secciones y del panel de administración.
- Verificar seguridad, autenticación y control de acceso, corrigiendo errores.
- Ajustar rendimiento y compatibilidad con dispositivos y navegadores.

### Fase 3: Despliegue (5 días)
- Desplegar la aplicación en un entorno online accesible públicamente (Heroku, Vercel u otro).
- Revisar y finalizar la documentación para entrega.

## 4. Hitos del proyecto
- Día 5: Finalización del diseño de la arquitectura.
- Día 15: Desarrollo completo del backend, base de datos y primeras funcionalidades del frontend.
- Día 22: Integración del panel de administración, seguridad y funcionalidad de descarga del CV.
- Día 30: Pruebas finales, corrección de errores, despliegue online y revisión de la documentación.

## 4.1. Hito 2: Entrega del primer prototipo (MVP) de la aplicación

### Introducción
En este segundo hito se realizará la presentación y entrega del **primer prototipo funcional** del proyecto **“Portfolio Carlos Díaz Oller”**, concebido como **Producto Mínimo Viable (MVP)**. El objetivo principal de esta fase es disponer de una versión inicial operativa que permita **validar la viabilidad técnica** del portfolio web personal y recopilar **retroalimentación temprana** sobre su uso y comprensión.

### Alcance y objetivos de la entrega

#### Funcionalidades clave
El prototipo incluirá las funcionalidades imprescindibles definidas en los requisitos del proyecto para demostrar el valor principal del portfolio:
- **Visualización pública** del portfolio: página principal con información general del perfil profesional y acceso a las secciones del sitio.
- **Sección de proyectos** con visualización de título, descripción, tecnologías utilizadas y enlaces asociados.
- **Secciones adicionales** de habilidades técnicas y experiencia formativa.
- **Descarga pública del CV** en los formatos indicados (PDF, DOCX y TXT).
- **Formulario de contacto** accesible desde la parte pública del portfolio, con validación previa de los datos.
- **Acceso al panel de administración** protegido mediante autenticación y autorización.
- **Gestión de proyectos y contenidos** desde el panel de administración: creación, edición/actualización y eliminación de proyectos, así como gestión de otros contenidos del portfolio, incluyendo los archivos del CV disponibles para descarga.
- **Persistencia y recuperación dinámica de datos**, garantizando la integridad de la información en las operaciones de alta, modificación y eliminación.

#### Diseño preliminar
Se entregará un diseño preliminar de la interfaz basado en una estructura web **responsiva**, priorizando la claridad de navegación y la coherencia entre secciones. El prototipo presentará una organización mínima de las vistas y rutas necesarias para la consulta pública del portfolio y el acceso restringido al panel de administración.

#### Pruebas y validaciones iniciales
Se realizarán pruebas funcionales básicas orientadas a detectar errores críticos y verificar:
- La correcta visualización de las secciones públicas y la recuperación de la información.
- El acceso restringido al panel de administración mediante autenticación y autorización.
- La ejecución de operaciones de gestión de proyectos y contenidos, preservando la integridad de los datos.
- La validación de datos en el formulario de contacto.

### Resultados esperados
- Una mejor comprensión de los requerimientos reales del proyecto a partir de las reacciones e impresiones de usuarios y partes interesadas.
- Un documento de retroalimentación que permita ajustar el alcance y priorizar mejoras en fases posteriores.
- La validación de supuestos iniciales sobre la dirección técnica y funcional del proyecto, reduciendo riesgos a medio plazo.

## 5. Recursos del proyecto

### 5.1. Humanos
**Desarrollador/Autor**: Responsable de todo el desarrollo del proyecto, incluyendo frontend, backend, base de datos, seguridad, panel de administración, gestión de contenidos y documentación.

### 5.2. Tecnológicos
- Lenguajes: HTML, CSS, JavaScript, Java (Spring Boot)
- Frameworks/Bibliotecas:
  - Thymeleaf para el frontend y la integración con Spring Boot
  - Spring Boot para backend y lógica del sistema
- Gestión y control de versiones: Git/GitHub
- Planificación: Trello
- Infraestructura: Servicios gratuitos de despliegue online (Heroku, Vercel, Netlify, etc.)

### 5.3. Económicos
Presupuesto: Limitado a recursos gratuitos y herramientas disponibles en el entorno académico.

## 6. Roles y responsabilidades
**Desarrollador / Autor (Carlos Díaz Oller)**:
- Diseñar y construir la interfaz de usuario usando Thymeleaf, HTML, CSS y JavaScript.
- Implementar el backend con Spring Boot y gestionar la persistencia en la base de datos.
- Integrar la interfaz con la lógica del backend y asegurar la correcta gestión de archivos del CV.
- Garantizar la seguridad, control de acceso al panel de administración y protección de datos.
- Realizar pruebas funcionales, corregir errores y optimizar el rendimiento del portfolio.
- Gestionar la planificación del proyecto, el cronograma y la documentación final.

## 7. Indicadores de éxito
- Completar el portfolio funcional con backend, base de datos y frontend en Thymeleaf dentro del plazo de 1 mes.
- Implementar correctamente el panel de administración y la funcionalidad de descarga del CV en distintos formatos.
- Garantizar que la aplicación sea responsiva y compatible con dispositivos móviles, tablets y ordenadores.
- Cumplir con los requisitos de seguridad y control de acceso al panel de administración.
- Entregar la documentación completa, incluyendo diseño, arquitectura, cronograma y pruebas.

## 8. Suposiciones y dependencias

### Suposiciones
- El autor contará con acceso continuo a todas las herramientas necesarias para el desarrollo (IDE, GitHub, Trello, servicios en la nube).
- El desarrollo se realizará según el cronograma establecido para completar el proyecto en 1 mes.
- Los archivos del CV y los contenidos del portfolio estarán disponibles y actualizados por el autor.

### Dependencias
- Disponibilidad de un entorno de pruebas local para verificar el funcionamiento de la aplicación.
- Acceso a servicios en la nube gratuitos (Heroku, Vercel, etc.) para el despliegue del portfolio.
- Correcta integración de Spring Boot con Thymeleaf y la base de datos para la visualización dinámica de los contenidos.
