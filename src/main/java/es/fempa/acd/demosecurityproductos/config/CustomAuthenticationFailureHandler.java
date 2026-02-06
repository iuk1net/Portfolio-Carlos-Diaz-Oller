package es.fempa.acd.demosecurityproductos.config;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Manejador personalizado de fallos de autenticación.
 * Detecta cuando una cuenta está deshabilitada (email no verificado)
 * y redirige con un mensaje específico.
 *
 * @author Carlos Díaz Oller
 * @version 2.6.0
 * @since 2026-02-06
 */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                       HttpServletResponse response,
                                       AuthenticationException exception)
            throws IOException, ServletException {

        String redirectUrl;

        // Si la cuenta está deshabilitada (email no verificado)
        if (exception instanceof DisabledException) {
            System.out.println("⚠️ Intento de login con cuenta deshabilitada (email no verificado)");
            redirectUrl = "/login?disabled=true";
        } else {
            // Otros errores de autenticación
            redirectUrl = "/login?error=true";
        }

        // Redirigir con el parámetro apropiado
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}

