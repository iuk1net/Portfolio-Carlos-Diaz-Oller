package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.CV;
import es.fempa.acd.demosecurityproductos.model.enums.Rol;
import es.fempa.acd.demosecurityproductos.service.ProyectoService;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import es.fempa.acd.demosecurityproductos.service.CVService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ProyectoService proyectoService;
    private final CVService cvService;

    public UsuarioController(UsuarioService usuarioService, ProyectoService proyectoService, CVService cvService) {
        this.usuarioService = usuarioService;
        this.proyectoService = proyectoService;
        this.cvService = cvService;
    }

    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden listar usuarios
    @GetMapping("/lista")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/lista"; // Apunta a una plantilla Thymeleaf
    }

    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden crear nuevos usuarios
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", Rol.values());
        return "usuarios/nuevo";
    }

    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden guardar nuevos usuarios
    @PostMapping
    public String crearUsuario(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam Rol rol) {
        usuarioService.crearUsuario(username, password, email, rol);
        return "redirect:/usuarios/lista";
    }

    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden eliminar usuarios
    @PostMapping("/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios/lista";
    }
    
    @GetMapping("/{id}/editar")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Rol.values()); // Cargar roles disponibles
        return "usuarios/editar_usuario";
    }

    @PostMapping("/{id}/editar")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario) {
        usuarioService.actualizarUsuario(id, usuario);
        return "redirect:/usuarios/lista";
    }
    
    // ========== PERFIL PÚBLICO DE USUARIO ==========

    /**
     * Ver perfil público de un usuario por su email (usado como username)
     * Accesible sin autenticación
     *
     * @param username email del usuario
     * @param model modelo de Spring MVC
     * @return vista perfil-publico.html
     */
    @GetMapping("/perfil/{username}")
    public String verPerfilPublico(@PathVariable String username, Model model) {
        try {
            // Buscar usuario por email (username)
            Usuario usuario = usuarioService.buscarPorEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            // Obtener proyectos del usuario
            List<Proyecto> proyectosUsuario = proyectoService.listarProyectosPorUsuario(usuario);

            // Calcular estadísticas
            int totalProyectos = proyectosUsuario.size();
            int totalVotosRecibidos = proyectosUsuario.stream()
                    .mapToInt(Proyecto::getTotalLikes)
                    .sum();

            // Obtener CVs del usuario (públicos para descarga)
            List<CV> cvsUsuario = cvService.obtenerCVsPorUsuario(usuario);

            // Agregar datos al modelo
            model.addAttribute("usuario", usuario);
            model.addAttribute("proyectos", proyectosUsuario);
            model.addAttribute("cvs", cvsUsuario);
            model.addAttribute("totalProyectos", totalProyectos);
            model.addAttribute("totalVotosRecibidos", totalVotosRecibidos);

            return "usuario/perfil-publico";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Usuario no encontrado");
            return "error/404";
        }
    }

    /**
     * Ver solo los proyectos de un usuario
     * Endpoint complementario al perfil público
     *
     * @param username email del usuario
     * @param model modelo de Spring MVC
     * @return vista de lista de proyectos filtrada
     */
    @GetMapping("/perfil/{username}/proyectos")
    public String proyectosUsuario(@PathVariable String username, Model model) {
        try {
            Usuario usuario = usuarioService.buscarPorEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            List<Proyecto> proyectosUsuario = proyectoService.listarProyectosPorUsuario(usuario);

            model.addAttribute("proyectos", proyectosUsuario);
            model.addAttribute("usuario", usuario);
            model.addAttribute("tituloSeccion", "Proyectos de " + usuario.getNombre());

            return "usuario/perfil-proyectos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Usuario no encontrado");
            return "error/404";
        }
    }

    /**
     * Descarga pública de CV desde perfil de usuario
     * Accesible sin autenticación para que reclutadores puedan descargar
     *
     * @param cvId ID del CV a descargar
     * @return ResponseEntity con el archivo para descarga
     */
    @GetMapping("/perfil/cv/descargar/{cvId}")
    public org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> descargarCVPublico(@PathVariable Long cvId) {
        try {
            CV cv = cvService.obtenerCVPorId(cvId)
                    .orElseThrow(() -> new IllegalArgumentException("CV no encontrado"));

            org.springframework.core.io.Resource resource = cvService.descargarCVPublico(cvId);

            // Generar nombre descriptivo para la descarga
            String filename = "CV_" + cv.getUsuario().getNombre().replace(" ", "_") +
                             "_" + cvId + "." + cv.getTipoArchivo().toLowerCase();

            // Determinar tipo de contenido
            org.springframework.http.MediaType mediaType = org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
            if (cv.getTipoArchivo().equalsIgnoreCase("PDF")) {
                mediaType = org.springframework.http.MediaType.APPLICATION_PDF;
            }

            return org.springframework.http.ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                           "attachment; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.notFound().build();
        }
    }

    // ========== EDITAR PERFIL ==========

    /**
     * Muestra el formulario de edición del perfil del usuario autenticado
     * Solo el usuario puede editar su propio perfil
     *
     * @param authentication datos del usuario autenticado
     * @param model modelo de Spring MVC
     * @return vista editar-perfil.html
     */
    @GetMapping("/perfil/editar")
    public String mostrarFormularioEdicionPerfil(Authentication authentication, Model model) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            model.addAttribute("usuario", usuario);

            return "usuario/editar-perfil";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Error al cargar el perfil");
            return "redirect:/usuario/dashboard";
        }
    }

    /**
     * Procesa la actualización del perfil del usuario
     * Actualiza: nombre, emailProfesional, whatsapp, telefono, enlacesRRSS
     *
     * @param nombre nombre completo del usuario
     * @param emailProfesional email profesional público
     * @param whatsapp número de WhatsApp
     * @param telefono teléfono de contacto
     * @param enlacesRRSS lista de enlaces a redes sociales
     * @param authentication datos del usuario autenticado
     * @param redirectAttributes atributos para redirección
     * @return redirección al dashboard o formulario con errores
     */
    @PostMapping("/perfil/editar")
    public String actualizarPerfil(
            @RequestParam String nombre,
            @RequestParam(required = false) String emailProfesional,
            @RequestParam(required = false) String whatsapp,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) List<String> enlacesRRSS,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            // Validar nombre (obligatorio)
            if (nombre == null || nombre.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error",
                    "❌ El nombre es obligatorio");
                return "redirect:/usuarios/perfil/editar";
            }

            // Actualizar campos
            usuario.setNombre(nombre.trim());
            usuario.setEmailProfesional(emailProfesional != null && !emailProfesional.trim().isEmpty()
                ? emailProfesional.trim() : null);
            usuario.setWhatsapp(whatsapp != null && !whatsapp.trim().isEmpty()
                ? whatsapp.trim() : null);
            usuario.setTelefono(telefono != null && !telefono.trim().isEmpty()
                ? telefono.trim() : null);

            // Limpiar y actualizar enlaces RRSS (eliminar vacíos)
            // IMPORTANTE: Para @ElementCollection debemos limpiar primero y luego agregar
            usuario.getEnlacesRRSS().clear(); // Limpiar la lista existente

            if (enlacesRRSS != null && !enlacesRRSS.isEmpty()) {
                List<String> enlacesLimpios = enlacesRRSS.stream()
                    .filter(enlace -> enlace != null && !enlace.trim().isEmpty())
                    .map(String::trim)
                    .distinct() // Evitar duplicados
                    .toList();
                usuario.getEnlacesRRSS().addAll(enlacesLimpios); // Agregar los nuevos
            }

            // Guardar cambios
            usuarioService.actualizarUsuario(usuario.getId(), usuario);

            redirectAttributes.addFlashAttribute("success",
                "✅ Perfil actualizado correctamente");

            return "redirect:/usuario/dashboard";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error",
                "❌ " + e.getMessage());
            return "redirect:/usuarios/perfil/editar";
        } catch (Exception e) {
            // Log del error para debugging
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error",
                "❌ Error al actualizar el perfil: " + e.getMessage());
            return "redirect:/usuarios/perfil/editar";
        }
    }


}
