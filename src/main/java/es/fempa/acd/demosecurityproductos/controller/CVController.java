package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.CV;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.CVService;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

/**
 * Controlador para la gestión de CVs (Curriculum Vitae)
 * Implementa endpoints para subir, descargar y eliminar CVs según requisitos del proyecto
 *
 * @author Portfolio Social v2.0
 * @version 1.0
 */
@Controller
@RequestMapping("/usuario/cv")
@PreAuthorize("isAuthenticated()") // Solo usuarios autenticados
public class CVController {

    private final CVService cvService;
    private final UsuarioService usuarioService;

    public CVController(CVService cvService, UsuarioService usuarioService) {
        this.cvService = cvService;
        this.usuarioService = usuarioService;
    }

    /**
     * Muestra la página de gestión de CVs del usuario autenticado
     * Lista todos los CVs subidos por el usuario ordenados por fecha
     *
     * @param authentication datos del usuario autenticado
     * @param model modelo de Spring MVC
     * @return vista cvs.html
     */
    @GetMapping("/lista")
    public String mostrarGestionCVs(Authentication authentication, Model model) {
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<CV> cvs = cvService.listarCVsPorUsuario(usuario);

        model.addAttribute("cvs", cvs);
        model.addAttribute("usuario", usuario);
        model.addAttribute("totalCVs", cvs.size());

        return "usuario/cvs";
    }

    /**
     * Procesa la subida de un nuevo CV
     * Valida formato (PDF, DOCX, TXT) y tamaño (max 10MB)
     *
     * @param file archivo multipart del CV
     * @param authentication datos del usuario autenticado
     * @param redirectAttributes atributos para redirección
     * @return redirección a la lista de CVs
     */
    @PostMapping("/subir")
    public String subirCV(@RequestParam("file") MultipartFile file,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            // Validación adicional del nombre del archivo
            if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
                redirectAttributes.addFlashAttribute("error",
                    "❌ El archivo no tiene un nombre válido");
                return "redirect:/usuario/cv/lista";
            }

            CV cv = cvService.subirCV(file, usuario);

            redirectAttributes.addFlashAttribute("success",
                "✅ CV subido correctamente: " + file.getOriginalFilename());

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "❌ " + e.getMessage());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error",
                "❌ Error al guardar el archivo. Por favor, inténtalo de nuevo.");
        }

        return "redirect:/usuario/cv/lista";
    }

    /**
     * Descarga un CV específico
     * Solo el propietario del CV puede descargarlo (seguridad)
     *
     * @param id ID del CV a descargar
     * @param authentication datos del usuario autenticado
     * @return ResponseEntity con el archivo para descarga
     */
    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> descargarCV(@PathVariable Long id,
                                                Authentication authentication) {
        try {
            String username = authentication.getName();
            Resource resource = cvService.descargarCV(id, username);

            // Obtener información del CV para el nombre del archivo
            CV cv = cvService.obtenerCVPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("CV no encontrado"));

            // Generar nombre descriptivo para la descarga
            String filename = "CV_" + cv.getUsuario().getNombre().replace(" ", "_") +
                             "_" + id + "." + cv.getTipoArchivo().toLowerCase();

            // Determinar tipo de contenido según la extensión
            MediaType mediaType = getMediaTypeForExtension(cv.getTipoArchivo());

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                           "attachment; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (org.springframework.security.access.AccessDeniedException e) {
            return ResponseEntity.status(403).build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Elimina un CV del sistema (archivo físico y registro de BD)
     * Solo el propietario del CV puede eliminarlo
     *
     * @param id ID del CV a eliminar
     * @param authentication datos del usuario autenticado
     * @param redirectAttributes atributos para redirección
     * @return redirección a la lista de CVs
     */
    @PostMapping("/{id}/eliminar")
    public String eliminarCV(@PathVariable Long id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            cvService.eliminarCV(id, username);

            redirectAttributes.addFlashAttribute("success",
                "✅ CV eliminado correctamente");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error",
                "❌ " + e.getMessage());
        } catch (org.springframework.security.access.AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("error",
                "❌ No tienes permisos para eliminar este CV");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error",
                "❌ Error al eliminar el archivo. Por favor, inténtalo de nuevo.");
        }

        return "redirect:/usuario/cv/lista";
    }

    /**
     * Endpoint AJAX para obtener el CV más reciente (opcional)
     * Útil para mostrar en el dashboard si hay CV disponible
     *
     * @param authentication datos del usuario autenticado
     * @return ResponseEntity con información del CV o 404
     */
    @GetMapping("/activo")
    @ResponseBody
    public ResponseEntity<?> obtenerCVActivo(Authentication authentication) {
        try {
            String username = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            return cvService.obtenerCVMasReciente(usuario)
                    .map(cv -> ResponseEntity.ok(new CVResponse(cv)))
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Determina el MediaType apropiado según la extensión del archivo
     *
     * @param extension extensión del archivo (PDF, DOCX, TXT)
     * @return MediaType correspondiente
     */
    private MediaType getMediaTypeForExtension(String extension) {
        return switch (extension.toUpperCase()) {
            case "PDF" -> MediaType.APPLICATION_PDF;
            case "DOCX" -> MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            case "TXT" -> MediaType.TEXT_PLAIN;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

    /**
     * DTO para respuesta JSON del CV (usado en endpoint AJAX)
     */
    private static class CVResponse {
        private final Long id;
        private final String tipoArchivo;
        private final String fechaSubida;

        public CVResponse(CV cv) {
            this.id = cv.getId();
            this.tipoArchivo = cv.getTipoArchivo();
            this.fechaSubida = cv.getFechaSubida().toString();
        }

        public Long getId() { return id; }
        public String getTipoArchivo() { return tipoArchivo; }
        public String getFechaSubida() { return fechaSubida; }
    }
}

