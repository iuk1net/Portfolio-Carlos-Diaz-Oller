# Especificaciones Técnicas

## 1. Arquitectura

### 1.1. Patrón Arquitectónico
**Modelo-Vista-Controlador (MVC)** implementado con Spring Boot:
- **Modelo**: Entidades JPA con anotaciones Hibernate
- **Vista**: Templates Thymeleaf con Bootstrap 5
- **Controlador**: Controladores Spring MVC con anotaciones REST

### 1.2. Arquitectura en Capas
```
┌─────────────────────────────────┐
│   Presentation Layer            │
│   (Controllers + Views)         │
├─────────────────────────────────┤
│   Business Logic Layer          │
│   (Services)                    │
├─────────────────────────────────┤
│   Data Access Layer             │
│   (Repositories + Entities)     │
├─────────────────────────────────┤
│   Database                      │
│   (PostgreSQL)                  │
└─────────────────────────────────┘
```

## 2. Stack Tecnológico

### 2.1. Backend
- **Framework**: Spring Boot 3.x
  - Spring MVC (controladores web)
  - Spring Security (autenticación/autorización)
  - Spring Data JPA (persistencia)
- **Lenguaje**: Java 17+
- **Build**: Maven 3.x
- **ORM**: Hibernate

### 2.2. Frontend
- **Template Engine**: Thymeleaf
- **Framework CSS**: Bootstrap 5
- **JavaScript**: ES6 (Fetch API, AJAX)
- **Librerías**: 
  - jQuery 3.6.0 (dependencia de Lightbox)
  - Lightbox 2.11.4 (galería de imágenes)

### 2.3. Base de Datos
- **SGBD**: PostgreSQL 15+
- **Driver**: PostgreSQL JDBC Driver
- **Gestión de esquema**: JPA/Hibernate DDL auto-update

## 3. Modelo de Datos

### 3.1. Entidades JPA

#### Usuario
```java
@Entity
@Table(name = "usuarios")
- Long id (PK)
- String nombre
- String email (UNIQUE)
- String contraseña (BCrypt)
- Rol rol (ENUM: ADMIN, USER)
- Estado estado (ENUM: ACTIVO, BLOQUEADO)
- String emailProfesional
- String whatsapp
- String telefono
- List<String> enlacesRRSS
- boolean emailVerificado (v2.6.0)
```

#### Proyecto
```java
@Entity
@Table(name = "proyectos")
- Long id (PK)
- String titulo
- String descripcion (TEXT)
- String tecnologias
- List<String> galeriaImagenes
- String enlaceWeb
- Integer totalLikes
- Usuario usuario (FK)
```

#### Voto
```java
@Entity
@Table(name = "votos")
@UniqueConstraint(columnNames = {"id_usuario", "id_proyecto"})
- Long id (PK)
- Usuario usuario (FK)
- Proyecto proyecto (FK)
- LocalDateTime fechaVoto
```

#### CV
```java
@Entity
@Table(name = "cvs")
- Long id (PK)
- String tipoArchivo
- String rutaServidor
- LocalDateTime fechaSubida
- Usuario usuario (FK)
```

#### PublicacionRRSS
```java
@Entity
@Table(name = "publicaciones_rrss")
- Long id (PK)
- Proyecto proyecto (FK)
- String redSocial
- LocalDateTime fechaPublicacion
- EstadoPublicacion estado (ENUM)
- String idExterno (v3.0.0)
- String urlPublicacion (v3.0.0)
- String mensajeError (v3.0.0)
```

#### Favorito
```java
@Entity
@Table(name = "favoritos")
- Long id (PK)
- Usuario usuario (FK)
- Proyecto proyecto (FK)
```

#### VerificacionEmail (v2.6.0)
```java
@Entity
@Table(name = "verificaciones_email")
- Long id (PK)
- Usuario usuario (FK, UNIQUE)
- String token (UNIQUE)
- LocalDateTime fechaCreacion
- LocalDateTime fechaExpiracion
- boolean usado
- TipoVerificacion tipo (ENUM: REGISTRO, RECUPERACION)
```

## 4. API REST

### 4.1. Endpoints Principales

#### Autenticación
- `GET /login` - Formulario de login
- `POST /login` - Procesar login
- `GET /register` - Formulario de registro
- `POST /register` - Procesar registro
- `POST /logout` - Cerrar sesión

#### Verificación de Email (v2.6.0)
- `GET /verificar-email?token={token}` - Verificar cuenta con token
- `GET /reenviar-verificacion` - Formulario reenviar verificación
- `POST /reenviar-verificacion` - Procesar reenvío
- `GET /solicitar-recuperacion` - Formulario recuperar contraseña
- `POST /solicitar-recuperacion` - Enviar email de recuperación
- `GET /recuperar-password?token={token}` - Formulario nueva contraseña
- `POST /recuperar-password` - Guardar nueva contraseña

#### Proyectos
- `GET /proyectos/lista` - Listar proyectos (público)
- `GET /proyectos/{id}` - Ver detalle (público)
- `GET /proyectos/nuevo` - Formulario nuevo (autenticado)
- `POST /proyectos` - Crear proyecto (autenticado)
- `GET /proyectos/{id}/editar` - Formulario editar (propietario)
- `POST /proyectos/{id}/editar` - Actualizar (propietario)
- `POST /proyectos/{id}/eliminar` - Eliminar (propietario/admin)
- `GET /proyectos/favoritos` - Mis favoritos (autenticado)

#### Votación
- `POST /api/votos/{proyectoId}/toggle` - Votar/quitar voto (AJAX)
- `GET /api/votos/{proyectoId}/verificar` - Verificar si votó (AJAX)

#### Galería de Imágenes
- `POST /api/proyectos/{id}/imagenes` - Subir imagen
- `DELETE /api/proyectos/{id}/imagenes/{index}` - Eliminar imagen
- `PUT /api/proyectos/{id}/imagenes/{index}/principal` - Establecer principal

#### CVs
- `GET /usuario/cvs` - Listar mis CVs
- `POST /usuario/cvs/subir` - Subir CV
- `GET /usuario/cvs/{id}/descargar` - Descargar CV
- `POST /usuario/cvs/{id}/eliminar` - Eliminar CV

#### Usuario
- `GET /usuario/perfil` - Ver mi perfil
- `GET /usuario/editar` - Formulario editar perfil
- `POST /usuario/editar` - Actualizar perfil
- `GET /usuario/{id}/publico` - Perfil público

#### Administración
- `GET /admin/dashboard` - Panel admin
- `GET /admin/usuarios` - Listar usuarios
- `POST /admin/usuarios/{id}/bloquear` - Bloquear usuario
- `POST /admin/usuarios/{id}/desbloquear` - Desbloquear usuario
- `POST /admin/usuarios/{id}/eliminar` - Eliminar usuario

## 5. Seguridad

### 5.1. Spring Security
```java
- Autenticación basada en formulario
- BCrypt password encoder (factor 12)
- Autorización por roles (@PreAuthorize)
- CSRF protection habilitado
- Session management
```

### 5.2. Control de Acceso
| Recurso | Público | USER | ADMIN |
|---------|---------|------|-------|
| `/proyectos/lista` | ✓ | ✓ | ✓ |
| `/proyectos/{id}` | ✓ | ✓ | ✓ |
| `/proyectos/nuevo` | ✗ | ✓ | ✓ |
| `/proyectos/{id}/editar` | ✗ | ✓ (propietario) | ✓ |
| `/proyectos/{id}/eliminar` | ✗ | ✓ (propietario) | ✓ |
| `/usuario/perfil` | ✗ | ✓ | ✓ |
| `/admin/**` | ✗ | ✗ | ✓ |

### 5.3. Validaciones
- **Backend**: Bean Validation (JSR-380)
- **Frontend**: HTML5 + JavaScript custom
- **Archivos**: Tipo, tamaño, extensión
- **SQL Injection**: Consultas parametrizadas JPA

## 6. Gestión de Archivos

### 6.1. Estructura de Directorios
```
uploads/
├── images/
│   └── {proyecto_id}/
│       ├── {timestamp}_1.jpg
│       ├── {timestamp}_2.png
│       └── ...
└── cvs/
    └── {usuario_id}/
        ├── {timestamp}.pdf
        ├── {timestamp}.docx
        └── ...
```

### 6.2. Configuración
- **Imágenes**: Máx 5MB, formatos: JPG, PNG, GIF, WEBP
- **CVs**: Máx 10MB, formatos: PDF, DOCX, TXT
- **Servir archivos**: `WebMvcConfig` con `ResourceHandler`

## 7. Rendimiento

### 7.1. Optimizaciones
- Índices en columnas de búsqueda frecuente
- Paginación en listados
- Lazy loading en relaciones JPA
- Cache de consultas frecuentes

### 7.2. Índices de Base de Datos
```sql
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_proyectos_totalLikes ON proyectos(totalLikes DESC);
CREATE INDEX idx_proyectos_usuario ON proyectos(id_usuario);
CREATE INDEX idx_votos_usuario ON votos(id_usuario);
CREATE INDEX idx_votos_proyecto ON votos(id_proyecto);
CREATE INDEX idx_favoritos_usuario ON favoritos(usuario_id);
```

## 8. Testing

### 8.1. Estrategia de Pruebas
- **Unitarias**: JUnit 5 para servicios
- **Integración**: Spring Boot Test
- **Funcionales**: Testing manual de endpoints
- **Seguridad**: Verificación de permisos

### 8.2. Cobertura Objetivo
- Servicios críticos: > 80%
- Controladores: > 60%
- Repositorios: Verificación de consultas custom

## 9. Configuración

### 9.1. application.properties
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/portfolio_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=50MB

# Custom
app.upload.images.dir=uploads/images
app.upload.cvs.dir=uploads/cvs

# Email Configuration (v2.6.0+)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
verificacion.email.expiracion-horas=24
verificacion.email.url-base=${VERIFICACION_URL_BASE:http://localhost:8080}

# LinkedIn Configuration (v3.0.0+)
linkedin.enabled=true
linkedin.test-mode=false
linkedin.access-token=${LINKEDIN_ACCESS_TOKEN:}
linkedin.publication-type=personal
```

## 10. Despliegue

### 10.1. Requisitos del Servidor
- Java 17+
- PostgreSQL 15+
- 512MB RAM mínimo
- 2GB espacio en disco

### 10.2. Variables de Entorno
```bash
DB_USERNAME=carlos
DB_PASSWORD=postgre
SERVER_PORT=8080
```

## 11. Estándares de Código

### 11.1. Convenciones
- **Clases**: PascalCase
- **Métodos/variables**: camelCase
- **Constantes**: UPPER_SNAKE_CASE
- **Paquetes**: lowercase

### 11.2. Documentación
- JavaDoc en clases y métodos públicos
- Comentarios para lógica compleja
- README actualizado

### 11.3. Git
- Commits descriptivos
- Branches por feature
- Pull requests para cambios importantes

