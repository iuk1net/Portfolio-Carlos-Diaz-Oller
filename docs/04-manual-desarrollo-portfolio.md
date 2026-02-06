# Manual de Desarrollo

## 1. Objetivo
Establecer procedimientos, estándares y buenas prácticas para el desarrollo, mantenimiento y evolución de la plataforma.

## 2. Entorno de Desarrollo

### 2.1. Requisitos
- **Java JDK**: 17 o superior
- **Maven**: 3.6+
- **PostgreSQL**: 15+
- **IDE**: IntelliJ IDEA (recomendado) o Eclipse
- **Git**: 2.30+

### 2.2. Configuración Inicial
```bash
# Clonar repositorio
git clone https://github.com/usuario/portfolio-social.git
cd portfolio-social

# Configurar base de datos
createdb portfolio_db
psql portfolio_db < schema.sql

# Configurar variables de entorno
export DB_USERNAME=portfolio_user
export DB_PASSWORD=secure_password

# Compilar proyecto
mvn clean install

# Ejecutar aplicación
mvn spring-boot:run
```

## 3. Estructura del Proyecto

```
src/main/java/es/fempa/acd/demosecurityproductos/
├── config/              # Configuración (Security, MVC, etc.)
├── controller/          # Controladores MVC y REST
├── model/               # Entidades JPA
│   └── enums/          # Enumeraciones
├── repository/          # Repositorios JPA
├── service/            # Lógica de negocio
└── DemoSecurityProductosApplication.java

src/main/resources/
├── application.properties
├── static/             # CSS, JS, imágenes
└── templates/          # Vistas Thymeleaf

src/test/java/          # Tests unitarios e integración
```

## 4. Flujo de Trabajo Git

### 4.1. Estrategia de Branching
- **main**: Código estable en producción
- **develop**: Rama de integración (opcional)
- **feature/**: Nuevas funcionalidades
- **bugfix/**: Correcciones de bugs
- **hotfix/**: Fixes urgentes en producción

### 4.2. Convenciones de Commits
```
feat: Añadir sistema de votación AJAX
fix: Corregir validación de voto duplicado
docs: Actualizar documentación de API
refactor: Optimizar consultas de ranking
test: Añadir tests para VotoService
chore: Actualizar dependencias Spring Boot
```

### 4.3. Proceso de Desarrollo
```bash
# 1. Crear branch desde main
git checkout main
git pull origin main
git checkout -b feature/nueva-funcionalidad

# 2. Desarrollo con commits frecuentes
git add .
git commit -m "feat: implementar base de nueva funcionalidad"

# 3. Mantener actualizado
git fetch origin
git rebase origin/main

# 4. Push y crear Pull Request
git push origin feature/nueva-funcionalidad
# Crear PR en GitHub

# 5. Después de aprobación
git checkout main
git pull origin main
git branch -d feature/nueva-funcionalidad
```

## 5. Estándares de Código

### 5.1. Convenciones de Nomenclatura
```java
// Clases: PascalCase
public class ProyectoService { }

// Métodos y variables: camelCase
public void crearProyecto() { }
private String nombreUsuario;

// Constantes: UPPER_SNAKE_CASE
private static final int MAX_FILE_SIZE = 10;

// Paquetes: lowercase
package es.fempa.acd.demosecurityproductos.service;
```

### 5.2. Documentación JavaDoc
```java
/**
 * Registra un voto de un usuario a un proyecto
 * 
 * @param usuario el usuario que vota
 * @param proyecto el proyecto a votar
 * @return el voto creado
 * @throws IllegalArgumentException si el usuario ya votó
 */
@Transactional
public Voto votar(Usuario usuario, Proyecto proyecto) {
    // Implementación
}
```

### 5.3. Manejo de Excepciones
```java
// Excepciones específicas
throw new IllegalArgumentException("Mensaje descriptivo");
throw new AccessDeniedException("Sin permisos");

// Try-catch con logging
try {
    // Operación
} catch (IOException e) {
    log.error("Error al procesar archivo: {}", e.getMessage());
    throw new RuntimeException("Error al subir archivo", e);
}
```

### 5.4. Transacciones
```java
// Operaciones que modifican BD
@Transactional
public void crearProyecto(Proyecto proyecto) {
    // Múltiples operaciones atómicas
    proyectoRepository.save(proyecto);
    auditService.registrar("Proyecto creado");
}
```

## 6. Testing

### 6.0. Estado Actual
✅ **44 tests implementados** con JUnit 5 y Spring Boot Test  
✅ **100% exitosos** - 0 fallos, 0 errores  
✅ **Cobertura ~70%** en servicios críticos

| Servicio | Tests | Archivo |
|----------|-------|---------|
| VotoService | 10 | `VotoServiceTest.java` |
| ProyectoService | 10 | `ProyectoServiceTest.java` |
| UsuarioService | 12 | `UsuarioServiceTest.java` |
| FavoritoService | 11 | `FavoritoServiceTest.java` |

### 6.1. Tests Unitarios

Ejemplo de test implementado en `VotoServiceTest.java`:

```java
@SpringBootTest
@Transactional
class VotoServiceTest {
    
    @Autowired
    private VotoService votoService;
    
    @Test
    @DisplayName("Test 1: Votar un proyecto correctamente")
    void testVotarProyecto() {
        // Given
        Usuario usuario = crearUsuarioTest();
        Proyecto proyecto = crearProyectoTest();
        
        // When
        Voto voto = votoService.votar(usuario, proyecto);
        
        // Then
        assertNotNull(voto);
        assertEquals(1, proyecto.getTotalLikes());
    }
    
    @Test
    @DisplayName("Test 2: No permitir voto duplicado")
    void testNoPermitirVotoDuplicado() {
        // Given: usuario ya votó el proyecto
        votoService.votar(usuario, proyecto);
        
        // When/Then: intentar votar de nuevo
        assertThrows(IllegalArgumentException.class, 
            () -> votoService.votar(usuario, proyecto));
    }
}
```

### 6.2. Tests de Integración

Los tests utilizan `@SpringBootTest` para testing de integración completo:

```java
@SpringBootTest
@Transactional // Rollback automático después de cada test
@WithMockUser(roles = "ADMIN") // Para tests que requieren autenticación
class ProyectoServiceTest {
    
    @Autowired
    private ProyectoService proyectoService;
    
    @Test
    @DisplayName("Test: Solo propietario puede editar")
    void testNoActualizarProyectoAjeno() {
        // Given: usuario1 crea proyecto
        Proyecto proyecto = proyectoService.crearProyecto(proyecto, usuario1);
        
        // When/Then: usuario2 intenta editar
        assertThrows(AccessDeniedException.class,
            () -> proyectoService.actualizarProyecto(
                proyecto.getId(), proyectoActualizado, usuario2.getEmail()
            )
        );
    }
}
```

### 6.3. Ejecutar Tests
```bash
# Todos los tests
mvn test

# Test específico
mvn test -Dtest=VotoServiceTest

# Servicio específico
mvn test -Dtest=ProyectoServiceTest

# Limpiar y testear
mvn clean test

# Resultado esperado:
# [INFO] Tests run: 44, Failures: 0, Errors: 0, Skipped: 0
# [INFO] BUILD SUCCESS
```

### 6.4. Cobertura con JaCoCo (Opcional)

Añadir al `pom.xml`:
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
</plugin>
```

Ejecutar:
```bash
mvn test jacoco:report
# Reporte en: target/site/jacoco/index.html
```

## 7. Base de Datos

### 7.1. Migraciones
```sql
-- V1__create_usuarios_table.sql
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL
);

-- V2__add_indexes.sql
CREATE INDEX idx_proyectos_totalLikes ON proyectos(total_likes DESC);
CREATE INDEX idx_votos_usuario_proyecto ON votos(id_usuario, id_proyecto);
```

### 7.2. Consultas Optimizadas
```java
// Repositorio con consultas custom
@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    
    @Query("SELECT p FROM Proyecto p LEFT JOIN FETCH p.usuario ORDER BY p.totalLikes DESC")
    List<Proyecto> findRankingConUsuario();
    
    @Query("SELECT p FROM Proyecto p WHERE p.usuario.id = :usuarioId")
    List<Proyecto> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
```

## 8. Seguridad

### 8.1. Validación de Entrada
```java
// En controlador
@PostMapping("/proyectos")
public String crear(@Valid @ModelAttribute Proyecto proyecto, 
                   BindingResult result) {
    if (result.hasErrors()) {
        return "proyectos/nuevo";
    }
    // Procesar
}

// En entidad
@Entity
public class Proyecto {
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200)
    private String titulo;
    
    @NotBlank
    @Size(max = 5000)
    private String descripcion;
}
```

### 8.2. Autorización
```java
// En servicio
@PreAuthorize("hasRole('ADMIN')")
public void eliminarUsuario(Long id) {
    usuarioRepository.deleteById(id);
}

// Verificación manual
public void actualizarProyecto(Long id, String username) {
    Proyecto proyecto = obtenerProyecto(id);
    if (!proyecto.getUsuario().getUsername().equals(username)) {
        throw new AccessDeniedException("Sin permisos");
    }
    // Actualizar
}
```

## 9. Despliegue

### 9.1. Build de Producción
```bash
# Crear JAR ejecutable
mvn clean package -DskipTests

# JAR generado en:
target/demoSecurityProductos-1.0.0.jar
```

### 9.2. Variables de Entorno
```bash
# En producción
export SPRING_PROFILES_ACTIVE=prod
export DB_URL=jdbc:postgresql://prod-server:5432/portfolio_db
export DB_USERNAME=prod_user
export DB_PASSWORD=secure_prod_password
export JWT_SECRET=very_secure_secret_key
```

### 9.3. Ejecutar en Producción
```bash
java -jar demoSecurityProductos-1.0.0.jar \
  --spring.profiles.active=prod \
  --server.port=8080
```

### 9.4. Docker (Opcional)
```dockerfile
FROM openjdk:17-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
# Build imagen
docker build -t portfolio-social:1.0.0 .

# Ejecutar contenedor
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://db:5432/portfolio \
  portfolio-social:1.0.0
```

## 10. Monitoreo y Logs

### 10.1. Logging
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProyectoService {
    private static final Logger log = LoggerFactory.getLogger(ProyectoService.class);
    
    public void crearProyecto(Proyecto proyecto) {
        log.info("Creando proyecto: {}", proyecto.getTitulo());
        try {
            proyectoRepository.save(proyecto);
            log.info("Proyecto creado con ID: {}", proyecto.getId());
        } catch (Exception e) {
            log.error("Error al crear proyecto", e);
            throw e;
        }
    }
}
```

### 10.2. Actuator (Opcional)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized
```

## 11. Resolución de Problemas

### 11.1. Errores Comunes

**Error: Port already in use**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

**Error: Database connection refused**
```bash
# Verificar PostgreSQL
pg_isready -h localhost -p 5432

# Reiniciar servicio
sudo service postgresql restart
```

**Error: Tests fallan**
```bash
# Limpiar y recompilar
mvn clean install -U

# Verificar base de datos de test
dropdb test_db && createdb test_db
```

## 12. Buenas Prácticas

### 12.1. Código Limpio
- Métodos cortos (< 20 líneas)
- Nombres descriptivos
- Evitar código duplicado (DRY)
- Un solo nivel de abstracción por método
- Comentarios solo para lógica compleja

### 12.2. Performance
- Usar `@Transactional` solo cuando sea necesario
- Lazy loading en relaciones JPA
- Índices en columnas de búsqueda frecuente
- Paginación en listados grandes
- Cache para consultas frecuentes

### 12.3. Seguridad
- Nunca commitear credenciales
- Validar entrada en frontend Y backend
- Usar prepared statements (JPA)
- Implementar rate limiting
- Logs sin información sensible

## 13. Recursos

### 13.1. Documentación
- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Thymeleaf Docs](https://www.thymeleaf.org/documentation.html)

### 13.2. Herramientas
- **DBeaver**: Gestión de PostgreSQL
- **Postman**: Testing de API
- **SonarLint**: Análisis de código en IDE
- **Git Extensions**: Interfaz Git visual

