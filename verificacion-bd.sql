-- Script de verificación de Base de Datos
-- Portfolio v2.6.0 - Verificación de Email

-- =====================================================
-- 1. VERIFICAR CONEXIÓN A LA BASE DE DATOS
-- =====================================================
SELECT version();
SELECT current_database();

-- =====================================================
-- 2. LISTAR TODAS LAS TABLAS
-- =====================================================
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public'
ORDER BY table_name;

-- =====================================================
-- 3. VERIFICAR ESTRUCTURA DE LA TABLA USUARIOS
-- =====================================================
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_name = 'usuarios'
ORDER BY ordinal_position;

-- =====================================================
-- 4. VERIFICAR SI EXISTE COLUMNA email_verificado
-- =====================================================
SELECT column_name, data_type, column_default
FROM information_schema.columns
WHERE table_name = 'usuarios'
  AND column_name = 'email_verificado';

-- =====================================================
-- 5. VERIFICAR TABLA verificaciones_email
-- =====================================================
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_name = 'verificaciones_email'
ORDER BY ordinal_position;

-- =====================================================
-- 6. VERIFICAR CONSTRAINTS
-- =====================================================
SELECT constraint_name, constraint_type
FROM information_schema.table_constraints
WHERE table_name = 'verificaciones_email';

-- =====================================================
-- 7. CONTAR REGISTROS
-- =====================================================
SELECT
    'usuarios' as tabla,
    COUNT(*) as total_registros
FROM usuarios
UNION ALL
SELECT
    'verificaciones_email' as tabla,
    COUNT(*) as total_registros
FROM verificaciones_email;

-- =====================================================
-- 8. VERIFICAR ÍNDICES
-- =====================================================
SELECT indexname, indexdef
FROM pg_indexes
WHERE tablename IN ('usuarios', 'verificaciones_email');

-- =====================================================
-- INSTRUCCIONES DE USO:
-- =====================================================
-- Para ejecutar este script desde la línea de comandos:
-- psql -U carlos -d portfolio -f verificacion-bd.sql

-- Para ejecutar desde pgAdmin o DBeaver:
-- Simplemente abre este archivo y ejecuta las consultas

-- RESULTADO ESPERADO:
-- 1. ✅ tabla 'usuarios' debe tener columna 'email_verificado'
-- 2. ✅ tabla 'verificaciones_email' debe existir con columnas:
--    - id, usuario_id, token, fecha_creacion, fecha_expiracion, usado, tipo
-- 3. ✅ Constraint UNIQUE en token
-- 4. ✅ Foreign key de verificaciones_email a usuarios

