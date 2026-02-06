# 🚀 Guía de Pruebas - Verificación de Email v2.6.0

**Fecha:** 06/02/2026  
**Estado:** Aplicación compilada y lista para probar

---

## ✅ ESTADO ACTUAL

### Compilación
- ✅ **BUILD SUCCESS**
- ✅ **46 archivos compilados** (+1 nuevo: VerificacionEmailController)
- ✅ **61 recursos copiados** (+5 nuevas vistas HTML)
- ✅ Sin errores de compilación

### Aplicación
- 🔄 Ejecutándose en segundo plano
- 🔗 **URL:** http://localhost:8089
- 📧 **Email configurado:** carlosiuka88@gmail.com
- 🔑 **App Password:** yguc ccvn dsja dclu

---

## 🧪 GUÍA DE PRUEBAS PASO A PASO

### 📋 PRUEBA 1: Registro de Usuario con Verificación

#### Paso 1: Acceder a la aplicación
1. Abrir navegador
2. Ir a: http://localhost:8089
3. Deberías ver la página de login

#### Paso 2: Ir a registro
1. Click en "Regístrate aquí"
2. Deberías ver el formulario de registro

#### Paso 3: Registrar nuevo usuario
**Datos de prueba:**
```
Nombre: Usuario Prueba
Email: [tu_email_personal]@gmail.com  (⚠️ Usa tu email real para recibir el correo)
Password: prueba123
```

#### Paso 4: Verificar mensaje de confirmación
Después de registrarte, deberías ver en el login:
```
✓ Registro exitoso
📧 Hemos enviado un email de verificación a tu correo.
Por favor, verifica tu email antes de iniciar sesión.
```

#### Paso 5: Verificar email recibido
1. **Revisar tu bandeja de entrada** (puede tardar 10-30 segundos)
2. **Si no llega**, revisar carpeta de SPAM
3. **Email debe tener:**
   - Asunto: "Verifica tu cuenta - Portfolio Social"
   - Remitente: carlosiuka88@gmail.com
   - Botón verde: "✅ Verificar mi cuenta"
   - Enlace de respaldo

#### Paso 6: Hacer clic en el enlace
1. Click en el botón o copiar el enlace
2. Deberías ver página con:
   - ✅ Icono verde animado
   - "¡Verificación Exitosa!"
   - Lista de funcionalidades
   - Botón "🚀 Iniciar Sesión"

#### Paso 7: Iniciar sesión
1. Click en "Iniciar Sesión"
2. Ingresar email y contraseña
3. ✅ **Deberías poder acceder al dashboard**

---

### 📋 PRUEBA 2: Reenvío de Verificación

#### Escenario: Usuario no recibió el email

#### Paso 1: Ir a reenviar verificación
1. En el login, click en "📧 Reenviar email de verificación"
2. Deberías ver formulario con campo de email

#### Paso 2: Ingresar email
1. Ingresar el email que registraste
2. Click en "📤 Enviar Enlace de Verificación"

#### Paso 3: Verificar mensaje
Deberías ver:
```
✓ Email de verificación reenviado. Revisa tu bandeja de entrada.
```

#### Paso 4: Verificar nuevo email
1. Revisar bandeja (puede tardar 10-30 segundos)
2. Debería llegar un nuevo email
3. El token será diferente al anterior

---

### 📋 PRUEBA 3: Recuperación de Contraseña

#### Paso 1: Ir a recuperación
1. En el login, click en "¿Olvidaste tu contraseña?"
2. Deberías ver formulario de recuperación (fondo naranja)

#### Paso 2: Solicitar recuperación
1. Ingresar tu email
2. Click en "📧 Enviar Enlace de Recuperación"

#### Paso 3: Verificar email de recuperación
**Email debe tener:**
- Asunto: "Recuperación de contraseña - Portfolio Social"
- Botón amarillo: "🔑 Restablecer contraseña"
- Advertencia de 24 horas

#### Paso 4: Cambiar contraseña
1. Click en el enlace
2. Deberías ver formulario (fondo púrpura)
3. Ingresar nueva contraseña (mínimo 6 caracteres)
4. Repetir contraseña
5. **Validación en tiempo real:** Si no coinciden, botón se deshabilita

#### Paso 5: Confirmar cambio
1. Click en "💾 Guardar Nueva Contraseña"
2. Deberías ver mensaje: "Contraseña cambiada exitosamente"

#### Paso 6: Login con nueva contraseña
1. Iniciar sesión con la nueva contraseña
2. ✅ **Deberías poder acceder**

---

### 📋 PRUEBA 4: Errores y Validaciones

#### Error 1: Token expirado (después de 24h)
**Esperado:**
- Página de error (roja)
- Mensaje: "Este enlace ha expirado"
- Botón para reenviar

#### Error 2: Token ya usado
**Esperado:**
- Página de error (roja)
- Mensaje: "Este enlace ya fue utilizado"

#### Error 3: Email no existe (recuperación)
**Esperado:**
- Mensaje genérico (por seguridad): "Si el email existe, recibirás instrucciones"

#### Error 4: Email ya verificado
**Esperado:**
- Mensaje: "Tu cuenta ya está verificada"

---

## 🔍 VERIFICACIONES EN BASE DE DATOS

### Usando pgAdmin o DBeaver

#### 1. Verificar usuario creado
```sql
SELECT id, nombre, email, email_verificado, estado
FROM usuarios
WHERE email = '[tu_email]@gmail.com';
```

**Antes de verificar:** `email_verificado = false`  
**Después de verificar:** `email_verificado = true`

#### 2. Verificar token creado
```sql
SELECT id, token, fecha_creacion, fecha_expiracion, usado, tipo
FROM verificaciones_email
WHERE usuario_id = [id_usuario];
```

**Antes de usar:** `usado = false`  
**Después de usar:** `usado = true`

#### 3. Verificar todas las tablas
```sql
\dt  -- En psql
-- O en pgAdmin: Ver panel izquierdo → Tables
```

**Deberías ver:**
- usuarios
- proyectos
- cvs
- votos
- favoritos
- publicaciones_rrss
- usuario_enlaces_rrss
- **verificaciones_email** ⭐ NUEVA

---

## 📧 EJEMPLO DE EMAIL RECIBIDO

### Email de Verificación
```
De: carlosiuka88@gmail.com
Para: [tu_email]
Asunto: Verifica tu cuenta - Portfolio Social

[HTML con diseño profesional]

🎯 Verifica tu cuenta

Hola [Nombre],

Gracias por registrarte en Portfolio Social.

Para activar tu cuenta, por favor haz clic en el siguiente botón:

[✅ Verificar mi cuenta] (botón verde grande)

O copia y pega este enlace:
http://localhost:8089/verificar-email?token=abc123...

⚠️ Este enlace expira en 24 horas.

Portfolio Social © 2026 | Carlos Díaz Oller
```

### Email de Bienvenida (después de verificar)
```
De: carlosiuka88@gmail.com
Para: [tu_email]
Asunto: ¡Bienvenido a Portfolio Social!

🎉 ¡Bienvenido!

Hola [Nombre],

¡Tu cuenta ha sido verificada exitosamente!

Ya puedes acceder a todas las funcionalidades:
- ✅ Crear y publicar proyectos
- ✅ Subir tu CV
- ✅ Votar proyectos
- ✅ Guardar favoritos
- ✅ Compartir en redes sociales

[🚀 Ir a mi dashboard] (botón azul)
```

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### Problema 1: No llega el email
**Causas posibles:**
1. Email en carpeta SPAM → Revisar carpeta de spam
2. Email incorrecto → Verificar que escribiste bien tu email
3. Gmail bloqueando → Verificar configuración SMTP
4. Firewall → Verificar que el puerto 587 está abierto

**Verificar en logs:**
```bash
# Buscar en consola de Spring Boot:
📧 Email de verificación enviado a: [email]
```

### Problema 2: Error al enviar email
**Si ves en logs:**
```
⚠️ Error al enviar email de verificación
```

**Verificar:**
1. App Password es correcto: `yguc ccvn dsja dclu`
2. Verificación en 2 pasos activada en Gmail
3. Internet funciona correctamente

### Problema 3: Token inválido
**Si ves página de error:**
- Verifica que copiaste el enlace completo
- El token puede tener expiración de 24h
- El token solo se puede usar una vez

### Problema 4: No puedo hacer login después de verificar
**Verificar en BD:**
```sql
SELECT email_verificado FROM usuarios WHERE email = '[tu_email]';
```

Si es `false`, el proceso de verificación falló. Intenta reenviar.

---

## 📊 CHECKLIST DE PRUEBAS

### Funcionalidad Básica
- [ ] Usuario se puede registrar
- [ ] Email de verificación llega
- [ ] Link de verificación funciona
- [ ] Página de éxito se muestra
- [ ] Email de bienvenida llega
- [ ] Usuario puede hacer login después de verificar

### Reenvío de Verificación
- [ ] Formulario se muestra correctamente
- [ ] Email se puede reenviar
- [ ] Nuevo token se genera
- [ ] Mensaje de confirmación aparece

### Recuperación de Contraseña
- [ ] Formulario se muestra correctamente
- [ ] Email de recuperación llega
- [ ] Formulario de nueva contraseña funciona
- [ ] Validación en tiempo real funciona
- [ ] Contraseña se actualiza correctamente
- [ ] Login con nueva contraseña funciona

### Errores y Validaciones
- [ ] Token expirado muestra error apropiado
- [ ] Token usado muestra error apropiado
- [ ] Email no existe (mensaje genérico)
- [ ] Validación de contraseñas coincidentes

### Visual y UX
- [ ] Diseño responsive en móvil
- [ ] Animaciones funcionan
- [ ] Colores son consistentes
- [ ] Botones tienen hover effect
- [ ] Mensajes son claros

---

## 🎯 RESULTADOS ESPERADOS

### ✅ TODO FUNCIONA SI:
1. ✅ Emails llegan en menos de 30 segundos
2. ✅ Tokens se validan correctamente
3. ✅ `email_verificado` cambia a `true` en BD
4. ✅ Usuario puede hacer login después de verificar
5. ✅ Formularios tienen validación
6. ✅ Diseño es responsive y profesional
7. ✅ Todos los flujos funcionan sin errores

### ❌ HAY PROBLEMAS SI:
1. ❌ Emails no llegan (problema SMTP)
2. ❌ Tokens no se validan (problema en BD)
3. ❌ `email_verificado` no cambia (problema en servicio)
4. ❌ Usuario no puede login (falta validación)
5. ❌ Errores 500 en consola (problema en código)

---

## 📞 SIGUIENTE PASO DESPUÉS DE PROBAR

### Si TODO funciona:
1. ✅ Documentar en CHANGELOG (v2.6.0)
2. ✅ Actualizar README con nueva funcionalidad
3. ✅ Crear tests automatizados (Fase 6)
4. ✅ (Opcional) Agregar validación de email en login

### Si hay PROBLEMAS:
1. ❌ Revisar logs de Spring Boot
2. ❌ Verificar configuración SMTP
3. ❌ Revisar base de datos
4. ❌ Documentar bugs encontrados

---

## 🔗 ACCESOS RÁPIDOS

- **Aplicación:** http://localhost:8089
- **Login:** http://localhost:8089/login
- **Registro:** http://localhost:8089/register
- **Reenviar verificación:** http://localhost:8089/reenviar-verificacion
- **Recuperar password:** http://localhost:8089/solicitar-recuperacion

---

## 📝 NOTAS IMPORTANTES

1. ⚠️ **Usa tu email personal real** para recibir los correos
2. ⏱️ Los tokens expiran en **24 horas**
3. 🔒 Los tokens solo se pueden usar **una vez**
4. 📧 Los emails pueden tardar **10-30 segundos** en llegar
5. 📁 Revisar **carpeta de SPAM** si no llega el email
6. 💾 La BD debe tener la tabla **verificaciones_email**
7. ✉️ El email debe estar configurado correctamente en **application.properties**

---

**Documento creado:** 06/02/2026  
**Propósito:** Guía completa para probar la verificación de email  
**Versión:** 2.6.0 (beta)

---

*¡Sigue esta guía para probar todas las funcionalidades! 🧪*

