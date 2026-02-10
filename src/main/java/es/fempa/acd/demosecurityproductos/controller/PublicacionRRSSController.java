package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.PublicacionRRSS;
import es.fempa.acd.demosecurityproductos.service.ProyectoService;
import es.fempa.acd.demosecurityproductos.service.PublicacionRRSSService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * API REST Controller para publicación en redes sociales
 * Proporciona endpoints AJAX para compartir proyectos en RRSS
 *
 * @author Portfolio Social v3.0
 * @version 3.0.0 - LinkedIn Integration
 */
@RestController
@RequestMapping("/api/publicaciones")
@PreAuthorize("isAuthenticated()")
public class PublicacionRRSSController {

    private final PublicacionRRSSService publicacionRRSSService;
    private final ProyectoService proyectoService;

    public PublicacionRRSSController(PublicacionRRSSService publicacionRRSSService,
                                    ProyectoService proyectoService) {
        this.publicacionRRSSService = publicacionRRSSService;
        this.proyectoService = proyectoService;
    }

    /**
     * Publica un proyecto en una red social (AJAX)
     *
     * @param proyectoId ID del proyecto a publicar
     * @param redSocial nombre de la red social (LinkedIn, Twitter, etc.)
     * @param authentication datos del usuario autenticado
     * @return ResponseEntity con el resultado de la publicación
     */
    @PostMapping("/{proyectoId}/publicar")
    public ResponseEntity<?> publicarProyecto(@PathVariable Long proyectoId,
                                              @RequestParam String redSocial,
                                              Authentication authentication) {
        try {
            // Obtener proyecto
            Proyecto proyecto = proyectoService.buscarPorId(proyectoId);

            // Verificar que el usuario sea el propietario del proyecto
            String emailUsuario = authentication.getName();
            if (!proyecto.getUsuario().getEmail().equals(emailUsuario)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(crearErrorResponse("No tienes permisos para publicar este proyecto"));
            }

            // Publicar en la red social
            // LinkedIn → Intenta automático, si falla usa compartir manual
            // Otras redes → Abre ventana para compartir manualmente
            boolean esAdmin = false;
            PublicacionRRSS publicacion = publicacionRRSSService.publicarEnRedSocial(proyecto, redSocial, esAdmin);

            // Construir respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("publicacion", crearPublicacionDTO(publicacion));

            // Mensaje diferente según la red social y estado
            if ("LinkedIn".equals(redSocial)) {
                if (publicacion.getEstado().name().equals("PUBLICADO")) {
                    // LinkedIn → Publicación automática exitosa
                    response.put("mensaje", "¡Proyecto publicado automáticamente en LinkedIn!");
                    response.put("url", publicacion.getUrlPublicacion());
                    response.put("verEnLinkedIn", true);
                } else if (publicacion.getEstado().name().equals("PENDIENTE") && publicacion.getUrlPublicacion() != null) {
                    // LinkedIn → Compartir manual (sin permisos de organización)
                    response.put("mensaje", "Abriendo LinkedIn para compartir. Puedes seleccionar tu página de empresa en el diálogo de compartir.");
                    response.put("url", publicacion.getUrlPublicacion());
                    response.put("abrirEnNuevaVentana", true);
                    response.put("compartirEnEmpresa", true);
                } else if (publicacion.getEstado().name().equals("ERROR")) {
                    response.put("mensaje", "Error al publicar en LinkedIn: " + publicacion.getMensajeError());
                    response.put("error", true);
                } else {
                    response.put("mensaje", "Publicando en LinkedIn...");
                }
            } else if ("Instagram".equals(redSocial) || "GitHub".equals(redSocial)) {
                // Instagram/GitHub → No tienen compartir directo, copiar link
                response.put("mensaje", redSocial + " no permite compartir automáticamente. Se abrirá el proyecto para copiar el enlace.");
                response.put("url", publicacion.getUrlPublicacion());
                response.put("abrirEnNuevaVentana", true);
                response.put("copiarEnlace", true);
            } else {
                // Otras redes → Abrir ventana para compartir manualmente
                response.put("mensaje", "Abriendo " + redSocial + " para compartir...");
                response.put("url", publicacion.getUrlPublicacion());
                response.put("abrirEnNuevaVentana", true);
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al publicar el proyecto"));
        }
    }

    /**
     * Obtiene el historial de publicaciones de un proyecto (AJAX)
     *
     * @param proyectoId ID del proyecto
     * @return ResponseEntity con la lista de publicaciones
     */
    @GetMapping("/{proyectoId}/historial")
    public ResponseEntity<?> obtenerHistorial(@PathVariable Long proyectoId) {
        try {
            Proyecto proyecto = proyectoService.buscarPorId(proyectoId);
            List<PublicacionRRSS> publicaciones = publicacionRRSSService.obtenerPublicacionesPorProyecto(proyecto);

            // Convertir a DTOs
            List<Map<String, Object>> publicacionesDTO = publicaciones.stream()
                    .map(this::crearPublicacionDTO)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("publicaciones", publicacionesDTO);
            response.put("total", publicaciones.size());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al obtener el historial"));
        }
    }

    /**
     * Reintenta una publicación fallida (AJAX)
     *
     * @param publicacionId ID de la publicación
     * @return ResponseEntity con el resultado
     */
    @PostMapping("/{publicacionId}/reintentar")
    public ResponseEntity<?> reintentarPublicacion(@PathVariable Long publicacionId) {
        try {
            // Reintentar publicación
            PublicacionRRSS publicacion = publicacionRRSSService.reintentarPublicacion(publicacionId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("publicacion", crearPublicacionDTO(publicacion));
            response.put("mensaje", "Reintentando publicación...");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al reintentar la publicación"));
        }
    }

    /**
     * Verifica si un proyecto ya fue publicado en una red social (AJAX)
     *
     * @param proyectoId ID del proyecto
     * @param redSocial nombre de la red social
     * @return ResponseEntity con el estado
     */
    @GetMapping("/{proyectoId}/verificar")
    public ResponseEntity<?> verificarPublicacion(@PathVariable Long proyectoId,
                                                  @RequestParam String redSocial) {
        try {
            Proyecto proyecto = proyectoService.buscarPorId(proyectoId);
            List<PublicacionRRSS> publicaciones =
                publicacionRRSSService.obtenerPublicacionesPorProyectoYRedSocial(proyecto, redSocial);

            boolean yaPublicado = publicaciones.stream()
                    .anyMatch(p -> p.getEstado().name().equals("PUBLICADO"));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("yaPublicado", yaPublicado);
            response.put("totalPublicaciones", publicaciones.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al verificar la publicación"));
        }
    }

    /**
     * Crea un DTO de publicación para respuestas JSON
     *
     * @param publicacion la publicación
     * @return Map con los datos de la publicación
     */
    private Map<String, Object> crearPublicacionDTO(PublicacionRRSS publicacion) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", publicacion.getId());
        dto.put("redSocial", publicacion.getRedSocial());
        dto.put("estado", publicacion.getEstado().name());
        dto.put("fechaPublicacion", publicacion.getFechaPublicacion().toString());
        return dto;
    }

    /**
     * Crea una respuesta de error consistente
     *
     * @param mensaje mensaje de error
     * @return Map con estructura de error
     */
    private Map<String, Object> crearErrorResponse(String mensaje) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", mensaje);
        return error;
    }
}

