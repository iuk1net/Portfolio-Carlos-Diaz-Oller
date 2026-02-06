# Guía de Configuración y Despliegue - Portfolio Social v2.4.0

Esta guía contiene toda la información necesaria para configurar, desarrollar y desplegar la aplicación.

---

## 📋 Tabla de Contenidos

1. [Configuración del Entorno](#configuración-del-entorno)
2. [Variables de Entorno](#variables-de-entorno)
3. [Despliegue Local](#despliegue-local)
4. [Despliegue en Producción](#despliegue-en-producción)
5. [Resolución de Problemas](#resolución-de-problemas)

---

## ⚙️ Configuración del Entorno

### Requisitos Previos
- Java 17 o superior
- PostgreSQL 15 o superior
- Maven 3.6 o superior

### Instalación de Dependencias

**Java 17:**
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# macOS (con Homebrew)
brew install openjdk@17

# Windows
# Descargar desde: https://adoptium.net/
```

**PostgreSQL:**
```bash
# Ubuntu/Debian
sudo apt install postgresql postgresql-contrib

# macOS
brew install postgresql@15

# Windows
# Descargar desde: https://www.postgresql.org/download/windows/
```

---

## 🔐 Variables de Entorno

La aplicación utiliza variables de entorno para configuración sensible (credenciales, rutas, etc.).

### Variables Disponibles

| Variable | Descripción | Por Defecto (dev) | Requerido (prod) |
|----------|-------------|-------------------|------------------|
| `DB_HOST` | Host de PostgreSQL | `localhost` | ✅ |
| `DB_PORT` | Puerto de PostgreSQL | `5432` | ✅ |
| `DB_NAME` | Nombre de la base de datos | `portfolio` | ✅ |
| `DB_USERNAME` | Usuario de la base de datos | `carlos` | ✅ |
| `DB_PASSWORD` | Contraseña de la base de datos | `postgre` | ✅ |
| `SERVER_PORT` | Puerto del servidor Spring | `8089` | ❌ |
| `UPLOAD_DIR` | Directorio de CVs | `uploads/cvs` | ❌ |
| `UPLOAD_IMAGES_DIR` | Directorio de imágenes | `uploads/images` | ❌ |

### Configurar Variables de Entorno

#### Opción 1: Variables del Sistema (Linux/Mac)
```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=portfolio
export DB_USERNAME=tu_usuario
export DB_PASSWORD=tu_password
```

#### Opción 2: Variables del Sistema (Windows PowerShell)
```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="5432"
$env:DB_NAME="portfolio"
$env:DB_USERNAME="tu_usuario"
$env:DB_PASSWORD="tu_password"
```

#### Opción 3: Archivo .env (Solo desarrollo local)
```bash
# Crear archivo .env en la raíz del proyecto
cat > .env << EOF
DB_HOST=localhost
DB_PORT=5432
DB_NAME=portfolio
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password
EOF
```

⚠️ **IMPORTANTE:** El archivo `.env` ya está en `.gitignore` y NO se debe subir a Git.

---

## 🏠 Despliegue Local (Desarrollo)

### Paso 1: Clonar Repositorio
```bash
git clone https://github.com/iuk1net/Portfolio-Carlos-Diaz-Oller.git
cd Portfolio-Carlos-Diaz-Oller
```

### Paso 2: Crear Base de Datos
```bash
# Opción 1: Con comando createdb
createdb portfolio

# Opción 2: Con psql
psql -U postgres
CREATE DATABASE portfolio;
\q
```

### Paso 3: Configurar Credenciales (Opcional)

**Si NO configuras nada**, la aplicación usará valores por defecto:
- Usuario: `carlos`
- Password: `postgre`
- Base de datos: `portfolio` en `localhost:5432`

**Para usar tus propias credenciales**, configura las variables de entorno (ver sección anterior).

### Paso 4: Ejecutar Aplicación
```bash
# Desarrollo (perfil por defecto)
./mvnw spring-boot:run

# O con Maven Wrapper en Windows
.\mvnw.cmd spring-boot:run
```

### Paso 5: Acceder a la Aplicación
```
http://localhost:8089
```

**Credenciales iniciales:** Debes registrarte en `/register`

---

## 🌐 Despliegue en Producción

### Paso 1: Preparar Servidor

Instalar Java y PostgreSQL (ver sección "Configuración del Entorno").

### Paso 2: Crear Base de Datos y Usuario

```bash
sudo -u postgres psql

CREATE DATABASE portfolio;
CREATE USER portfolio_user WITH PASSWORD 'password_muy_seguro';
GRANT ALL PRIVILEGES ON DATABASE portfolio TO portfolio_user;
\q
```

### Paso 3: Configurar Variables de Entorno

**Crear service de systemd** (`/etc/systemd/system/portfolio.service`):

```ini
[Unit]
Description=Portfolio Social Application
After=postgresql.service

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/portfolio
ExecStart=/usr/bin/java -jar /opt/portfolio/portfolio.jar --spring.profiles.active=prod
Restart=always
RestartSec=10

Environment="DB_HOST=localhost"
Environment="DB_PORT=5432"
Environment="DB_NAME=portfolio"
Environment="DB_USERNAME=portfolio_user"
Environment="DB_PASSWORD=password_muy_seguro"
Environment="SERVER_PORT=8080"

[Install]
WantedBy=multi-user.target
```

### Paso 4: Compilar Aplicación

```bash
# En tu máquina local o en el servidor
./mvnw clean package -DskipTests

# El JAR se genera en:
# target/demoSecurityProductos-0.0.1-SNAPSHOT.jar
```

### Paso 5: Subir al Servidor

```bash
# Desde tu máquina local
scp target/demoSecurityProductos-0.0.1-SNAPSHOT.jar usuario@servidor:/opt/portfolio/portfolio.jar
```

### Paso 6: Ejecutar en Producción

```bash
# Con systemd (recomendado)
sudo systemctl daemon-reload
sudo systemctl start portfolio
sudo systemctl enable portfolio
sudo systemctl status portfolio

# O manual (para pruebas)
java -jar /opt/portfolio/portfolio.jar --spring.profiles.active=prod
```

### Paso 7: Configurar Nginx (Opcional pero recomendado)

**Instalar Nginx:**
```bash
sudo apt install nginx
```

**Configurar proxy inverso** (`/etc/nginx/sites-available/portfolio`):

```nginx
server {
    listen 80;
    server_name tu-dominio.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Servir archivos estáticos directamente
    location /uploads/ {
        alias /opt/portfolio/uploads/;
    }
}
```

**Habilitar sitio:**
```bash
sudo ln -s /etc/nginx/sites-available/portfolio /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

**Configurar SSL/HTTPS con Let's Encrypt:**
```bash
sudo apt install certbot python3-certbot-nginx
sudo certbot --nginx -d tu-dominio.com
```

---

## 🐳 Despliegue con Docker (Alternativa)

### Dockerfile

```dockerfile
FROM openjdk:17-alpine
WORKDIR /app
COPY target/demoSecurityProductos-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
```

### docker-compose.yml

```yaml
version: '3.8'
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: portfolio
      POSTGRES_USER: portfolio_user
      POSTGRES_PASSWORD: password_seguro
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      DB_HOST: db
      DB_PORT: 5432
      DB_NAME: portfolio
      DB_USERNAME: portfolio_user
      DB_PASSWORD: password_seguro
      SPRING_PROFILES_ACTIVE: prod
    depends_on:
      - db

volumes:
  postgres_data:
```

### Ejecutar

```bash
./mvnw clean package -DskipTests
docker-compose up -d
docker-compose logs -f app
```

---

## 🆘 Resolución de Problemas

### Error: "Cannot connect to database"

```bash
# Verificar PostgreSQL está corriendo
sudo systemctl status postgresql

# Verificar credenciales
echo $DB_USERNAME
echo $DB_PASSWORD

# Probar conexión manual
psql -h localhost -U portfolio_user -d portfolio
```

### Error: "Port 8080 already in use"

```bash
# Ver qué está usando el puerto
sudo lsof -i :8080

# Cambiar puerto
export SERVER_PORT=8081
```

### Error: "Out of memory"

```bash
# Aumentar memoria JVM
java -Xmx512m -Xms256m -jar portfolio.jar --spring.profiles.active=prod
```

### Ver Logs

```bash
# Logs del service
sudo journalctl -u portfolio -f

# Logs de la aplicación
tail -f /opt/portfolio/logs/portfolio-app.log
```

---

## ✅ Checklist Post-Despliegue

### Seguridad
- [ ] Variables de entorno configuradas
- [ ] `ddl-auto=validate` en producción
- [ ] HTTPS configurado (con Nginx + Let's Encrypt)
- [ ] Firewall configurado (solo puertos 80, 443, 22)
- [ ] PostgreSQL no accesible desde fuera
- [ ] Backup automático configurado

### Funcionalidad
- [ ] Aplicación arranca sin errores
- [ ] Registro de usuarios funciona
- [ ] Login funciona
- [ ] Crear proyecto funciona
- [ ] Subir imágenes funciona
- [ ] Votación funciona
- [ ] Favoritos funciona
- [ ] Subir CV funciona

---

## 📞 Soporte

Si encuentras problemas:
1. Revisa los logs: `journalctl -u portfolio -f`
2. Verifica las variables de entorno
3. Consulta la documentación técnica: `docs/04-manual-desarrollo-portfolio.md`
4. Revisa el CHANGELOG: `docs/CHANGELOG.md`

---

**Última actualización:** 03/02/2026  
**Versión:** 2.4.0

