# 📋 Integración LinkedIn - Portfolio Social

## 📊 Resumen

| Característica | Estado |
|----------------|--------|
| Compartir en LinkedIn | ✅ Disponible |
| Publicar en perfil personal | ✅ Disponible |
| Publicar en página de empresa | ✅ Disponible (selección manual) |

## 🎯 Cómo Funciona

Cuando haces clic en **"Compartir en LinkedIn"** desde un proyecto:

1. Se abre una ventana de LinkedIn con el enlace del proyecto
2. Puedes escribir un comentario personalizado
3. **Para publicar en tu página de empresa:**
   - Haz clic en el selector "Publicar como: [Tu nombre]"
   - Selecciona tu página de empresa
4. Haz clic en **Publicar**

## 🔧 Configuración

```properties
# application.properties
linkedin.enabled=true
linkedin.test-mode=false
```

## 📝 Notas Técnicas

### Limitaciones de la API de LinkedIn

LinkedIn requiere el producto "Community Management API" para publicar automáticamente en páginas de empresa mediante API. Este producto no está disponible para solicitar (botón deshabilitado).

**Solución implementada:** Usamos el diálogo de compartir nativo de LinkedIn, que permite al usuario seleccionar si publicar como persona o como página de empresa.

### Ventajas de esta solución

- ✅ No requiere permisos especiales de API
- ✅ El usuario tiene control total sobre la publicación
- ✅ Puede seleccionar cualquier página de empresa que administre
- ✅ Puede personalizar el mensaje antes de publicar

## 🔗 Enlaces

- [App LinkedIn Developer](https://www.linkedin.com/developers/apps/228942413)
- [Página de Empresa](https://www.linkedin.com/company/111341630/)

---

*Última actualización: 08/02/2026*

