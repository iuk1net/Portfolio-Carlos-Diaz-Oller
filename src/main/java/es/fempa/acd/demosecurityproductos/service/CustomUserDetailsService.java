package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("====== LOADING USER: " + username + " ======");

        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        System.out.println("Usuario encontrado: " + usuario.getEmail());
        System.out.println("Rol del usuario: " + usuario.getRol());
        System.out.println("Estado del usuario: " + usuario.getEstado());
        System.out.println("Email verificado: " + usuario.isEmailVerificado());

        // ⭐ VALIDACIÓN DE EMAIL VERIFICADO (v2.6.0)
        // Si el email no está verificado, deshabilitar la cuenta
        boolean cuentaHabilitada = usuario.isEmailVerificado();

        if (!cuentaHabilitada) {
            System.out.println("⚠️ ACCESO DENEGADO: Email no verificado para " + usuario.getEmail());
        }

        UserDetails userDetails = User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword()) // Contraseña encriptada
                .roles(usuario.getRol().name()) // Rol asignado
                .disabled(!cuentaHabilitada) // ⭐ Deshabilitar si email no verificado
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();

        System.out.println("UserDetails creado con authorities: " + userDetails.getAuthorities());
        System.out.println("Cuenta habilitada: " + userDetails.isEnabled());
        System.out.println("=========================================");

        return userDetails;
    }
}
