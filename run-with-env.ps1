# ========================================
# Script para ejecutar la aplicación con variables de entorno
# ========================================

Write-Host "🚀 Cargando variables de entorno desde .env..." -ForegroundColor Cyan

# Leer el archivo .env
$envFile = ".\.env"
if (Test-Path $envFile) {
    Get-Content $envFile | ForEach-Object {
        if ($_ -match '^([^#][^=]+)=(.*)$') {
            $name = $matches[1].Trim()
            $value = $matches[2].Trim()
            [System.Environment]::SetEnvironmentVariable($name, $value, [System.EnvironmentVariableTarget]::Process)
            Write-Host "  ✅ $name configurado" -ForegroundColor Green
        }
    }
} else {
    Write-Host "  ❌ Archivo .env no encontrado" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "🏃 Ejecutando aplicación Spring Boot..." -ForegroundColor Cyan
Write-Host ""

# Ejecutar la aplicación con Maven
mvn spring-boot:run

