#!/usr/bin/env pwsh
# Script para subir cambios a GitHub

Set-Location "C:\Users\USUARIO\IdeaProjects\2ª Evaluacion\demoSecurityProductos"

Write-Host "=== Verificando estado de Git ===" -ForegroundColor Cyan
git status

Write-Host "`n=== Agregando archivos ===" -ForegroundColor Cyan
git add .

Write-Host "`n=== Creando commit ===" -ForegroundColor Cyan
git commit -m "docs: profesionalizar documentación y verificar implementación UML completa"

Write-Host "`n=== Configurando remoto (si no existe) ===" -ForegroundColor Cyan
$remoteExists = git remote | Select-String -Pattern "origin" -Quiet
if (-not $remoteExists) {
    git remote add origin https://github.com/iuk1net/Portfolio-Carlos-Diaz-Oller.git
    Write-Host "Remoto 'origin' configurado" -ForegroundColor Green
} else {
    Write-Host "Remoto 'origin' ya existe" -ForegroundColor Yellow
}

Write-Host "`n=== Verificando remoto ===" -ForegroundColor Cyan
git remote -v

Write-Host "`n=== Asegurando rama main ===" -ForegroundColor Cyan
git branch -M main

Write-Host "`n=== Subiendo a GitHub ===" -ForegroundColor Cyan
git push -u origin main

Write-Host "`n=== Estado final ===" -ForegroundColor Cyan
git status

Write-Host "`n=== Últimos commits ===" -ForegroundColor Cyan
git log --oneline -3

Write-Host "`n✅ Proceso completado" -ForegroundColor Green

