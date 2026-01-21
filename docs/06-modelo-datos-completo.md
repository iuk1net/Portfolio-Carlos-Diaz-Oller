# Modelo de Datos Completo - Portfolio Social v2.0

**Proyecto:** Plataforma Social de Portfolios — Carlos Díaz Oller  
**Versión:** 2.0  
**Fecha:** Enero 2026

---

## Diagrama de Referencia
El modelo de datos se basa en el diagrama **Modelo Entidad-Relación (ER) y Diagrama de Clases UML** ubicado en:
- `docs/Modelo entidad relacion y UML.png`

---

## 1. Entidad: Usuario

### Descripción
Representa a los usuarios registrados en la plataforma, tanto usuarios estándar como administradores.

### Tabla en BD: `usuarios`

| Campo | Tipo | Constraints | Descripción |
|-------|------|-------------|-------------|
| `id_usuario` | INT | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `nombre` | VARCHAR(100) | NOT NULL | Nombre completo del usuario |
| `email` | VARCHAR(150) | NOT NULL, UNIQUE | Email de acceso (único) |
| `contraseña` | VARCHAR(255) | NOT NULL | Contraseña cifrada con BCrypt |
| `rol` | VARCHAR(20) | NOT NULL | Rol del usuario: 'admin' o 'user' |
| `estado` | VARCHAR(20) | NOT NULL | Estado: 'activo' o 'bloqueado' |
| `emailProfesional` | VARCHAR(150) | NULL | Email profesional para contacto público |
| `whatsapp` | VARCHAR(20) | NULL | Número de WhatsApp |
| `telefono` | VARCHAR(20) | NULL | Teléfono de contacto |
| `enlacesRRSS` | JSON | NULL | Array de enlaces a redes sociales |

### Relaciones
- **Usuario → Proyecto** (1:N): Un usuario puede crear múltiples proyectos
- **Usuario → CV** (1:N*): Un usuario puede subir múltiples versiones de CV
- **Usuario → Voto** (1:N): Un usuario puede emitir múltiples votos

### Reglas de Negocio
- El email debe ser único en el sistema
- La contraseña se almacena siempre cifrada (BCrypt factor 12)
- Los usuarios bloqueados no pueden acceder a la plataforma
- El rol por defecto es 'user'
- El estado por defecto es 'activo'

---

## 2. Entidad: Proyecto

### Descripción
Representa los proyectos publicados por los usuarios en sus portfolios.

### Tabla en BD: `proyectos`

| Campo | Tipo | Constraints | Descripción |
|-------|------|-------------|-------------|
| `id_proyecto` | INT | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `titulo` | VARCHAR(200) | NOT NULL | Título del proyecto |
| `descripcion` | TEXT | NOT NULL | Descripción detallada |
| `tecnologias` | VARCHAR(500) | NULL | Tecnologías utilizadas (separadas por comas) |
| `galeriaImagenes` | JSON | NULL | Array de rutas de imágenes |
| `enlaceWeb` | VARCHAR(500) | NULL | URL del proyecto/repositorio |
| `totalLikes` | INT | NOT NULL, DEFAULT 0 | Contador total de votos |
| `id_usuario` | INT | NOT NULL, FOREIGN KEY | Usuario creador del proyecto |

### Relaciones
- **Proyecto → Usuario** (N:1): Cada proyecto pertenece a un usuario
- **Proyecto → Voto** (1:N): Un proyecto puede recibir múltiples votos
- **Proyecto → PublicacionRRSS** (1:N): Un proyecto puede publicarse en múltiples redes

### Reglas de Negocio
- Solo el propietario puede editar o eliminar su proyecto
- Los administradores pueden moderar (eliminar) cualquier proyecto
- `totalLikes` se actualiza automáticamente al crear/eliminar votos
- Al eliminar un proyecto, se eliminan en cascada sus votos y publicaciones

---

## 3. Entidad: CV (Curriculum Vitae)

### Descripción
Almacena los archivos de CV subidos por los usuarios.

### Tabla en BD: `cvs`

| Campo | Tipo | Constraints | Descripción |
|-------|------|-------------|-------------|
| `id_cv` | INT | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `tipoArchivo` | VARCHAR(10) | NOT NULL | Extensión del archivo: 'PDF', 'DOCX', 'TXT' |
| `rutaServidor` | VARCHAR(500) | NOT NULL | Ruta física del archivo en el servidor |
| `fechaSubida` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Fecha de carga |
| `id_usuario` | INT | NOT NULL, FOREIGN KEY | Usuario propietario del CV |

### Relaciones
- **CV → Usuario** (N:1): Cada CV pertenece a un usuario

### Reglas de Negocio
- Los archivos se almacenan en: `/uploads/cvs/{id_usuario}/`
- Nombre de archivo único: `{id_usuario}_{timestamp}.{extension}`
- Tamaño máximo: 10 MB
- Formatos permitidos: PDF, DOCX, TXT
- Un usuario puede tener múltiples versiones de CV (histórico)

---

## 4. Entidad: Voto

### Descripción
Representa los votos (likes) emitidos por usuarios a proyectos.

### Tabla en BD: `votos`

| Campo | Tipo | Constraints | Descripción |
|-------|------|-------------|-------------|
| `id_voto` | INT | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `id_usuario` | INT | NOT NULL, FOREIGN KEY | Usuario que emite el voto |
| `id_proyecto` | INT | NOT NULL, FOREIGN KEY | Proyecto que recibe el voto |
| `fechaVoto` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Fecha del voto |

**Constraint adicional:**
```sql
UNIQUE (id_usuario, id_proyecto)
```

### Relaciones
- **Voto → Usuario** (N:1): Cada voto pertenece a un usuario
- **Voto → Proyecto** (N:1): Cada voto está dirigido a un proyecto

### Reglas de Negocio
- **Un usuario solo puede votar una vez por proyecto** (constraint UNIQUE)
- Al insertar un voto, incrementar `Proyecto.totalLikes`
- Al eliminar un voto, decrementar `Proyecto.totalLikes`
- Un usuario puede votar en múltiples proyectos
- Un proyecto puede recibir votos de múltiples usuarios

---

## 5. Entidad: PublicacionRRSS

### Descripción
Registra las publicaciones de proyectos en redes sociales.

### Tabla en BD: `publicaciones_rrss`

| Campo | Tipo | Constraints | Descripción |
|-------|------|-------------|-------------|
| `id_publicacion` | INT | PRIMARY KEY, AUTO_INCREMENT | Identificador único |
| `id_proyecto` | INT | NOT NULL, FOREIGN KEY | Proyecto publicado |
| `redSocial` | VARCHAR(50) | NOT NULL | Red social: 'LinkedIn', 'Twitter', 'Facebook', etc. |
| `fechaPublicacion` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Fecha de publicación |
| `estado` | VARCHAR(20) | NOT NULL | Estado: 'pendiente', 'publicado', 'error' |

### Relaciones
- **PublicacionRRSS → Proyecto** (N:1): Cada publicación pertenece a un proyecto

### Reglas de Negocio
- Estados válidos: 'pendiente', 'publicado', 'error'
- Una publicación en estado 'error' puede reintentarse
- Se puede publicar el mismo proyecto en múltiples redes sociales
- Al eliminar un proyecto, se eliminan sus publicaciones

---

## 6. Diagrama de Relaciones (Resumen)

```
Usuario (1) ──crea──> (N) Proyecto
Usuario (1) ──sube──> (N) CV
Usuario (1) ──emite──> (N) Voto
Proyecto (1) ──recibe──> (N) Voto
Proyecto (1) ──se publica en──> (N) PublicacionRRSS
```

---

## 7. Índices Recomendados

Para optimizar consultas frecuentes:

```sql
-- Búsquedas por email (login)
CREATE INDEX idx_usuarios_email ON usuarios(email);

-- Ranking de proyectos
CREATE INDEX idx_proyectos_totalLikes ON proyectos(totalLikes DESC);

-- Proyectos de un usuario
CREATE INDEX idx_proyectos_usuario ON proyectos(id_usuario);

-- Votos de un usuario
CREATE INDEX idx_votos_usuario ON votos(id_usuario);

-- Votos a un proyecto
CREATE INDEX idx_votos_proyecto ON votos(id_proyecto);
```

---

## 8. Scripts de Inicialización

### Crear usuario administrador por defecto
```sql
INSERT INTO usuarios (nombre, email, contraseña, rol, estado)
VALUES ('Admin', 'admin@portfolio.com', '$2a$12$...', 'admin', 'activo');
```

### Crear usuario de prueba
```sql
INSERT INTO usuarios (nombre, email, contraseña, rol, estado, emailProfesional, whatsapp, telefono)
VALUES ('Carlos Díaz', 'carlos@example.com', '$2a$12$...', 'user', 'activo', 
        'carlos.profesional@example.com', '+34600000000', '600000000');
```

---

## 9. Ejemplos de Consultas

### Obtener ranking global de proyectos
```sql
SELECT p.id_proyecto, p.titulo, p.totalLikes, u.nombre AS autor
FROM proyectos p
INNER JOIN usuarios u ON p.id_usuario = u.id_usuario
ORDER BY p.totalLikes DESC, p.id_proyecto DESC
LIMIT 20 OFFSET 0;
```

### Verificar si un usuario ya votó un proyecto
```sql
SELECT COUNT(*) FROM votos
WHERE id_usuario = ? AND id_proyecto = ?;
```

### Obtener proyectos de un usuario con su contador de votos
```sql
SELECT p.*, COUNT(v.id_voto) AS totalVotos
FROM proyectos p
LEFT JOIN votos v ON p.id_proyecto = v.id_proyecto
WHERE p.id_usuario = ?
GROUP BY p.id_proyecto
ORDER BY p.id_proyecto DESC;
```

---

**Última actualización:** 2026-01-21  
**Autor:** Carlos Díaz Oller

