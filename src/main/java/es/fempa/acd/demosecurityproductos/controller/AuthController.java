package es.fempa.acd.demosecurityproductos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.fempa.acd.demosecurityproductos.model.Favorito;
import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.FavoritoService;
import es.fempa.acd.demosecurityproductos.service.ProyectoService;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import es.fempa.acd.demosecurityproductos.service.VotoService;

@Controller
public class AuthController {

	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	ProyectoService proyectoService;

	@Autowired
	FavoritoService favoritoService;

	@Autowired
	VotoService votoService;

    @GetMapping("/login")
    public String login() {
        return "login"; // Apunta a una plantilla Thymeleaf llamada login.html
    }
    
    @GetMapping("/")
    public String home() {
        return "login"; // Apunta a una plantilla Thymeleaf llamada login.html
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }
    
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/usuario/dashboard")
	public String userDashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Cargar proyectos del usuario
        List<Proyecto> proyectos = proyectoService.listarProyectosPorUsuario(usuario);

        // Calcular estadísticas
        int totalProyectos = proyectos.size();
        int totalVotos = proyectos.stream().mapToInt(Proyecto::getTotalLikes).sum();
        int totalFavoritos = favoritoService.listarFavoritosPorUsuario(usuario).size();
        int totalVotosEmitidos = votoService.obtenerVotosPorUsuario(usuario).size();

        // Crear objeto de estadísticas
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalProyectos", totalProyectos);
        stats.put("totalVotos", totalVotos);
        stats.put("totalFavoritos", totalFavoritos);
        stats.put("totalVotosEmitidos", totalVotosEmitidos);

        model.addAttribute("usuario", usuario);
        model.addAttribute("stats", stats);
        model.addAttribute("proyectos", proyectos.stream().limit(5).collect(Collectors.toList()));

		return "usuario/dashboard";
	}
    
    
    // Ya no se usa este endpoint - ahora se usa CustomAuthenticationSuccessHandler
    /*
    @RequestMapping("/default")
    public String defaultAfterLogin(Authentication authentication, Model model) {
        
        // Debug: Imprimir información de autenticación
        System.out.println("========== DEBUG AUTHENTICATION ==========");
        System.out.println("Usuario autenticado: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());
        System.out.println("Is Authenticated: " + authentication.isAuthenticated());
        System.out.println("==========================================");

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
          	 // Obtener el nombre del usuario autenticado
            String userName = authentication.getName();
            // Buscar el usuario en la base de datos
            Usuario usuario = usuarioService.buscarPorEmail(userName).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            model.addAttribute("usuario", usuario);
            System.out.println("Redirigiendo a /usuario/dashboard");
        	return "redirect:/usuario/dashboard";
        }
        else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
       	 // Obtener el nombre del usuario autenticado
            String userName = authentication.getName();
            // Buscar el usuario en la base de datos
            Usuario usuario = usuarioService.buscarPorEmail(userName).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            model.addAttribute("usuario", usuario);
            System.out.println("Redirigiendo a /admin/dashboard");
            return "redirect:/admin/dashboard";
        }
        System.out.println("No se encontró rol apropiado, redirigiendo a /login");
        return "redirect:/login";
    }
    */

    
}
