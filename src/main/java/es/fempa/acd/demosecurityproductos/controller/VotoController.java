package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.ProyectoService;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import es.fempa.acd.demosecurityproductos.service.VotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * API REST Controller para el sistema de votación
 * Proporciona endpoints AJAX para votar proyectos sin recargar la página
 *
 * @author Portfolio Social v2.0
 * @version 1.0
 */
@RestController
@RequestMapping("/api/votos")
@PreAuthorize("isAuthenticated()")
public class VotoController {

    private final VotoService votoService;
    private final UsuarioService usuarioService;
    private final ProyectoService proyectoService;

    public VotoController(VotoService votoService,
                         UsuarioService usuarioService,
                         ProyectoService proyectoService) {
        this.votoService = votoService;
        this.usuarioService = usuarioService;
        this.proyectoService = proyectoService;
    }

    /**
     * Toggle voto: si existe lo quita, si no existe lo crea (AJAX)
     * Endpoint principal para votación asíncrona
     *
     * @param proyectoId ID del proyecto a votar
     * @param authentication datos del usuario autenticado
     * @return ResponseEntity con el estado del voto y contador actualizado
     */
    @PostMapping("/{proyectoId}/toggle")
    public ResponseEntity<?> toggleVoto(@PathVariable Long proyectoId,
                                        Authentication authentication) {
        try {
            // Obtener usuario autenticado
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            // Obtener proyecto
            Proyecto proyecto = proyectoService.buscarPorId(proyectoId);

            // Toggle del voto
            boolean votado = votoService.toggleVoto(usuario, proyecto);

            // Recargar el proyecto para obtener el contador actualizado
            proyecto = proyectoService.buscarPorId(proyectoId);

            // Construir respuesta JSON
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("votado", votado);
            response.put("totalLikes", proyecto.getTotalLikes());
            response.put("mensaje", votado ? "¡Voto registrado!" : "Voto eliminado");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al procesar el voto"));
        }
    }

    /**
     * Verifica si el usuario actual ya votó por un proyecto (AJAX)
     * Útil para sincronizar el estado del botón
     *
     * @param proyectoId ID del proyecto
     * @param authentication datos del usuario autenticado
     * @return ResponseEntity con el estado del voto
     */
    @GetMapping("/{proyectoId}/estado")
    public ResponseEntity<?> verificarEstadoVoto(@PathVariable Long proyectoId,
                                                  Authentication authentication) {
        try {
            // Obtener usuario autenticado
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            // Obtener proyecto
            Proyecto proyecto = proyectoService.buscarPorId(proyectoId);

            // Verificar si ya votó
            boolean yaVoto = votoService.verificarVoto(usuario, proyecto);

            // Construir respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("votado", yaVoto);
            response.put("totalLikes", proyecto.getTotalLikes());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al verificar el estado del voto"));
        }
    }

    /**
     * Votar un proyecto (AJAX) - Método explícito
     *
     * @param proyectoId ID del proyecto a votar
     * @param authentication datos del usuario autenticado
     * @return ResponseEntity con el resultado
     */
    @PostMapping("/{proyectoId}/votar")
    public ResponseEntity<?> votar(@PathVariable Long proyectoId,
                                   Authentication authentication) {
        try {
            // Obtener usuario autenticado
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            // Obtener proyecto
            Proyecto proyecto = proyectoService.buscarPorId(proyectoId);

            // Votar
            votoService.votar(usuario, proyecto);

            // Recargar proyecto
            proyecto = proyectoService.buscarPorId(proyectoId);

            // Construir respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("votado", true);
            response.put("totalLikes", proyecto.getTotalLikes());
            response.put("mensaje", "¡Voto registrado exitosamente!");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al votar"));
        }
    }

    /**
     * Quitar voto de un proyecto (AJAX) - Método explícito
     *
     * @param proyectoId ID del proyecto
     * @param authentication datos del usuario autenticado
     * @return ResponseEntity con el resultado
     */
    @DeleteMapping("/{proyectoId}/quitar")
    public ResponseEntity<?> quitarVoto(@PathVariable Long proyectoId,
                                        Authentication authentication) {
        try {
            // Obtener usuario autenticado
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            // Obtener proyecto
            Proyecto proyecto = proyectoService.buscarPorId(proyectoId);

            // Quitar voto
            votoService.quitarVoto(usuario, proyecto);

            // Recargar proyecto
            proyecto = proyectoService.buscarPorId(proyectoId);

            // Construir respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("votado", false);
            response.put("totalLikes", proyecto.getTotalLikes());
            response.put("mensaje", "Voto eliminado");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al quitar el voto"));
        }
    }

    /**
     * Obtiene el total de votos de un proyecto (AJAX)
     *
     * @param proyectoId ID del proyecto
     * @return ResponseEntity con el contador de votos
     */
    @GetMapping("/{proyectoId}/total")
    public ResponseEntity<?> obtenerTotalVotos(@PathVariable Long proyectoId) {
        try {
            Proyecto proyecto = proyectoService.buscarPorId(proyectoId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("totalLikes", proyecto.getTotalLikes());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al obtener votos"));
        }
    }

    /**
     * Helper para crear respuestas de error consistentes
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

