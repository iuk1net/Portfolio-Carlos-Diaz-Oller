package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.service.ProyectoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * API REST Controller para gestión de galería de imágenes de proyectos
 * Maneja subida, eliminación y establecimiento de imagen principal
 *
 * @author Portfolio Social v2.0
 * @version 1.0
 */
@RestController
@RequestMapping("/api/proyectos")
@PreAuthorize("isAuthenticated()")
public class GaleriaImagenesController {

    private final ProyectoService proyectoService;
    private final Path uploadPath;

    // Tamaño máximo: 5 MB por imagen
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    // Formatos permitidos
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif", "webp");

    public GaleriaImagenesController(ProyectoService proyectoService,
                                    @Value("${app.upload.images.dir:uploads/images}") String uploadDirectory) {
        this.proyectoService = proyectoService;
        this.uploadPath = Paths.get(uploadDirectory);
        try {
            Files.createDirectories(this.uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de imágenes", e);
        }
    }

    /**
     * Sube una nueva imagen a la galería del proyecto
     *
     * @param proyectoId ID del proyecto
     * @param file archivo de imagen
     * @param esPrincipal si es la imagen principal
     * @param authentication usuario autenticado
     * @return ResponseEntity con el resultado
     */
    @PostMapping("/{proyectoId}/imagenes")
    public ResponseEntity<?> subirImagen(@PathVariable Long proyectoId,
                                        @RequestParam("file") MultipartFile file,
                                        @RequestParam(defaultValue = "false") boolean esPrincipal,
                                        Authentication authentication) {
        try {
            // Obtener proyecto
            Proyecto proyecto = proyectoService.buscarPorId(proyectoId);

            // Verificar permisos
            String emailUsuario = authentication.getName();
            if (!proyecto.getUsuario().getEmail().equals(emailUsuario)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(crearErrorResponse("No tienes permisos para modificar este proyecto"));
            }

            // Validar archivo
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(crearErrorResponse("El archivo está vacío"));
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest()
                        .body(crearErrorResponse("La imagen excede el tamaño máximo de 5 MB"));
            }

            String extension = obtenerExtension(file.getOriginalFilename());
            if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                return ResponseEntity.badRequest()
                        .body(crearErrorResponse("Formato no permitido. Usa: JPG, PNG, GIF, WEBP"));
            }

            // Crear directorio del proyecto
            Path proyectoDir = uploadPath.resolve(String.valueOf(proyectoId));
            Files.createDirectories(proyectoDir);

            // Generar nombre único
            String timestamp = String.valueOf(System.currentTimeMillis());
            String filename = timestamp + "." + extension;
            Path filePath = proyectoDir.resolve(filename);

            // Guardar archivo
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Actualizar proyecto
            List<String> galeria = proyecto.getGaleriaImagenes();
            if (galeria == null) {
                galeria = new ArrayList<>();
            }

            String urlImagen = "/uploads/images/" + proyectoId + "/" + filename;

            // Si es principal, ponerla al inicio
            if (esPrincipal) {
                galeria.add(0, urlImagen);
            } else {
                galeria.add(urlImagen);
            }

            proyecto.setGaleriaImagenes(galeria);
            proyectoService.actualizarProyecto(proyectoId, proyecto, emailUsuario);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("url", urlImagen);
            response.put("esPrincipal", esPrincipal);
            response.put("mensaje", "Imagen subida correctamente");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(crearErrorResponse(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al guardar la imagen"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error inesperado"));
        }
    }

    /**
     * Elimina una imagen de la galería
     *
     * @param proyectoId ID del proyecto
     * @param index índice de la imagen en la galería
     * @param authentication usuario autenticado
     * @return ResponseEntity con el resultado
     */
    @DeleteMapping("/{proyectoId}/imagenes/{index}")
    public ResponseEntity<?> eliminarImagen(@PathVariable Long proyectoId,
                                           @PathVariable int index,
                                           Authentication authentication) {
        try {
            Proyecto proyecto = proyectoService.buscarPorId(proyectoId);

            // Verificar permisos
            String emailUsuario = authentication.getName();
            if (!proyecto.getUsuario().getEmail().equals(emailUsuario)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(crearErrorResponse("No tienes permisos"));
            }

            List<String> galeria = proyecto.getGaleriaImagenes();
            if (galeria == null || index < 0 || index >= galeria.size()) {
                return ResponseEntity.badRequest()
                        .body(crearErrorResponse("Índice inválido"));
            }

            // Obtener URL de la imagen
            String urlImagen = galeria.get(index);

            // Eliminar archivo físico
            try {
                String filename = urlImagen.substring(urlImagen.lastIndexOf("/") + 1);
                Path filePath = uploadPath.resolve(proyectoId + "/" + filename);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // Continuar aunque falle la eliminación física
            }

            // Eliminar de la galería
            galeria.remove(index);
            proyecto.setGaleriaImagenes(galeria);
            proyectoService.actualizarProyecto(proyectoId, proyecto, emailUsuario);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("mensaje", "Imagen eliminada correctamente");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al eliminar la imagen"));
        }
    }

    /**
     * Establece una imagen como principal (mueve al inicio del array)
     *
     * @param proyectoId ID del proyecto
     * @param index índice de la imagen
     * @param authentication usuario autenticado
     * @return ResponseEntity con el resultado
     */
    @PutMapping("/{proyectoId}/imagenes/{index}/principal")
    public ResponseEntity<?> establecerImagenPrincipal(@PathVariable Long proyectoId,
                                                       @PathVariable int index,
                                                       Authentication authentication) {
        try {
            Proyecto proyecto = proyectoService.buscarPorId(proyectoId);

            // Verificar permisos
            String emailUsuario = authentication.getName();
            if (!proyecto.getUsuario().getEmail().equals(emailUsuario)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(crearErrorResponse("No tienes permisos"));
            }

            List<String> galeria = proyecto.getGaleriaImagenes();
            if (galeria == null || index < 0 || index >= galeria.size()) {
                return ResponseEntity.badRequest()
                        .body(crearErrorResponse("Índice inválido"));
            }

            // Mover imagen al inicio
            String imagenPrincipal = galeria.remove(index);
            galeria.add(0, imagenPrincipal);

            proyecto.setGaleriaImagenes(galeria);
            proyectoService.actualizarProyecto(proyectoId, proyecto, emailUsuario);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("mensaje", "Imagen principal establecida");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al establecer imagen principal"));
        }
    }

    /**
     * Obtiene la extensión de un archivo
     */
    private String obtenerExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            return "";
        }
        return filename.substring(lastDot + 1);
    }

    /**
     * Crea una respuesta de error
     */
    private Map<String, Object> crearErrorResponse(String mensaje) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", mensaje);
        return error;
    }
}

