package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Favorito;
import es.fempa.acd.demosecurityproductos.model.Producto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import es.fempa.acd.demosecurityproductos.service.FavoritoService;
import es.fempa.acd.demosecurityproductos.service.ProductoService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final FavoritoService favoritoService;
    private final UsuarioService usuarioService;


    public ProductoController(ProductoService productoService, FavoritoService favoritoService, UsuarioService usuarioService ) {
        this.productoService = productoService;
        this.favoritoService = favoritoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/lista")
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.listarProductos();
        model.addAttribute("productos", productos);
        return "productos/lista"; // Apunta a una plantilla Thymeleaf
    }

    
    
    
    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden acceder a la página de edición de productos
    public String mostrarFormularioNuevoProducto() {
        return "productos/nuevo"; // Devuelve la plantilla "editar.html"
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden crear productos
    public String crearProducto(@RequestParam String nombre,
                                @RequestParam String descripcion,
                                @RequestParam double precio,
                                @RequestParam String imagen) {
        productoService.crearProducto(nombre, descripcion, precio, imagen);
        return "redirect:/productos/lista";
    }

    @PostMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden editar productos
    public String editarProducto(@PathVariable Long id,
                                  @RequestParam String nombre,
                                  @RequestParam String descripcion,
                                  @RequestParam double precio,
                                  @RequestParam String imagen) {
        Producto producto = productoService.buscarPorId(id);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setImagen(imagen);

        // Llama al servicio para guardar el producto actualizado
        productoService.actualizarProducto(producto);

        return "redirect:/productos/lista";
    }
    
    
    
    @PostMapping("/{id}/favoritar")
    public String favoritarProducto(@PathVariable Long id, Authentication authentication ) {
        Producto producto = productoService.buscarPorId(id);// el servicio lanza la excepción si no se encuentra el producto
        
        // Obtener el nombre del usuario autenticado
        String userName = authentication.getName();
        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioService.buscarPorUsername(userName).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        
        favoritoService.agregarFavorito(usuario, producto);
        return "redirect:/productos/lista";
    }
    
    
    @GetMapping("/favoritos")
    public String listarFavoritos(Model model, Authentication authentication) {
    	 // Obtener el nombre del usuario autenticado
        String userName = authentication.getName();
        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioService.buscarPorUsername(userName).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        List<Favorito> favoritos = favoritoService.listarFavoritosPorUsuario(usuario);
        model.addAttribute("favoritos", favoritos);
        return "productos/favoritos";
    }

    
    @GetMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden acceder a la página de edición de productos
    public String mostrarFormularioEditarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.buscarPorId(id); // Obtén el producto existente por su ID
        model.addAttribute("producto", producto); // Agrega el producto al modelo
        return "productos/editar"; // Devuelve la plantilla "editar.html"
    }


    
    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden eliminar productos
    public String eliminarProducto(@PathVariable Long id, Model model) {
            productoService.eliminarProducto(id);
            return "redirect:/productos/lista";
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
