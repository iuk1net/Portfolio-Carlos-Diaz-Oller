package es.fempa.acd.demosecurityproductos.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.fempa.acd.demosecurityproductos.model.Producto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.FavoritoService;
import es.fempa.acd.demosecurityproductos.service.ProductoService;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;

@Controller
@RequestMapping("/favoritos")
public class FavoritoController {

    private FavoritoService favoritoService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

	public FavoritoController(FavoritoService favoritoService, UsuarioService usuarioService, ProductoService productoService) {
		this.favoritoService = favoritoService;
		this.usuarioService = usuarioService;
		this.productoService = productoService;
	}
    
    @PostMapping("/{id}/eliminar")
    public String eliminarFavorito(@PathVariable Long id, Authentication authentication) {
        String userName = authentication.getName();
        Usuario usuario = usuarioService.buscarPorUsername(userName).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        favoritoService.eliminarFavorito(id, usuario.getUsername());
        return "redirect:/productos/favoritos";
    }
}
