# Diagramas del Proyecto - Portfolio Social v2.0

## Modelo Entidad-Relación (ER)

```mermaid
erDiagram
    USUARIO ||--o{ PROYECTO : "crea"
    USUARIO ||--o{ CV : "sube"
    USUARIO ||--o{ VOTO : "emite"
    USUARIO ||--o{ FAVORITO : "marca"
    PROYECTO ||--o{ VOTO : "recibe"
    PROYECTO ||--o{ PUBLICACION_RRSS : "se publica en"
    PROYECTO ||--o{ FAVORITO : "es favorito de"

    USUARIO {
        int id_usuario PK
        string nombre
        string email UK
        string contraseña
        string rol
        string estado
        string emailProfesional
        string whatsapp
        string telefono
        json enlacesRRSS
        timestamp fechaRegistro
        timestamp fechaActualizacion
    }

    PROYECTO {
        int id_proyecto PK
        string titulo
        text descripcion
        string tecnologias
        json galeriaImagenes
        string enlaceWeb
        int totalLikes
        int id_usuario FK
        timestamp fechaCreacion
        timestamp fechaActualizacion
    }

    CV {
        int id_cv PK
        string tipoArchivo
        string rutaServidor
        string nombreOriginal
        bigint tamañoBytes
        int id_usuario FK
        timestamp fechaSubida
        boolean activo
    }

    VOTO {
        int id_voto PK
        int id_usuario FK
        int id_proyecto FK
        timestamp fechaVoto
    }

    PUBLICACION_RRSS {
        int id_publicacion PK
        int id_proyecto FK
        string redSocial
        timestamp fechaPublicacion
        string estado
        string urlPublicacion
        text mensajeError
    }

    FAVORITO {
        int id_favorito PK
        int id_usuario FK
        int id_proyecto FK
    }
```

---

## Diagrama de Clases UML

```mermaid
classDiagram
    class Usuario {
        -Long id
        -String nombre
        -String email
        -String contraseña
        -Rol rol
        -Estado estado
        -String emailProfesional
        -String whatsapp
        -String telefono
        -List~String~ enlacesRRSS
        +crearUsuario()
        +actualizarUsuario()
        +bloquearUsuario()
        +eliminarUsuario()
        +getUsername()
        +getPassword()
    }

    class Proyecto {
        -Long id
        -String titulo
        -String descripcion
        -String tecnologias
        -List~String~ galeriaImagenes
        -String enlaceWeb
        -Integer totalLikes
        +crearProyecto()
        +actualizarProyecto()
        +eliminarProyecto()
        +publicarEnRRSS()
        +obtenerRanking()
        +agregarImagen()
        +incrementarLikes()
        +decrementarLikes()
    }

    class CV {
        -Long id
        -String tipoArchivo
        -String rutaServidor
        -String nombreOriginal
        -Long tamañoBytes
        -LocalDateTime fechaSubida
        -Boolean activo
        +subirCV()
        +descargarCV()
        +eliminarCV()
    }

    class Voto {
        -Long id
        -LocalDateTime fechaVoto
        +votar()
    }

    class PublicacionRRSS {
        -Long id
        -String redSocial
        -LocalDateTime fechaPublicacion
        -EstadoPublicacion estado
        -String urlPublicacion
        -String mensajeError
    }

    class Favorito {
        -Long id
        +agregarFavorito()
        +eliminarFavorito()
        +listarFavoritosPorUsuario()
    }

    class Rol {
        <<enumeration>>
        ADMIN
        USER
    }

    class Estado {
        <<enumeration>>
        ACTIVO
        BLOQUEADO
    }

    class EstadoPublicacion {
        <<enumeration>>
        PENDIENTE
        PUBLICADO
        ERROR
    }

    Usuario "1" --> "*" Proyecto : crea
    Usuario "1" --> "*" CV : sube
    Usuario "1" --> "*" Voto : emite
    Usuario "1" --> "*" Favorito : marca
    Usuario --> Rol
    Usuario --> Estado
    Proyecto "1" --> "*" Voto : recibe
    Proyecto "1" --> "*" PublicacionRRSS : se publica en
    Proyecto "1" --> "*" Favorito : es favorito de
    PublicacionRRSS --> EstadoPublicacion
```

---

## Cómo usar estos diagramas

### En GitHub (README.md o documentación)
GitHub soporta Mermaid nativamente. Solo copia el bloque de código directamente en tu archivo Markdown.

### En herramientas online
1. **Mermaid Live Editor**: https://mermaid.live/
2. **Draw.io** con plugin Mermaid
3. **Confluence** (si tienes la extensión)

### Exportar como imagen
1. Ve a https://mermaid.live/
2. Pega el código Mermaid
3. Haz clic en "Actions" → "PNG" o "SVG"
4. Descarga la imagen

---

## Notas técnicas

### Constraint UNIQUE en Voto
```sql
UNIQUE (id_usuario, id_proyecto)
```
Un usuario solo puede votar una vez por proyecto.

### Constraint UNIQUE en Favorito
```sql
UNIQUE (id_usuario, id_proyecto)
```
Un usuario solo puede marcar el mismo proyecto como favorito una vez.

### Cascada en eliminaciones
- Al eliminar Usuario → se eliminan sus Proyectos, CVs, Votos y Favoritos
- Al eliminar Proyecto → se eliminan sus Votos, PublicacionRRSS y Favoritos

