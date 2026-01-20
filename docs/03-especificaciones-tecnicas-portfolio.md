# Especificaciones Técnicas del Proyecto: Portfolio

> Nota: El texto original incluía el encabezado “Red Social de Mascotas” en esta sección. En este documento se mantiene el contenido técnico, aplicado al proyecto **Portfolio Carlos Díaz Oller**.

## 1. Arquitectura
El portfolio estará basado en una arquitectura **cliente-servidor**, adaptada a un proyecto individual:

### Cliente
- Interfaz web responsiva accesible desde navegadores de escritorio, tablets y móviles.
- Desarrollo de las vistas usando **Thymeleaf**, integrado con Spring Boot para mostrar contenido dinámico.
- Interacción con el backend mediante peticiones HTTP gestionadas internamente por **Spring MVC**.

### Servidor
- Backend desarrollado en Java con **Spring Boot**, siguiendo el patrón MVC.
- Gestión de la lógica de negocio, seguridad y acceso a la base de datos.
- Implementación de un panel de administración para gestionar proyectos y archivos del CV.
- Integración con una base de datos relacional (MySQL o PostgreSQL) para almacenar los proyectos, información del portfolio y archivos del CV.
- Implementación de autenticación y control de acceso para proteger el panel de administración.

## 2. Tecnologías seleccionadas

### Frontend
- Lenguajes: **HTML5**, **CSS3**, **JavaScript (ES6)**.
- Framework/Biblioteca: **Thymeleaf** para generar vistas dinámicas integradas con Spring Boot.
- Herramientas adicionales: posible uso de **Bootstrap** para diseño responsivo y compatibilidad con dispositivos móviles.

### Backend
- Lenguaje: **Java 11 o superior**.
- Framework principal: **Spring Boot** (incluyendo Spring MVC y Spring Data JPA).
- Seguridad: **Spring Security** para la autenticación y autorización en el panel de administración.

### Base de datos
- Motor: **PostgreSQL**.
- ORM: **Hibernate** para la persistencia de datos y mapeo objeto-relacional.

### Infraestructura
- Servidor: despliegue en servicios gratuitos o educativos en la nube (Heroku, Vercel, etc.).
- Contenedores: opcionalmente **Docker** para empaquetar la aplicación.
- Control de versiones: **Git** con repositorio en **GitHub**.

## 3. Estándares

### Arquitectura MVC y rutas
Para este proyecto, la comunicación entre frontend (Thymeleaf) y backend se realiza mediante **controladores Spring MVC**, siguiendo buenas prácticas de nombres de métodos y rutas claras.

### Seguridad
- Autenticación y control de acceso: proteger el panel de administración mediante **Spring Security**.
- Protección de datos: uso de **HTTP** para las comunicaciones y manejo seguro de la información del portfolio y archivos del CV.
- Buenas prácticas: evitar inyecciones SQL y validar correctamente los datos ingresados por el usuario en el panel de administración.

### Desarrollo
- Convenciones de codificación: seguir las **Java Code Conventions** para mantener claridad y consistencia en clases, métodos y variables.
- Pruebas: implementar pruebas unitarias y básicas de integración con **JUnit** para verificar que la lógica del backend funciona correctamente.
- Control de versiones y CI/CD: usar Git/GitHub para control de versiones. Integración continua opcional mediante GitHub Actions para automatizar pruebas y despliegue.

## 4. Interfaz de usuario

### Diseño web responsivo
- La web se adaptará a diferentes resoluciones y dispositivos (móviles, tablets y ordenadores).
- Se aplicarán principios básicos de UX/UI para garantizar una navegación intuitiva y clara.
- Uso de Thymeleaf para renderizado dinámico y Bootstrap para facilitar el diseño responsivo.

### Componentes clave
- Página de inicio: presentación del autor, breve descripción y enlaces a proyectos destacados.
- Sección de proyectos: visualización de los proyectos realizados, con descripciones, tecnologías usadas y capturas.
- Descarga del CV: botones para descargar el CV en distintos formatos (PDF, DOCX, etc.).
- Panel de administración: acceso protegido para gestionar los proyectos, actualizar contenidos y archivos del CV.

## 5. Seguridad

### Autenticación
- El panel de administración estará protegido mediante inicio de sesión con usuario y contraseña cifrada usando **BCrypt**.
- Solo el autor tendrá acceso al panel para gestionar proyectos y archivos del CV.

### Autorización
- Control de acceso limitado al rol de administrador (el propio autor).
- Restricción de acciones sensibles, como modificar o eliminar proyectos y archivos del CV, exclusivamente al administrador.

### Protección de datos
- Manejo seguro de los datos del portfolio y archivos del CV.
- Políticas básicas de privacidad sobre la información visible públicamente.
- Copias de seguridad periódicas de la base de datos.

### Seguridad de la aplicación
- Prevención de ataques comunes como inyección SQL y Cross-Site Scripting (XSS).
- Validación de los datos ingresados en el panel de administración.

## 6. Escalabilidad y rendimiento

### Escalabilidad horizontal
- La aplicación está diseñada para soportar crecimiento moderado, aunque el tráfico esperado será bajo.
- La estructura del backend y la base de datos permite añadir nuevos proyectos o secciones sin modificar la arquitectura básica.

### Optimización del rendimiento
- Consultas a la base de datos optimizadas para carga rápida de los proyectos y archivos del CV.
- Uso de caching básico en memoria para mejorar la respuesta de páginas consultadas frecuentemente.
- La interfaz web está optimizada para cargar rápido, incluso en dispositivos móviles.

## 7. Indicadores de rendimiento (KPIs)
- Tiempo de carga de páginas: menor a 2 segundos en condiciones normales de uso.
- Tiempo de respuesta del backend: menor a 500 ms para consultas básicas y carga de proyectos.
- Disponibilidad de la web: accesible > 95% del tiempo, considerando despliegue en servicios gratuitos en la nube.
- Velocidad de descarga del CV: archivos descargables en menos de 3 segundos.
- Compatibilidad y responsividad: la web debe mostrarse correctamente en ordenadores, tablets y móviles.

