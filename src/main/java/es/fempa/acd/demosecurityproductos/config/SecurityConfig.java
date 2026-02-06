package es.fempa.acd.demosecurityproductos.config;

import es.fempa.acd.demosecurityproductos.model.enums.Rol;
import es.fempa.acd.demosecurityproductos.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // Habilita seguridad en métodos como @PreAuthorize
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                         CustomAuthenticationSuccessHandler successHandler,
                         CustomAuthenticationFailureHandler failureHandler) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http	
        		.authenticationProvider(authenticationProvider())
        		.cors(Customizer.withDefaults())
        		// CSRF habilitado con excepción para API REST
        		.csrf(csrf -> csrf
        		        .ignoringRequestMatchers("/api/**") // APIs REST AJAX sin CSRF
        		)
                .authorizeHttpRequests(auth -> auth
                        // Recursos estáticos públicos
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/webjars/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll() // Imágenes subidas por usuarios (galerías, CVs, etc.)
                        .requestMatchers("/error", "/error/**").permitAll()
                        .requestMatchers("/login", "/", "/logout", "/register").permitAll()

                        // Verificación de email - públicas (v2.6.0)
                        .requestMatchers("/verificar-email", "/reenviar-verificacion",
                                       "/solicitar-recuperacion", "/recuperar-password").permitAll()

                        // Proyectos - Vista pública (ROL VISUALIZADOR)
                        .requestMatchers("/proyectos/lista", "/proyectos/ranking").permitAll()
                        .requestMatchers("/proyectos/{id}").permitAll() // Detalle de proyecto PÚBLICO
                        .requestMatchers("/proyectos/nuevo", "/proyectos/*/editar", "/proyectos/*/eliminar").authenticated()
                        .requestMatchers("/proyectos/favoritos").authenticated()
                        .requestMatchers("/proyectos/*/votar", "/proyectos/*/favorito", "/proyectos/*/quitar-favorito").authenticated()

                        // Perfiles públicos de usuario (sin autenticación)
                        .requestMatchers("/usuarios/perfil/**").permitAll()

                        // Dashboards por rol
                        .requestMatchers("/admin/**").hasRole(Rol.ADMIN.name())
                        .requestMatchers("/usuario/**").hasRole(Rol.USER.name())

                        // Gestión de usuarios - solo admin
                        .requestMatchers("/usuarios/**").hasRole(Rol.ADMIN.name())

                        // Favoritos - requiere autenticación
                        .requestMatchers("/favoritos/**").authenticated()

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler) // ⭐ Handler personalizado para email no verificado
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/error/403")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
