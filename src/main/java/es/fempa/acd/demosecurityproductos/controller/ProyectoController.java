package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Favorito;
import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import es.fempa.acd.demosecurityproductos.service.FavoritoService;
import es.fempa.acd.demosecurityproductos.service.ProyectoService;
import es.fempa.acd.demosecurityproductos.service.VotoService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;
    private final FavoritoService favoritoService;
    private final UsuarioService usuarioService;
    private final VotoService votoService;

    public ProyectoController(ProyectoService proyectoService,
                             FavoritoService favoritoService,
                             UsuarioService usuarioService,
                             VotoService votoService) {
        this.proyectoService = proyectoService;
        this.favoritoService = favoritoService;
        this.usuarioService = usuarioService;
        this.votoService = votoService;
    }

    // ========== LISTADO PÚBLICO DE PROYECTOS ==========

    /**
     * Lista todos los proyectos (público)
     * Muestra información de votos y favoritos del usuario actual si está autenticado
     */
    @GetMapping("/lista")
    public String listarProyectos(Model model, Authentication authentication) {
        List<Proyecto> proyectos = proyectoService.listarProyectos();
        model.addAttribute("proyectos", proyectos);

        // Si hay usuario autenticado, cargar sus votos y favoritos
        if (authentication != null) {
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElse(null);

            if (usuario != null) {
                model.addAttribute("usuarioActual", usuario);
                model.addAttribute("isAdmin", authentication.getAuthorities()
                        .contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
            }
        }

        return "proyectos/lista";
    }

    /**
     * Ranking de proyectos ordenados por votos
     */
    @GetMapping("/ranking")
    public String rankingProyectos(Model model) {
        List<Proyecto> proyectos = proyectoService.listarProyectosOrdenadosPorVotos();
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("isRanking", true);
        return "proyectos/lista";
    }

    /**
     * Ver detalle de un proyecto
     * PÚBLICO - Accesible para usuarios autenticados y visitantes (para compartir en LinkedIn)
     */
    @GetMapping("/{id}")
    public String verDetalleProyecto(@PathVariable Long id, Model model, Authentication authentication) {
        Proyecto proyecto = proyectoService.buscarPorId(id);
        model.addAttribute("proyecto", proyecto);

        // Por defecto, es visitante (no puede editar, votar, etc.)
        model.addAttribute("puedeEditar", false);
        model.addAttribute("yaVoto", false);
        model.addAttribute("esFavorito", false);
        model.addAttribute("esVisitante", authentication == null);

        // Verificar si el usuario actual es el propietario o admin
        if (authentication != null) {
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email).orElse(null);

            if (usuario != null) {
                model.addAttribute("esVisitante", false);

                boolean isOwner = proyecto.getUsuario().getId().equals(usuario.getId());
                boolean isAdmin = authentication.getAuthorities()
                        .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

                model.addAttribute("puedeEditar", isOwner || isAdmin);
                model.addAttribute("usuarioActual", usuario);

                // Verificar si el usuario ya votó este proyecto
                boolean yaVoto = votoService.usuarioYaVoto(usuario, proyecto);
                model.addAttribute("yaVoto", yaVoto);

                // Verificar si está en favoritos
                boolean esFavorito = favoritoService.esFavorito(usuario, proyecto);
                model.addAttribute("esFavorito", esFavorito);
            }
        }

        return "proyectos/detalle";
    }
    // ========== CREAR PROYECTO ==========

    /**
     * Mostrar formulario de nuevo proyecto
     * Accesible para usuarios autenticados (USER y ADMIN)
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoProyecto(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        return "proyectos/nuevo";
    }

    /**
     * Crear nuevo proyecto
     * Cualquier usuario autenticado puede crear proyectos
     */
    @PostMapping("/nuevo")
    public String crearProyecto(@RequestParam String titulo,
                                @RequestParam String descripcion,
                                @RequestParam(required = false) String tecnologias,
                                @RequestParam(required = false) String enlaceWeb,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            Proyecto proyecto = new Proyecto();
            proyecto.setTitulo(titulo);
            proyecto.setDescripcion(descripcion);
            proyecto.setTecnologias(tecnologias);
            proyecto.setEnlaceWeb(enlaceWeb);

            Proyecto proyectoCreado = proyectoService.crearProyecto(proyecto, usuario);

            redirectAttributes.addFlashAttribute("success",
                "✅ Proyecto creado exitosamente. Ahora puedes agregar imágenes a la galería.");
            return "redirect:/proyectos/" + proyectoCreado.getId() + "/editar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el proyecto: " + e.getMessage());
            return "redirect:/proyectos/nuevo";
        }
    }

    // ========== EDITAR PROYECTO ==========

    /**
     * Mostrar formulario de edición
     * Solo el propietario o admin pueden acceder
     */
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditarProyecto(@PathVariable Long id,
                                                   Model model,
                                                   Authentication authentication,
                                                   RedirectAttributes redirectAttributes) {
        try {
            Proyecto proyecto = proyectoService.buscarPorId(id);
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email).orElseThrow();

            boolean isAdmin = authentication.getAuthorities()
                    .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
            boolean isOwner = proyecto.getUsuario().getId().equals(usuario.getId());

            if (!isAdmin && !isOwner) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para editar este proyecto");
                return "redirect:/proyectos/" + id;
            }

            model.addAttribute("proyecto", proyecto);
            return "proyectos/editar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Proyecto no encontrado");
            return "redirect:/proyectos/lista";
        }
    }

    /**
     * Actualizar proyecto
     * Solo el propietario o admin pueden actualizar
     */
    @PostMapping("/{id}/editar")
    public String editarProyecto(@PathVariable Long id,
                                  @RequestParam String titulo,
                                  @RequestParam String descripcion,
                                  @RequestParam(required = false) String tecnologias,
                                  @RequestParam(required = false) String enlaceWeb,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            Proyecto proyectoActualizado = new Proyecto();
            proyectoActualizado.setTitulo(titulo);
            proyectoActualizado.setDescripcion(descripcion);
            proyectoActualizado.setTecnologias(tecnologias);
            proyectoActualizado.setEnlaceWeb(enlaceWeb);

            proyectoService.actualizarProyecto(id, proyectoActualizado, authentication.getName());

            redirectAttributes.addFlashAttribute("success", "Proyecto actualizado exitosamente");
            return "redirect:/proyectos/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/proyectos/" + id + "/editar";
        }
    }    // ========== ELIMINAR PROYECTO ==========

    /**
     * Eliminar proyecto
     * Solo el propietario o admin pueden eliminar
     */
    @PostMapping("/{id}/eliminar")
    public String eliminarProyecto(@PathVariable Long id,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        try {
            boolean isAdmin = authentication.getAuthorities()
                    .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

            proyectoService.eliminarProyecto(id, authentication.getName(), isAdmin);

            redirectAttributes.addFlashAttribute("success", "Proyecto eliminado exitosamente");
            return "redirect:/proyectos/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            return "redirect:/proyectos/" + id;
        }
    }

    // ========== SISTEMA DE VOTOS ==========

    /**
     * Votar un proyecto
     */
    @PostMapping("/{id}/votar")
    public String votarProyecto(@PathVariable Long id,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            Proyecto proyecto = proyectoService.buscarPorId(id);

            votoService.votar(usuario, proyecto);
            redirectAttributes.addFlashAttribute("success", "Voto registrado");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al votar");
        }

        return "redirect:/proyectos/" + id;
    }

    /**
     * Quitar voto de un proyecto
     */
    @PostMapping("/{id}/quitar-voto")
    public String quitarVoto(@PathVariable Long id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            Proyecto proyecto = proyectoService.buscarPorId(id);

            votoService.quitarVoto(usuario, proyecto);
            redirectAttributes.addFlashAttribute("success", "Voto eliminado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al quitar voto");
        }

        return "redirect:/proyectos/" + id;
    }

    // ========== SISTEMA DE FAVORITOS ==========

    /**
     * Agregar proyecto a favoritos
     */
    @PostMapping("/{id}/favorito")
    public String agregarFavorito(@PathVariable Long id,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            Proyecto proyecto = proyectoService.buscarPorId(id);

            favoritoService.agregarFavorito(usuario, proyecto);
            redirectAttributes.addFlashAttribute("success", "Proyecto agregado a favoritos");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar a favoritos");
        }

        return "redirect:/proyectos/" + id;
    }

    /**
     * Quitar proyecto de favoritos
     */
    @PostMapping("/{id}/quitar-favorito")
    public String quitarFavorito(@PathVariable Long id,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            Proyecto proyecto = proyectoService.buscarPorId(id);

            favoritoService.eliminarFavorito(usuario, proyecto);
            redirectAttributes.addFlashAttribute("success", "Proyecto eliminado de favoritos");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al quitar de favoritos");
        }

        return "redirect:/proyectos/" + id;
    }

    /**
     * Listar proyectos favoritos del usuario actual
     */
    @GetMapping("/favoritos")
    public String listarFavoritos(Model model, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<Favorito> favoritos = favoritoService.listarFavoritosPorUsuario(usuario);
        model.addAttribute("favoritos", favoritos);
        return "proyectos/favoritos";
    }
}



