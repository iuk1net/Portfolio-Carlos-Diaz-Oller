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

    private final RestTemplate restTemplate;

    public LinkedInService() {
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
            logger.info("🚀 Publicando en LinkedIn REAL");
            logger.info("  Título: {}", titulo);
            logger.info("  Proyecto ID: {}", proyectoId);

            // Validar token
            if (accessToken == null || accessToken.isEmpty()) {
                throw new IllegalStateException("Access token de LinkedIn no configurado");
            }

            // Construir el texto del post
            String textoPost = construirTextoPost(titulo, descripcion, proyectoId);

            // Construir el payload para LinkedIn API
            Map<String, Object> payload = construirPayloadLinkedIn(textoPost);

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);
            headers.set("X-Restli-Protocol-Version", "2.0.0");

            // Request
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            // Llamar a LinkedIn API
            String url = apiBaseUrl + "/ugcPosts";
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
            );

            // Procesar respuesta
            if (response.getStatusCode() == HttpStatus.CREATED) {
                Map<String, Object> responseBody = response.getBody();
                String postId = (String) responseBody.get("id");
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
            logger.error("❌ Error HTTP al publicar en LinkedIn: {}", e.getStatusCode());
            logger.error("   Mensaje: {}", e.getResponseBodyAsString());
            resultado.put("success", "false");
            resultado.put("error", e.getMessage());
            throw new RuntimeException("Error al publicar en LinkedIn: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("❌ Error inesperado al publicar en LinkedIn", e);
            resultado.put("success", "false");
            resultado.put("error", e.getMessage());
            throw new RuntimeException("Error al publicar en LinkedIn: " + e.getMessage(), e);
        }

        return resultado;
    }

    /**
     * Construye el texto del post para LinkedIn
     */
    private String construirTextoPost(String titulo, String descripcion, Long proyectoId) {
        StringBuilder texto = new StringBuilder();
        texto.append("🚀 ").append(titulo).append("\n\n");

        // Limitar descripción a 300 caracteres
        String desc = descripcion != null ? descripcion : "";
        if (desc.length() > 300) {
            desc = desc.substring(0, 297) + "...";
        }
        texto.append(desc).append("\n\n");

        texto.append("🔗 Ver proyecto: http://localhost:8089/proyectos/").append(proyectoId).append("\n\n");
        texto.append("#desarrollo #portfolio #java #springboot");

        return texto.toString();
    }

    /**
     * Construye el payload para la API de LinkedIn
     * Según documentación: https://docs.microsoft.com/en-us/linkedin/consumer/integrations/self-serve/share-on-linkedin
     */
    private Map<String, Object> construirPayloadLinkedIn(String texto) {
        Map<String, Object> payload = new HashMap<>();

        // Author (urn:li:person:CURRENT_USER se obtiene del token)
        payload.put("author", "urn:li:person:CURRENT_USER");

        // Lifecycle state
        payload.put("lifecycleState", "PUBLISHED");

        // Specific content
        Map<String, Object> specificContent = new HashMap<>();
        Map<String, Object> shareContent = new HashMap<>();
        Map<String, String> shareCommentary = new HashMap<>();
        shareCommentary.put("text", texto);
        shareContent.put("shareCommentary", shareCommentary);
        shareContent.put("shareMediaCategory", "NONE");
        specificContent.put("com.linkedin.ugc.ShareContent", shareContent);
        payload.put("specificContent", specificContent);

        // Visibility
        Map<String, String> visibility = new HashMap<>();
        visibility.put("com.linkedin.ugc.MemberNetworkVisibility", "PUBLIC");
        payload.put("visibility", visibility);

        return payload;
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

