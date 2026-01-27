package es.fempa.acd.demosecurityproductos.controller;

import java.util.List;

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
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;

@Controller
public class AuthController {

	@Autowired
	UsuarioService usuarioService;
	
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
    @GetMapping("/cliente/dashboard")
	public String userDashboard() {
		return "cliente/dashboard";
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
            System.out.println("Redirigiendo a /cliente/dashboard");
        	return "redirect:/cliente/dashboard";
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
