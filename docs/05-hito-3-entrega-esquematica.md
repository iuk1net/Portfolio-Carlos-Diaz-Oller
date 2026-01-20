# Hito 3 – Entrega Esquemática

**Proyecto:** Plataforma Social de Portfolios — Carlos Díaz Oller (v2.0)  
**Objetivo:** Consolidar la arquitectura del sistema y documentar la lógica de datos y negocio del MVP.

## 1. Modelo Entidad-Relación (ER)

**Objetivo:** Representar entidades y relaciones de la base de datos de la plataforma.

### Entidades principales

**Usuario**
- **Atributos:** id, nombre, contraseña, rol
- **Relaciones:** Usuario → Proyecto, CV, Mensaje, Voto
- **Cardinalidad:**
  - Usuario → Proyecto: 1:N
  - Usuario → CV: 1:N
  - Usuario → Mensaje: 1:N (como destinatario)
  - Usuario → Voto: 1:N

**Proyecto**
- **Atributos:** id, título, descripción, tecnologías
- **Relaciones:** Asociación con Usuario; relación con Voto
- **Cardinalidad:**
  - Proyecto → Usuario: N:1
  - Proyecto → Voto: 1:N

**CV (archivos)**
- **Atributos:** id, tipoArchivo, rutaArchivo
- **Relaciones:** Asociación con Usuario
- **Cardinalidad:** N:1

**Mensaje de contacto**
- **Atributos:** id, nombreRemitente, email, contenido
- **Relaciones:** Asociación con Usuario (destinatario)
- **Cardinalidad:** N:1

**Voto**
- **Atributos:** id, usuarioId (FK), proyectoId (FK), fechaVoto
- **Relaciones:** Asociación con Usuario y Proyecto
- **Cardinalidad:**
  - Voto → Usuario: N:1
  - Voto → Proyecto: N:1
- **Restricción:** un usuario solo puede votar una vez por proyecto (unicidad usuarioId + proyectoId).

### Notas
- Cada entidad tiene su **clave primaria**; las relaciones se implementan mediante **claves foráneas**.
- La cardinalidad refleja la multiplicidad indicada (1:N / N:1).
- Este modelo ER sirve de guía para la implementación y mantenimiento de la base de datos de la plataforma social.

## 2. Diagrama de Clases UML

**Objetivo:** Mostrar la estructura de clases y la lógica de negocio del sistema.

### Clases principales

**Usuario**
- **Atributos:** id, nombre, contraseña, rol
- **Relaciones:** Usuario → Proyecto, CV, Mensaje, Voto
- **Métodos:** getters, setters, funciones CRUD básicas

**Proyecto**
- **Atributos:** id, título, descripción, tecnologías
- **Relaciones:** Asociación con Usuario; agregación de votos
- **Métodos:** getters, setters, funciones CRUD básicas

**CV**
- **Atributos:** id, tipoArchivo, rutaArchivo
- **Relaciones:** Asociación con Usuario
- **Métodos:** getters, setters, funciones CRUD básicas

**Mensaje**
- **Atributos:** id, nombreRemitente, email, contenido
- **Relaciones:** Asociación con Usuario (destinatario)
- **Métodos:** getters, setters, funciones CRUD básicas

**Voto**
- **Atributos:** id, usuarioId, proyectoId, fechaVoto
- **Relaciones:** Asociación con Usuario y Proyecto
- **Métodos:** getters, setters, funciones CRUD básicas

### Notas
- Se mantiene el patrón **MVC**.
- Las clases reflejan la estructura de la base de datos y permiten la manipulación segura de los datos.
- La regla de negocio **“un voto por usuario y proyecto”** se refleja en la persistencia (restricción de unicidad) y en la lógica de negocio.

## 3. JavaDoc

**Objetivo:** Documentar código fuente Java para facilitar comprensión y mantenimiento.

### Clases y componentes documentados

**Usuario / Proyecto / CV / Mensaje / Voto**
- Función de la clase
- Descripción de atributos
- Propósito de métodos
- Ejemplo de uso (cuando aplique)

**Controladores / Servicios / Repositorios**
- Función de la clase
- Descripción de métodos
- Interacciones

### Resultado
Permite a futuros desarrolladores comprender la lógica y mantener la aplicación sin inconsistencias.

## 4. Documentación de la API

**Objetivo:** Detallar los endpoints del backend para consumo seguro y eficiente de datos.

### Endpoints principales

**`/api/proyectos`**
- **Operaciones:** listar, crear, actualizar, eliminar
- **Entrada/Salida:** JSON
- **Códigos de estado:** 200 OK, 201 Created, 400 Bad Request, 401 Unauthorized, 404 Not Found

**`/api/votos`**
- **Operaciones:** votar (crear), retirar voto (eliminar), consultar votos
- **Entrada/Salida:** JSON
- **Códigos de estado:** 200 OK, 201 Created, 400 Bad Request, 401 Unauthorized, 404 Not Found

**`/api/ranking`**
- **Operaciones:** listar ranking global de proyectos
- **Entrada/Salida:** JSON
- **Códigos de estado:** 200 OK, 400 Bad Request

**`/api/cv`**
- **Operaciones:** subir y descargar archivos del CV
- **Entrada/Salida:** JSON
- **Códigos de estado:** 200 OK, 201 Created, 400 Bad Request, 401 Unauthorized, 404 Not Found

**`/api/mensajes`**
- **Operaciones:** recibir y listar mensajes
- **Entrada/Salida:** JSON
- **Códigos de estado:** 200 OK, 201 Created, 400 Bad Request, 401 Unauthorized, 404 Not Found

**`/api/usuarios`**
- **Operaciones:** registro, login y gestión básica de usuarios
- **Entrada/Salida:** JSON
- **Códigos de estado:** 200 OK, 201 Created, 400 Bad Request, 401 Unauthorized, 404 Not Found

### Notas
- Documentación clara para integración y pruebas.
- Los endpoints siguen buenas prácticas REST y control de seguridad.

## 5. Resultados Esperados
- Visión integral de la arquitectura y base de datos de la plataforma social.
- Referencia clara para desarrollo, mantenimiento y ampliaciones.
- Reducción de ambigüedades durante la implementación.
- Preparación para fases posteriores de refinamiento, pruebas y despliegue.
