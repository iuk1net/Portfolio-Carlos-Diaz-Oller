# Plan de Implementación - Verificación de Email (Feature Futura v2.6.0)

**Proyecto:** Plataforma Social de Portfolios — Carlos Díaz Oller  
**Versión Actual:** 2.5.1  
**Versión Objetivo:** 2.6.0 (Feature planificada)  
**Fecha:** Febrero 2026  
**Estado:** 📋 Documentación preparatoria - NO implementado

---

## ⚠️ IMPORTANTE
Este documento describe una **funcionalidad futura** que aún NO está implementada.
La versión actual del proyecto es **v2.5.1** y funciona completamente SIN verificación de email.

---

## 📋 Índice
1. [Resumen del Plan](#resumen-del-plan)
2. [Generación de Diagramas](#generación-de-diagramas)
3. [Fases de Implementación](#fases-de-implementación)
4. [Configuración SMTP Gmail](#configuración-smtp-gmail)
5. [Checklist de Tareas](#checklist-de-tareas)

---

## 📝 Resumen del Plan

### Objetivo
Implementar un sistema completo de verificación de email para usuarios nuevos y recuperación de contraseña, utilizando Gmail SMTP.

### Nueva Funcionalidad
- ✅ Verificación obligatoria de email al registrarse
- ✅ Recuperación de contraseña por email
- ✅ Tokens únicos con expiración de 24 horas
- ✅ Emails HTML personalizados
- ✅ Control de acceso según estado de verificación

### Cambios en el Modelo de Datos
- **Usuario**: Nuevo campo `emailVerificado` (boolean)
- **Nueva entidad**: `VerificacionEmail`
- **Nuevos servicios**: `EmailService`, `VerificacionEmailService`
- **Nuevos repositorios**: `VerificacionEmailRepository`

---

## ✅ Diagramas Generados

### Archivos Disponibles
Los diagramas PNG han sido generados y están disponibles en:
- 🖼️ `docs/Modelo Entidad Relacion.png` - Diagrama ER v2.1 ✅
- 🖼️ `docs/UML.png` - Diagrama UML v2.1 ✅

### Características Incluidas
- ✅ Nueva entidad **VerificacionEmail**
- ✅ Campo **emailVerificado** en Usuario
- ✅ Servicios **EmailService** y **VerificacionEmailService**
- ✅ Enum **TipoVerificacion** (REGISTRO, RECUPERACION)
- ✅ Repositorio **VerificacionEmailRepository**

### Uso de los Diagramas
Consulta estos diagramas como referencia visual durante la implementación:
- **Diagrama ER**: Para entender las entidades y sus relaciones
- **Diagrama UML**: Para entender la arquitectura de clases y servicios

---

## 🚀 Fases de Implementación

### Fase 1: Modelo de Datos ✅ (Preparado)
**Archivos a crear/modificar:**
- [x] `model/VerificacionEmail.java` - Nueva entidad
- [x] `model/enums/TipoVerificacion.java` - Enum REGISTRO/RECUPERACION
- [x] `model/Usuario.java` - Añadir campo `emailVerificado`

**Estimación:** 1-2 horas

---

### Fase 2: Repositorios y Servicios
**Archivos a crear:**
- [ ] `repository/VerificacionEmailRepository.java`
- [ ] `service/VerificacionEmailService.java`
- [ ] `service/EmailService.java`

**Modificar:**
- [ ] `service/UsuarioService.java` - Integrar verificación

**Estimación:** 3-4 horas

---

### Fase 3: Configuración SMTP
**Archivos a modificar:**
- [ ] `src/main/resources/application.properties`
- [ ] `src/main/resources/application-prod.properties`
- [ ] `pom.xml` - Añadir dependencia `spring-boot-starter-mail`

**Estimación:** 1 hora

---

### Fase 4: Controladores y Vistas
**Archivos a crear/modificar:**
- [ ] `controller/VerificacionEmailController.java`
- [ ] `templates/email/verificacion-email.html`
- [ ] `templates/email/recuperacion-password.html`
- [ ] `templates/email/bienvenida.html`
- [ ] `templates/verificacion-exitosa.html`
- [ ] `templates/verificacion-error.html`

**Modificar:**
- [ ] `controller/AuthController.java` - Integrar verificación

**Estimación:** 4-5 horas

---

### Fase 5: Testing
**Archivos a crear:**
- [ ] `test/service/EmailServiceTest.java`
- [ ] `test/service/VerificacionEmailServiceTest.java`
- [ ] `test/controller/VerificacionEmailControllerTest.java`

**Estimación:** 3-4 horas

---

### Fase 6: Documentación
**Archivos a actualizar:**
- [ ] `docs/06-modelo-datos-completo.md`
- [ ] `docs/04-manual-desarrollo-portfolio.md`
- [ ] `docs/05-guia-configuracion-despliegue.md`
- [ ] `docs/CHANGELOG.md`
- [ ] `README.md`

**Estimación:** 2-3 horas

---

## 📧 Configuración SMTP Gmail

### Credenciales Proporcionadas
```properties
Email: [TU_EMAIL]@gmail.com
App Password: yguc ccvn dsja dclu
```

### Configuración en `application.properties`
```properties
# ====================================
# CONFIGURACIÓN EMAIL (Gmail SMTP)
# ====================================
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=carlosiuka88@gmail.com
spring.mail.password=yguc ccvn dsja dclu

# Propiedades SMTP
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000

# Configuración de verificación
verificacion.email.expiracion-horas=24
verificacion.email.url-base=http://localhost:8080
```

### Configuración en `application-prod.properties`
```properties
# URL base en producción
verificacion.email.url-base=https://tu-dominio.com

# Opcional: Email diferente en producción
spring.mail.username=produccion@gmail.com
spring.mail.password=[PASSWORD_PRODUCCION]
```

### Obtener App Password de Gmail
1. Ir a: https://myaccount.google.com/apppasswords
2. Seleccionar "Mail" y "Other (Custom name)"
3. Nombre: "Portfolio App"
4. Copiar la contraseña generada (16 caracteres sin espacios)
5. **Nota**: Ya tienes una: `yguc ccvn dsja dclu`

### Verificar Configuración
En Gmail:
- ✅ Verificación en 2 pasos activada
- ✅ "Acceso de aplicaciones menos seguras" NO necesario (usamos App Password)
- ✅ IMAP/POP3 activado (opcional)

---

## 📦 Dependencia Maven Necesaria

### Añadir a `pom.xml`
```xml
<!-- Email -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

---

## ✅ Checklist de Tareas

### Pre-implementación
- [x] ✅ Generar código Mermaid para ER
- [x] ✅ Generar código Mermaid para UML
- [ ] 🔲 Crear imágenes PNG de los diagramas
- [ ] 🔲 Actualizar documentación técnica

### Implementación Backend
- [ ] 🔲 Crear entidad `VerificacionEmail`
- [ ] 🔲 Crear enum `TipoVerificacion`
- [ ] 🔲 Modificar entidad `Usuario` (campo `emailVerificado`)
- [ ] 🔲 Crear `VerificacionEmailRepository`
- [ ] 🔲 Crear `VerificacionEmailService`
- [ ] 🔲 Crear `EmailService`
- [ ] 🔲 Modificar `UsuarioService`
- [ ] 🔲 Añadir dependencia Maven `spring-boot-starter-mail`
- [ ] 🔲 Configurar SMTP en `application.properties`

### Implementación Frontend
- [ ] 🔲 Crear controlador `VerificacionEmailController`
- [ ] 🔲 Crear plantilla HTML de email de verificación
- [ ] 🔲 Crear plantilla HTML de email de recuperación
- [ ] 🔲 Crear plantilla HTML de email de bienvenida
- [ ] 🔲 Crear vista `verificacion-exitosa.html`
- [ ] 🔲 Crear vista `verificacion-error.html`
- [ ] 🔲 Modificar `AuthController` para integrar verificación
- [ ] 🔲 Actualizar vista de registro con mensaje de verificación

### Testing
- [ ] 🔲 Test unitarios `EmailService`
- [ ] 🔲 Test unitarios `VerificacionEmailService`
- [ ] 🔲 Test integración `VerificacionEmailController`
- [ ] 🔲 Test end-to-end del flujo completo
- [ ] 🔲 Validar emails enviados en Gmail

### Documentación
- [ ] 🔲 Actualizar modelo de datos (06-modelo-datos-completo.md)
- [ ] 🔲 Actualizar manual de desarrollo
- [ ] 🔲 Actualizar guía de despliegue
- [ ] 🔲 Actualizar CHANGELOG con v2.1
- [ ] 🔲 Actualizar README con nueva funcionalidad

---

## 🎯 Próximos Pasos

### 1. Generar Imágenes (AHORA)
```bash
# Usar Mermaid Live Editor
https://mermaid.live/
# O instalar CLI y ejecutar:
npm install -g @mermaid-js/mermaid-cli
```

### 2. Revisar y Validar Diagramas
- Verificar relaciones entre entidades
- Confirmar cardinalidades
- Validar atributos y métodos

### 3. Comenzar Implementación
**Orden recomendado:**
1. Modelo de datos (VerificacionEmail, TipoVerificacion)
2. Repositorio y servicios base
3. Configuración SMTP
4. Lógica de negocio (generación tokens, validación)
5. Controladores y endpoints
6. Plantillas de email
7. Vistas web
8. Testing
9. Documentación

---

## 📊 Estimación Total

| Fase | Tiempo Estimado |
|------|----------------|
| Modelo de Datos | 1-2 horas |
| Repositorios/Servicios | 3-4 horas |
| Configuración SMTP | 1 hora |
| Controladores/Vistas | 4-5 horas |
| Testing | 3-4 horas |
| Documentación | 2-3 horas |
| **TOTAL** | **14-19 horas** |

---

## 🔍 Recursos Útiles

### Documentación Oficial
- Spring Mail: https://docs.spring.io/spring-boot/docs/current/reference/html/io.html#io.email
- Mermaid: https://mermaid.js.org/
- Gmail SMTP: https://support.google.com/mail/answer/7126229

### Herramientas
- Mermaid Live Editor: https://mermaid.live/
- Kroki: https://kroki.io/
- MailTrap (testing): https://mailtrap.io/

---

## 📞 Contacto y Soporte

**Autor:** Carlos Díaz Oller  
**Proyecto:** Portfolio Social v2.1  
**Fecha:** Febrero 2026

---

*Nota: Este documento se actualizará según avance la implementación.*

