# 🔍 Verificación de Base de Datos - v2.6.0

**Fecha:** 06/02/2026  
**Estado:** Backend completado - Verificando BD antes de continuar con frontend

---

## ✅ CAMBIOS EN LA BASE DE DATOS

### Nuevas Tablas

#### 1. `verificaciones_email`
```sql
CREATE TABLE verificaciones_email (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    token VARCHAR(100) NOT NULL UNIQUE,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_expiracion TIMESTAMP NOT NULL,
    usado BOOLEAN NOT NULL DEFAULT FALSE,
    tipo VARCHAR(20) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);
```

### Columnas Modificadas

#### Tabla `usuarios`
- **Nueva columna:** `email_verificado` BOOLEAN DEFAULT FALSE

---

## 🔧 CÓMO VERIFICAR LA BASE DE DATOS

### Opción 1: Script SQL Automatizado ✅ RECOMENDADO

He creado un script que verifica automáticamente toda la estructura:

**Archivo:** `verificacion-bd.sql` (en la raíz del proyecto)

**Ejecutar:**
```bash
# Desde línea de comandos (si tienes psql en PATH)
psql -U carlos -d portfolio -f verificacion-bd.sql

# O desde PowerShell
& "C:\Program Files\PostgreSQL\15\bin\psql.exe" -U carlos -d portfolio -f verificacion-bd.sql
```

**Desde pgAdmin o DBeaver:**
1. Abre pgAdmin o DBeaver
2. Conecta a la base de datos `portfolio`
3. Abre el archivo `verificacion-bd.sql`
4. Ejecuta todas las consultas

---

### Opción 2: Verificación Manual Paso a Paso

#### Paso 1: Conectar a la base de datos
```bash
psql -U carlos -d portfolio
```

#### Paso 2: Listar todas las tablas
```sql
\dt
```

**Resultado esperado:**
```
                  List of relations
 Schema |          Name           | Type  | Owner  
--------+-------------------------+-------+--------
 public | cvs                     | table | carlos
 public | favoritos               | table | carlos
 public | proyectos               | table | carlos
 public | publicaciones_rrss      | table | carlos
 public | usuario_enlaces_rrss    | table | carlos
 public | usuarios                | table | carlos
 public | verificaciones_email    | table | carlos  ← ✅ NUEVA
 public | votos                   | table | carlos
```

#### Paso 3: Verificar estructura de usuarios
```sql
\d usuarios
```

**Buscar esta línea:**
```
email_verificado | boolean | not null default false  ← ✅ NUEVA COLUMNA
```

#### Paso 4: Verificar estructura de verificaciones_email
```sql
\d verificaciones_email
```

**Resultado esperado:**
```
                          Table "public.verificaciones_email"
     Column      |            Type             | Collation | Nullable |      Default       
-----------------+-----------------------------+-----------+----------+--------------------
 id              | bigint                      |           | not null | nextval(...)
 usuario_id      | bigint                      |           | not null | 
 token           | character varying(100)      |           | not null | 
 fecha_creacion  | timestamp without time zone |           | not null | 
 fecha_expiracion| timestamp without time zone |           | not null | 
 usado           | boolean                     |           | not null | false
 tipo            | character varying(20)       |           | not null | 
Indexes:
    "verificaciones_email_pkey" PRIMARY KEY, btree (id)
    "uk_token" UNIQUE CONSTRAINT, btree (token)
    "uk_usuario_id" UNIQUE CONSTRAINT, btree (usuario_id)
Foreign-key constraints:
    "fk_usuario" FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
```

---

### Opción 3: Ejecutar la Aplicación y Ver Logs

#### Paso 1: Ejecutar la aplicación
```bash
cd "C:\Users\USUARIO\IdeaProjects\2ª Evaluacion\Portfolio-Carlos-Diaz-Oller-main"
.\mvnw.cmd spring-boot:run
```

#### Paso 2: Buscar en los logs

**Logs exitosos deben mostrar:**
```
✅ HikariPool-1 - Start completed
✅ Started DemoSecurityProductosApplication in X.XXX seconds
✅ Tomcat started on port(s): 8089
```

**Si hay errores de BD, verás:**
```
❌ Error creating bean with name 'entityManagerFactory'
❌ Table 'verificaciones_email' doesn't exist
❌ Column 'email_verificado' not found
```

---

## 🚨 PROBLEMAS COMUNES Y SOLUCIONES

### Problema 1: Tabla no se crea
**Síntoma:** Error "Table 'verificaciones_email' doesn't exist"

**Solución:**
```sql
-- Ejecutar manualmente si ddl-auto no funciona
CREATE TABLE verificaciones_email (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    token VARCHAR(100) NOT NULL UNIQUE,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_expiracion TIMESTAMP NOT NULL,
    usado BOOLEAN NOT NULL DEFAULT FALSE,
    tipo VARCHAR(20) NOT NULL,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);
```

### Problema 2: Columna email_verificado no existe
**Síntoma:** Error "Column 'email_verificado' not found"

**Solución:**
```sql
-- Agregar columna manualmente
ALTER TABLE usuarios 
ADD COLUMN email_verificado BOOLEAN NOT NULL DEFAULT FALSE;
```

### Problema 3: ddl-auto=create borra datos
**Síntoma:** Todos los datos se borran al reiniciar

**Solución en application.properties:**
```properties
# Cambiar de 'create' a 'update'
spring.jpa.hibernate.ddl-auto=update
```

### Problema 4: No se puede conectar a PostgreSQL
**Síntoma:** "Connection refused" o "Authentication failed"

**Verificar:**
1. PostgreSQL está corriendo:
   ```bash
   Get-Service -Name postgresql*
   ```
2. Credenciales correctas en application.properties:
   ```properties
   spring.datasource.username=carlos
   spring.datasource.password=postgre
   ```
3. Base de datos existe:
   ```bash
   psql -U postgres -c "\l" | findstr portfolio
   ```

---

## ✅ CHECKLIST DE VERIFICACIÓN

### Antes de continuar con la Fase 5:

- [ ] ✅ Base de datos `portfolio` existe
- [ ] ✅ Tabla `usuarios` tiene columna `email_verificado`
- [ ] ✅ Tabla `verificaciones_email` existe
- [ ] ✅ Tabla tiene todas las columnas necesarias
- [ ] ✅ Constraint UNIQUE en `token`
- [ ] ✅ Constraint UNIQUE en `usuario_id`
- [ ] ✅ Foreign key a `usuarios` configurada
- [ ] ✅ Aplicación arranca sin errores
- [ ] ✅ No hay errores en logs de Hibernate

---

## 📊 ESTADO ESPERADO DE LA BD

### Tablas Totales: 8

| Tabla | Estado | Observación |
|-------|--------|-------------|
| usuarios | ✅ MODIFICADA | +1 columna (email_verificado) |
| proyectos | ✅ EXISTENTE | Sin cambios |
| cvs | ✅ EXISTENTE | Sin cambios |
| votos | ✅ EXISTENTE | Sin cambios |
| favoritos | ✅ EXISTENTE | Sin cambios |
| publicaciones_rrss | ✅ EXISTENTE | Sin cambios |
| usuario_enlaces_rrss | ✅ EXISTENTE | Sin cambios |
| **verificaciones_email** | ⭐ **NUEVA** | Tabla para tokens |

### Constraints Nuevos: 3

1. **PRIMARY KEY** en verificaciones_email.id
2. **UNIQUE** en verificaciones_email.token
3. **UNIQUE** en verificaciones_email.usuario_id
4. **FOREIGN KEY** de usuario_id → usuarios.id (CASCADE)

---

## 🎯 SIGUIENTE PASO DESPUÉS DE VERIFICAR

Una vez verificado que la BD está correcta:

### ✅ Si todo está OK:
1. ✅ Continuar con **Fase 5: Controladores y Vistas**
2. ✅ Crear VerificacionEmailController
3. ✅ Crear templates Thymeleaf

### ❌ Si hay problemas:
1. ❌ Ejecutar scripts SQL de solución
2. ❌ Verificar logs de error
3. ❌ Revisar configuración de application.properties
4. ❌ Volver a ejecutar la aplicación

---

## 🛠️ COMANDOS ÚTILES

### Ver todas las tablas
```sql
SELECT table_name FROM information_schema.tables 
WHERE table_schema = 'public' ORDER BY table_name;
```

### Ver estructura completa de una tabla
```sql
\d+ verificaciones_email
```

### Ver constraints
```sql
SELECT constraint_name, constraint_type 
FROM information_schema.table_constraints 
WHERE table_name = 'verificaciones_email';
```

### Ver foreign keys
```sql
SELECT
    tc.constraint_name,
    tc.table_name,
    kcu.column_name,
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name
FROM information_schema.table_constraints AS tc
JOIN information_schema.key_column_usage AS kcu
    ON tc.constraint_name = kcu.constraint_name
JOIN information_schema.constraint_column_usage AS ccu
    ON ccu.constraint_name = tc.constraint_name
WHERE tc.constraint_type = 'FOREIGN KEY' 
  AND tc.table_name = 'verificaciones_email';
```

### Contar registros
```sql
SELECT COUNT(*) FROM verificaciones_email;
```

---

## 📝 REGISTRO DE VERIFICACIÓN

**Fecha:** ___________  
**Verificado por:** ___________

### Resultados:
- [ ] Conexión a BD exitosa
- [ ] Tabla verificaciones_email existe
- [ ] Columna email_verificado existe en usuarios
- [ ] Constraints correctos
- [ ] Foreign keys correctos
- [ ] Aplicación arranca sin errores

### Observaciones:
```
_______________________________________
_______________________________________
_______________________________________
```

---

**Documento creado:** 06/02/2026  
**Propósito:** Verificar que la BD está lista antes de continuar con Fase 5

---

*Una vez verificada la base de datos, estaremos listos para implementar los controladores y las vistas. 🚀*

