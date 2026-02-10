package es.fempa.acd.demosecurityproductos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Servicio para integración con LinkedIn API
 * Maneja la publicación de proyectos en LinkedIn
 *
 * @version 3.0.0 - MVP LinkedIn
 * @date 06/02/2026
 */
@Service
public class LinkedInService {

    private static final Logger logger = LoggerFactory.getLogger(LinkedInService.class);

    @Value("${linkedin.enabled:false}")
    private boolean linkedInEnabled;

    @Value("${linkedin.test-mode:true}")
    private boolean testMode;

    @Value("${linkedin.access-token:}")
    private String accessToken;

    @Value("${linkedin.api-base-url:https://api.linkedin.com/v2}")
    private String apiBaseUrl;

    @Value("${linkedin.publication-type:personal}")
    private String publicationType;

    @Value("${linkedin.organization-id:}")
    private String organizationId;

    private final RestTemplate restTemplate;

    public LinkedInService() {
        // RestTemplate simple - el control de Content-Length se hace en publicarPost
        this.restTemplate = new RestTemplate();
    }

    /**
     * Publica un post en LinkedIn
     *
     * @param titulo Título del proyecto
     * @param descripcion Descripción del proyecto
     * @param proyectoId ID del proyecto
     * @return Map con "id" (ID externo) y "url" (URL de la publicación)
     */
    public Map<String, String> publicarPost(String titulo, String descripcion, Long proyectoId) {
        Map<String, String> resultado = new HashMap<>();

        // MODO TEST - Simula la publicación
        if (testMode || !linkedInEnabled) {
            logger.info("MODO TEST: Simulando publicación en LinkedIn");
            logger.info("  Título: {}", titulo);
            logger.info("  Proyecto ID: {}", proyectoId);

            String simulatedId = "urn:li:ugcPost:" + UUID.randomUUID().toString();
            String simulatedUrl = "https://www.linkedin.com/feed/update/" + simulatedId;

            resultado.put("id", simulatedId);
            resultado.put("url", simulatedUrl);
            resultado.put("success", "true");
            resultado.put("mode", "TEST");

            logger.info("✅ Publicación simulada exitosamente");
            logger.info("  ID: {}", simulatedId);
            logger.info("  URL: {}", simulatedUrl);

            return resultado;
        }

        // MODO PRODUCCIÓN - Publicación real en LinkedIn
        try {
            logger.info("🚀 Publicando en LinkedIn REAL (API v2)");
            logger.info("  Título: {}", titulo);
            logger.info("  Proyecto ID: {}", proyectoId);
            logger.info("  Tipo de publicación: {}", publicationType);
            if ("organization".equals(publicationType)) {
                logger.info("  Organization ID: {}", organizationId);
            }

            // Validar token
            if (accessToken == null || accessToken.isEmpty()) {
                throw new IllegalStateException("Access token de LinkedIn no configurado");
            }

            // Log token info (sin exponer el token completo)
            logger.info("  Token length: {} chars", accessToken.length());
            logger.info("  Token prefix: {}...", accessToken.substring(0, Math.min(10, accessToken.length())));

            // Construir el texto del post
            String textoPost = construirTextoPost(titulo, descripcion, proyectoId);
            logger.info("  Texto del post: {} chars", textoPost.length());

            // Construir el payload para API v2 UGC Posts SIN campo author
            // Con w_member_social, omitir el campo author y LinkedIn lo infiere del token
            Map<String, Object> payload = construirPayloadLinkedInV2(textoPost);

            // Serializar a JSON String manualmente para tener control total
            String jsonPayload = convertirAJson(payload);
            logger.info("  JSON Payload length: {} bytes", jsonPayload.getBytes(java.nio.charset.StandardCharsets.UTF_8).length);

            // Headers correctos para LinkedIn UGC Posts API
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);
            headers.set("X-Restli-Protocol-Version", "2.0.0"); // Requerido para UGC API
            headers.set("LinkedIn-Version", "202401");
            headers.setConnection("close");
            headers.setContentLength(jsonPayload.getBytes(java.nio.charset.StandardCharsets.UTF_8).length);

            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            // Usar LinkedIn UGC Posts API - SIN campo author (inferido del token)
            String url = "https://api.linkedin.com/v2/ugcPosts";
            logger.info("🔗 Llamando a: {}", url);
            logger.info("📋 Headers: X-Restli-Protocol-Version=2.0.0");
            logger.info("📦 Payload preview: {}...", jsonPayload.substring(0, Math.min(200, jsonPayload.length())));

            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
            );

            // Procesar respuesta
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                String postId = responseBody != null ? (String) responseBody.get("id") : "ugc-post-" + UUID.randomUUID();
                String postUrl = "https://www.linkedin.com/feed/update/" + postId;

                resultado.put("id", postId);
                resultado.put("url", postUrl);
                resultado.put("success", "true");
                resultado.put("mode", "PRODUCTION");

                logger.info("✅ Publicación en LinkedIn exitosa");
                logger.info("  ID: {}", postId);
                logger.info("  URL: {}", postUrl);
            } else {
                throw new RuntimeException("LinkedIn API respondió con código: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException e) {
            logger.error("❌ Error HTTP al publicar en LinkedIn");
            logger.error("   Código HTTP: {}", e.getStatusCode());
            logger.error("   Status Text: {}", e.getStatusText());
            logger.error("   Response Body: {}", e.getResponseBodyAsString());
            logger.error("   Headers: {}", e.getResponseHeaders());

            String errorMsg = String.format("Error %s: %s", e.getStatusCode(), e.getResponseBodyAsString());
            resultado.put("success", "false");
            resultado.put("error", errorMsg);
            throw new RuntimeException("Error al publicar en LinkedIn: " + errorMsg, e);
        } catch (Exception e) {
            logger.error("❌ Error inesperado al publicar en LinkedIn");
            logger.error("   Tipo: {}", e.getClass().getName());
            logger.error("   Mensaje: {}", e.getMessage());
            logger.error("   Stack trace:", e);

            resultado.put("success", "false");
            resultado.put("error", e.getMessage());
            throw new RuntimeException("Error al publicar en LinkedIn: " + e.getMessage(), e);
        }

        return resultado;
    }


    /**
     * Construye el payload para LinkedIn UGC Posts API
     * Soporta publicación en perfil personal O página de empresa
     *
     * Documentación: https://learn.microsoft.com/en-us/linkedin/consumer/integrations/self-serve/share-on-linkedin
     */
    private Map<String, Object> construirPayloadLinkedInV2(String texto) {
        Map<String, Object> payload = new HashMap<>();

        // Determinar el author según el tipo de publicación
        if ("organization".equals(publicationType) && organizationId != null && !organizationId.isEmpty()) {
            // Publicar en página de empresa
            String authorUrn = "urn:li:organization:" + organizationId;
            payload.put("author", authorUrn);
            logger.info("📢 Configurado para publicar en ORGANIZACIÓN: {}", authorUrn);
        } else {
            // Publicar en perfil personal - NO incluir author, LinkedIn lo infiere del token
            logger.info("👤 Configurado para publicar en PERFIL PERSONAL (author inferido del token)");
        }

        // lifecycleState: PUBLISHED
        payload.put("lifecycleState", "PUBLISHED");

        // specificContent - Formato UGC
        Map<String, Object> specificContent = new HashMap<>();
        Map<String, Object> shareContent = new HashMap<>();

        // shareCommentary
        Map<String, String> shareCommentary = new HashMap<>();
        shareCommentary.put("text", texto);
        shareContent.put("shareCommentary", shareCommentary);

        // shareMediaCategory - NONE (sin media)
        shareContent.put("shareMediaCategory", "NONE");

        specificContent.put("com.linkedin.ugc.ShareContent", shareContent);
        payload.put("specificContent", specificContent);

        // visibility - PUBLIC
        Map<String, String> visibility = new HashMap<>();
        visibility.put("com.linkedin.ugc.MemberNetworkVisibility", "PUBLIC");
        payload.put("visibility", visibility);

        logger.info("✅ Payload UGC construido - Tipo: {}", publicationType);
        return payload;
    }


    /**
     * Convierte un Map a JSON String manualmente
     * Esto permite tener control total sobre la serialización y evita problemas de Transfer-Encoding
     */
    private String convertirAJson(Map<String, Object> map) {
        try {
            // Usar Jackson ObjectMapper para serializar
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException("Error al serializar JSON", e);
        }
    }

    /**
     * Construye el texto del post para LinkedIn
     * Limita el tamaño total a 900 caracteres para evitar problemas con la BD (límite 1000)
     */
    private String construirTextoPost(String titulo, String descripcion, Long proyectoId) {
        final int LIMITE_CARACTERES = 900;

        logger.info("📥 INPUT - Título: {} chars, Descripción: {} chars",
            titulo != null ? titulo.length() : 0,
            descripcion != null ? descripcion.length() : 0);

        // Componentes del post
        String prefijo = "🚀 ";
        String footer = "\n\n🔗 Ver proyecto: http://localhost:8089/proyectos/" + proyectoId + "\n\n#desarrollo #portfolio #java #springboot";
        String separador = "\n\n";

        // Calcular espacio fijo (prefijo + separador + footer)
        int espacioFijo = prefijo.length() + separador.length() + footer.length();
        int espacioDisponible = LIMITE_CARACTERES - espacioFijo;

        logger.info("📏 Espacio fijo: {} chars, Espacio disponible: {} chars", espacioFijo, espacioDisponible);

        // Asegurar que hay espacio mínimo
        if (espacioDisponible < 20) {
            logger.error("❌ Error: el footer es demasiado largo");
            return "🚀 Proyecto publicado\n\n#desarrollo #portfolio";
        }

        // Truncar título si es necesario (reservar al menos 10 chars para descripción)
        String tituloFinal = titulo != null ? titulo : "";
        int maxTitulo = espacioDisponible - 10; // Reservar espacio para descripción
        if (tituloFinal.length() > maxTitulo) {
            tituloFinal = tituloFinal.substring(0, maxTitulo - 3) + "...";
            logger.warn("⚠️ Título truncado de {} a {} caracteres", titulo.length(), tituloFinal.length());
        }

        // Calcular espacio restante para descripción
        int espacioDesc = espacioDisponible - tituloFinal.length();
        logger.info("📐 Título final: {} chars, Espacio para descripción: {} chars", tituloFinal.length(), espacioDesc);

        // Truncar descripción si es necesario
        String desc = descripcion != null ? descripcion : "";
        if (desc.length() > espacioDesc) {
            if (espacioDesc > 3) {
                desc = desc.substring(0, espacioDesc - 3) + "...";
                logger.warn("⚠️ Descripción truncada de {} a {} caracteres", descripcion.length(), desc.length());
            } else {
                desc = "";
                logger.warn("⚠️ Descripción eliminada - no hay espacio");
            }
        }

        // Construir el texto final
        String resultado = prefijo + tituloFinal + separador + desc + footer;

        logger.info("📦 Componentes - Prefijo: {}, Título: {}, Sep: {}, Desc: {}, Footer: {}",
            prefijo.length(), tituloFinal.length(), separador.length(), desc.length(), footer.length());

        // Verificación de seguridad final
        if (resultado.length() > LIMITE_CARACTERES) {
            logger.error("❌ ERROR CRÍTICO: El texto supera el límite ({} > {}). Truncando forzosamente.",
                resultado.length(), LIMITE_CARACTERES);
            resultado = resultado.substring(0, LIMITE_CARACTERES - 3) + "...";
        }

        logger.info("✅ Texto del post construido: {} caracteres (límite: {})", resultado.length(), LIMITE_CARACTERES);
        logger.debug("📄 Texto completo:\n{}", resultado);

        return resultado;
    }


    /**
     * Verifica si el servicio está configurado correctamente
     */
    public boolean estaConfigurado() {
        if (testMode) {
            logger.info("LinkedIn en MODO TEST");
            return true;
        }

        boolean configurado = linkedInEnabled && accessToken != null && !accessToken.isEmpty();

        if (configurado) {
            logger.info("LinkedIn configurado correctamente (PRODUCCIÓN)");
        } else {
            logger.warn("LinkedIn NO configurado o deshabilitado");
        }

        return configurado;
    }

    /**
     * Obtiene información de configuración (para debugging)
     */
    public Map<String, Object> obtenerConfiguracion() {
        Map<String, Object> config = new HashMap<>();
        config.put("enabled", linkedInEnabled);
        config.put("testMode", testMode);
        config.put("tokenConfigured", accessToken != null && !accessToken.isEmpty());
        config.put("apiBaseUrl", apiBaseUrl);
        return config;
    }
}

