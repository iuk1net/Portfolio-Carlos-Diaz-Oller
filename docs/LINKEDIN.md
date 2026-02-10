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

LinkedIn requiere el producto "**Community Management API**" para publicar automáticamente en páginas de empresa mediante API. Este producto **no está disponible para solicitar** (botón deshabilitado en el Developer Portal).

#### Error encontrado al intentar usar la API ugcPosts:
```
Error al publicar en LinkedIn: 500 Server Error on POST request for 
"https://api.linkedin.com/v2/ugcPosts": "{"message":"Internal Server Error","status":500}"
```

#### ¿Por qué ocurre este error?
1. La aplicación tiene el producto "**Share on LinkedIn**" (Default Tier) activado
2. Este producto solo permite compartir mediante el diálogo web nativo
3. Para publicaciones automáticas vía API se necesita "**Community Management API**"
4. El botón "Request access" de Community Management API está **deshabilitado**

**Solución implementada:** Usamos el diálogo de compartir nativo de LinkedIn, que permite al usuario seleccionar si publicar como persona o como página de empresa.

### Ventajas de esta solución

- ✅ No requiere permisos especiales de API
- ✅ El usuario tiene control total sobre la publicación
- ✅ Puede seleccionar cualquier página de empresa que administre
- ✅ Puede personalizar el mensaje antes de publicar

## 🔗 Enlaces

- [LinkedIn Developer Portal](https://www.linkedin.com/developers/apps/)
- [Documentación Share on LinkedIn](https://learn.microsoft.com/en-us/linkedin/consumer/integrations/self-serve/share-on-linkedin)

## ⚠️ Estado Actual

| Funcionalidad | Estado | Notas |
|---------------|--------|-------|
| Compartir manual | ✅ Funciona | Diálogo nativo de LinkedIn |
| Publicar vía API (personal) | ❌ No disponible | Requiere permisos especiales |
| Publicar vía API (empresa) | ❌ No disponible | Requiere Community Management API |

---

*Última actualización: 10/02/2026*

