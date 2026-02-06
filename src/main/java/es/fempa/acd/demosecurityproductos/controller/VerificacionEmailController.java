package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import es.fempa.acd.demosecurityproductos.service.VerificacionEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controlador para gestión de verificación de email.
 * Maneja los endpoints de verificación de cuenta y recuperación de contraseña.
 *
 * @author Carlos Díaz Oller
 * @version 2.6.0
 * @since 2026-02-06
 */
@Controller
public class VerificacionEmailController {

    private static final Logger log = LoggerFactory.getLogger(VerificacionEmailController.class);

    @Autowired
    private VerificacionEmailService verificacionService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Procesa la verificación de email mediante token.
     *
     * @param token el token de verificación
     * @param model modelo para la vista
     * @return vista de éxito o error
     */
    @GetMapping("/verificar-email")
    public String verificarEmail(@RequestParam("token") String token, Model model) {
        log.info("Procesando verificación de email con token");

        try {
            // Verificar el token y activar la cuenta
            boolean verificado = verificacionService.verificarToken(token);

            if (verificado) {
                log.info("Email verificado exitosamente");
                model.addAttribute("mensaje", "¡Tu cuenta ha sido verificada exitosamente!");
                model.addAttribute("subtitulo", "Ya puedes iniciar sesión");
                model.addAttribute("tipo", "exito");
                return "verificacion/verificacion-exitosa";
            }
        } catch (IllegalArgumentException e) {
            log.warn("Error al verificar token: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tipo", "error");
            return "verificacion/verificacion-error";
        }

        model.addAttribute("error", "Token inválido o expirado");
        model.addAttribute("tipo", "error");
        return "verificacion/verificacion-error";
    }

    /**
     * Muestra el formulario para reenviar email de verificación.
     *
     * @return vista del formulario
     */
    @GetMapping("/reenviar-verificacion")
    public String mostrarReenviarVerificacion() {
        return "verificacion/reenviar-verificacion";
    }

    /**
     * Procesa el reenvío del email de verificación.
     *
     * @param email el email del usuario
     * @param redirectAttributes atributos para redirección
     * @return redirección con mensaje
     */
    @PostMapping("/reenviar-verificacion")
    public String reenviarVerificacion(
            @RequestParam("email") String email,
            RedirectAttributes redirectAttributes) {

        log.info("Reenviando verificación para: {}", email);

        try {
            Optional<Usuario> optUsuario = usuarioService.buscarPorEmail(email);

            if (optUsuario.isEmpty()) {
                redirectAttributes.addFlashAttribute("error",
                    "No existe ninguna cuenta con ese email");
                return "redirect:/reenviar-verificacion";
            }

            Usuario usuario = optUsuario.get();

            // Verificar si ya está verificado
            if (usuario.isEmailVerificado()) {
                redirectAttributes.addFlashAttribute("info",
                    "Tu cuenta ya está verificada. Puedes iniciar sesión.");
                return "redirect:/login";
            }

            // Reenviar verificación
            verificacionService.reenviarVerificacion(usuario);

            redirectAttributes.addFlashAttribute("exito",
                "Email de verificación reenviado. Revisa tu bandeja de entrada.");
            return "redirect:/login";

        } catch (Exception e) {
            log.error("Error al reenviar verificación: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error",
                "Error al reenviar el email. Inténtalo de nuevo más tarde.");
            return "redirect:/reenviar-verificacion";
        }
    }

    /**
     * Muestra el formulario para solicitar recuperación de contraseña.
     *
     * @return vista del formulario
     */
    @GetMapping("/solicitar-recuperacion")
    public String mostrarSolicitarRecuperacion() {
        return "verificacion/solicitar-recuperacion";
    }

    /**
     * Procesa la solicitud de recuperación de contraseña.
     *
     * @param email el email del usuario
     * @param redirectAttributes atributos para redirección
     * @return redirección con mensaje
     */
    @PostMapping("/solicitar-recuperacion")
    public String solicitarRecuperacion(
            @RequestParam("email") String email,
            RedirectAttributes redirectAttributes) {

        log.info("Solicitando recuperación de contraseña para: {}", email);

        try {
            Optional<Usuario> optUsuario = usuarioService.buscarPorEmail(email);

            if (optUsuario.isEmpty()) {
                // Por seguridad, no revelar si el email existe
                redirectAttributes.addFlashAttribute("exito",
                    "Si el email existe, recibirás instrucciones para recuperar tu contraseña.");
                return "redirect:/login";
            }

            Usuario usuario = optUsuario.get();

            // Crear token de recuperación
            verificacionService.crearVerificacionRecuperacion(usuario);

            redirectAttributes.addFlashAttribute("exito",
                "Email de recuperación enviado. Revisa tu bandeja de entrada.");
            return "redirect:/login";

        } catch (Exception e) {
            log.error("Error al solicitar recuperación: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error",
                "Error al procesar tu solicitud. Inténtalo de nuevo más tarde.");
            return "redirect:/solicitar-recuperacion";
        }
    }

    /**
     * Muestra el formulario para establecer nueva contraseña.
     *
     * @param token el token de recuperación
     * @param model modelo para la vista
     * @return vista del formulario o error
     */
    @GetMapping("/recuperar-password")
    public String mostrarRecuperarPassword(
            @RequestParam("token") String token,
            Model model) {

        log.info("Mostrando formulario de recuperación de contraseña");

        try {
            // Validar el token
            Usuario usuario = verificacionService.validarTokenRecuperacion(token);

            model.addAttribute("token", token);
            model.addAttribute("email", usuario.getEmail());
            return "verificacion/recuperar-password";

        } catch (IllegalArgumentException e) {
            log.warn("Token de recuperación inválido: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tipo", "recuperacion");
            return "verificacion/verificacion-error";
        }
    }

    /**
     * Procesa el cambio de contraseña.
     *
     * @param token el token de recuperación
     * @param password la nueva contraseña
     * @param passwordConfirm confirmación de la contraseña
     * @param redirectAttributes atributos para redirección
     * @return redirección con mensaje
     */
    @PostMapping("/recuperar-password")
    public String recuperarPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("passwordConfirm") String passwordConfirm,
            RedirectAttributes redirectAttributes) {

        log.info("Procesando cambio de contraseña");

        try {
            // Validar contraseñas
            if (!password.equals(passwordConfirm)) {
                redirectAttributes.addFlashAttribute("error",
                    "Las contraseñas no coinciden");
                return "redirect:/recuperar-password?token=" + token;
            }

            if (password.length() < 6) {
                redirectAttributes.addFlashAttribute("error",
                    "La contraseña debe tener al menos 6 caracteres");
                return "redirect:/recuperar-password?token=" + token;
            }

            // Validar token y obtener usuario
            Usuario usuario = verificacionService.validarTokenRecuperacion(token);

            // Cambiar contraseña
            usuario.setPassword(passwordEncoder.encode(password));
            usuarioService.guardar(usuario);

            // Marcar token como usado
            verificacionService.marcarTokenRecuperacionUsado(token);

            log.info("Contraseña cambiada exitosamente para: {}", usuario.getEmail());

            redirectAttributes.addFlashAttribute("exito",
                "Contraseña cambiada exitosamente. Ya puedes iniciar sesión.");
            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            log.error("Error al cambiar contraseña: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/solicitar-recuperacion";
        }
    }

    /**
     * Endpoint para que usuarios autenticados puedan reenviar su verificación.
     *
     * @param userDetails detalles del usuario autenticado
     * @param redirectAttributes atributos para redirección
     * @return redirección con mensaje
     */
    @PostMapping("/mi-cuenta/reenviar-verificacion")
    public String reenviarVerificacionAutenticado(
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        try {
            Optional<Usuario> optUsuario = usuarioService.buscarPorEmail(userDetails.getUsername());

            if (optUsuario.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/usuario/dashboard";
            }

            Usuario usuario = optUsuario.get();

            if (usuario.isEmailVerificado()) {
                redirectAttributes.addFlashAttribute("info",
                    "Tu email ya está verificado");
                return "redirect:/usuario/dashboard";
            }

            verificacionService.reenviarVerificacion(usuario);

            redirectAttributes.addFlashAttribute("exito",
                "Email de verificación reenviado. Revisa tu bandeja de entrada.");
            return "redirect:/usuario/dashboard";

        } catch (Exception e) {
            log.error("Error al reenviar verificación: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error",
                "Error al reenviar el email");
            return "redirect:/usuario/dashboard";
        }
    }
}

