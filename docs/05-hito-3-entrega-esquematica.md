# Hito 3 – Entrega Esquemática

**Proyecto:** Portfolio Carlos Díaz Oller  
**Objetivo:** Consolidar la arquitectura del sistema y documentar la lógica de datos y negocio del MVP.

## 1. Modelo Entidad-Relación (ER)

**Objetivo:** Representar entidades y relaciones de la base de datos del portfolio.

### Entidades principales

**Usuario (admin)**
- **Atributos:** id, nombre, contraseña, rol
- **Relaciones:** Usuario → Proyecto, CV, Mensaje de contacto
- **Cardinalidad:** 1:N

**Proyecto**
- **Atributos:** id, título, descripción, tecnologías
- **Relaciones:** Asociación con Usuario
- **Cardinalidad:** N:1

**CV (archivos)**
- **Atributos:** id, tipoArchivo, rutaArchivo
- **Relaciones:** Asociación con Usuario
- **Cardinalidad:** N:1

**Mensaje de contacto**
- **Atributos:** id, nombreRemitente, email, contenido
- **Relaciones:** Asociación con Usuario
- **Cardinalidad:** N:1

### Notas
- Cada entidad tiene su **clave primaria**; las relaciones se implementan mediante **claves foráneas**.
- La **cardinalidad** refleja la multiplicidad indicada.
- Este modelo ER sirve de guía para la implementación y mantenimiento de la base de datos del portfolio.

## 2. Diagrama de Clases UML

**Objetivo:** Mostrar la estructura de clases y la lógica de negocio del sistema.

### Clases principales

**Usuario**
- **Atributos:** id, nombre, contraseña, rol
- **Relaciones:** Usuario → Proyecto, CV, Mensaje
- **Métodos:** getters, setters, funciones CRUD básicas

**Proyecto**
- **Atributos:** id, título, descripción, tecnologías
- **Relaciones:** Asociación con Usuario
- **Métodos:** getters, setters, funciones CRUD básicas

**CV**
- **Atributos:** id, tipoArchivo, rutaArchivo
- **Relaciones:** Asociación con Usuario
- **Métodos:** getters, setters, funciones CRUD básicas

**Mensaje**
- **Atributos:** id, nombreRemitente, email, contenido
- **Relaciones:** Asociación con Usuario
- **Métodos:** getters, setters, funciones CRUD básicas

### Notas
- Se mantiene el patrón **MVC**.
- Las clases reflejan la estructura de la base de datos y permiten la manipulación segura de los datos.

## 3. JavaDoc

**Objetivo:** Documentar código fuente Java para facilitar comprensión y mantenimiento.

### Clases y componentes documentados

**Usuario**
- Función de la clase
- Descripción de atributos
- Propósito de métodos
- Ejemplo de uso

**Proyecto**
- Función de la clase
- Descripción de atributos
- Propósito de métodos
- Ejemplo de uso

**CV**
- Función de la clase
- Descripción de atributos
- Propósito de métodos
- Ejemplo de uso

**Mensaje**
- Función de la clase
- Descripción de atributos
- Propósito de métodos
- Ejemplo de uso

**Controladores / Servicios / Repositorios**
- Función de la clase
- Descripción de métodos
- Interacciones
- Ejemplo de uso

### Resultado
Permite a futuros desarrolladores comprender la lógica y mantener la aplicación sin inconsistencias.

## 4. Documentación de la API

**Objetivo:** Detallar los endpoints del backend para consumo seguro y eficiente de datos.

### Endpoints principales

**`/api/proyectos`**
- **Operaciones:** listar, crear, actualizar, eliminar
- **Entrada/Salida:** JSON
- **Códigos de estado:** 200 OK, 201 Created, 400 Bad Request, 401 Unauthorized, 404 Not Found

**`/api/cv`**
- **Operaciones:** subir y descargar archivos del CV
- **Entrada/Salida:** JSON
- **Códigos de estado:** 200 OK, 201 Created, 400 Bad Request, 401 Unauthorized, 404 Not Found

**`/api/mensajes`**
- **Operaciones:** recibir y listar mensajes
- **Entrada/Salida:** JSON
- **Códigos de estado:** 200 OK, 201 Created, 400 Bad Request, 401 Unauthorized, 404 Not Found

**`/api/usuarios`**
- **Operaciones:** login y gestión básica de usuarios (admin)
- **Entrada/Salida:** JSON
- **Códigos de estado:** 200 OK, 201 Created, 400 Bad Request, 401 Unauthorized, 404 Not Found

### Notas
- Documentación clara para integración y pruebas.
- Todos los endpoints siguen buenas prácticas REST y control de seguridad.

## 5. Resultados Esperados
- Visión integral de la arquitectura y base de datos del portfolio.
- Referencia clara para desarrollo, mantenimiento y posibles ampliaciones.
- Reducción de ambigüedades durante la implementación.
- Preparación completa para la siguiente fase de refinamiento y despliegue.

