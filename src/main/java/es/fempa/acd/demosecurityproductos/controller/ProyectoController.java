package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Favorito;
import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import es.fempa.acd.demosecurityproductos.service.FavoritoService;
import es.fempa.acd.demosecurityproductos.service.ProyectoService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;
    private final FavoritoService favoritoService;
    private final UsuarioService usuarioService;


    public ProyectoController(ProyectoService proyectoService, FavoritoService favoritoService, UsuarioService usuarioService ) {
        this.proyectoService = proyectoService;
        this.favoritoService = favoritoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/lista")
    public String listarProductos(Model model) {
        List<Proyecto> proyectos = proyectoService.listarProyectos();
        model.addAttribute("productos", proyectos);
        return "proyectos/lista"; // Apunta a una plantilla Thymeleaf
    }




    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden acceder a la página de edición de productos
    public String mostrarFormularioNuevoProducto() {
        return "proyectos/nuevo"; // Devuelve la plantilla "editar.html"
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden crear productos
    public String crearProducto(@RequestParam String titulo,
                                @RequestParam String descripcion,
                                @RequestParam(required = false) String tecnologias,
                                @RequestParam(required = false) String enlaceWeb,
                                Authentication authentication) {
        // Obtener usuario autenticado
        String userName = authentication.getName();
        Usuario usuario = usuarioService.buscarPorUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Crear proyecto
        Proyecto proyecto = new Proyecto();
        proyecto.setTitulo(titulo);
        proyecto.setDescripcion(descripcion);
        proyecto.setTecnologias(tecnologias);
        proyecto.setEnlaceWeb(enlaceWeb);

        proyectoService.crearProyecto(proyecto, usuario);
        return "redirect:/proyectos/lista";
    }

    @PostMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden editar productos
    public String editarProducto(@PathVariable Long id,
                                  @RequestParam String titulo,
                                  @RequestParam String descripcion,
                                  @RequestParam(required = false) String tecnologias,
                                  @RequestParam(required = false) String enlaceWeb,
                                  Authentication authentication) {
        Proyecto proyecto = proyectoService.buscarPorId(id);
        proyecto.setTitulo(titulo);
        proyecto.setDescripcion(descripcion);
        proyecto.setTecnologias(tecnologias);
        proyecto.setEnlaceWeb(enlaceWeb);

        // Llama al servicio para guardar el proyecto actualizado
        proyectoService.actualizarProyecto(id, proyecto, authentication.getName());

        return "redirect:/proyectos/lista";
    }



    @PostMapping("/{id}/favoritar")
    public String favoritarProducto(@PathVariable Long id, Authentication authentication ) {
        Proyecto proyecto = proyectoService.buscarPorId(id);// el servicio lanza la excepción si no se encuentra el producto

        // Obtener el nombre del usuario autenticado
        String userName = authentication.getName();
        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioService.buscarPorUsername(userName).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));


        favoritoService.agregarFavorito(usuario, proyecto);
        return "redirect:/proyectos/lista";
    }


    @GetMapping("/favoritos")
    public String listarFavoritos(Model model, Authentication authentication) {
    	 // Obtener el nombre del usuario autenticado
        String userName = authentication.getName();
        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioService.buscarPorUsername(userName).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<Favorito> favoritos = favoritoService.listarFavoritosPorUsuario(usuario);
        model.addAttribute("favoritos", favoritos);
        return "proyectos/favoritos";
    }


    @GetMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden acceder a la página de edición de productos
    public String mostrarFormularioEditarProducto(@PathVariable Long id, Model model) {
        Proyecto proyecto = proyectoService.buscarPorId(id); // Obtén el producto existente por su ID
        model.addAttribute("producto", proyecto); // Agrega el producto al modelo
        return "proyectos/editar"; // Devuelve la plantilla "editar.html"
    }



    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden eliminar productos
    public String eliminarProducto(@PathVariable Long id) {
            proyectoService.eliminarProyectoAdmin(id);
            return "redirect:/proyectos/lista";
    }


//    @PostMapping("/{id}/eliminar")
//    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden eliminar productos
//    public String eliminarProducto(@PathVariable Long id, Model model) {
//    	try {
//    		productoService.eliminarProducto(id);
//    		return "redirect:/productos/lista";
//    	} catch (IllegalStateException ex) {
//    		model.addAttribute("error", ex.getMessage());
//    		return "error/409"; // Devuelve la vista Thymeleaf directamente
//    	}
//    }

}

