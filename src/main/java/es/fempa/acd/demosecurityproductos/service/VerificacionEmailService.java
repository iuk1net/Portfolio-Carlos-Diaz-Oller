package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.VerificacionEmail;
import es.fempa.acd.demosecurityproductos.model.enums.TipoVerificacion;
import es.fempa.acd.demosecurityproductos.repository.VerificacionEmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servicio para gestión de verificación de emails.
 * Maneja la lógica de negocio de tokens de verificación.
 *
 * @author Carlos Díaz Oller
 * @version 2.6.0
 * @since 2026-02-06
 */
@Service
@Transactional
public class VerificacionEmailService {

    private static final Logger log = LoggerFactory.getLogger(VerificacionEmailService.class);

    @Autowired
    private VerificacionEmailRepository verificacionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Crea y envía un token de verificación para registro.
     *
     * @param usuario el usuario a verificar
     * @return la verificación creada
     */
    public VerificacionEmail crearVerificacionRegistro(Usuario usuario) {
        log.info("Creando verificación de registro para usuario: {}", usuario.getEmail());

        // Eliminar verificaciones previas del mismo tipo si existen
        eliminarVerificacionesPrevias(usuario, TipoVerificacion.REGISTRO);

        // Crear nueva verificación
        VerificacionEmail verificacion = new VerificacionEmail(usuario, TipoVerificacion.REGISTRO);
        verificacion = verificacionRepository.save(verificacion);

        // Enviar email
        emailService.enviarEmailVerificacion(
            usuario.getEmail(),
            usuario.getNombre(),
            verificacion.getToken()
        );

        log.info("Verificación de registro creada para: {}", usuario.getEmail());
        return verificacion;
    }

    /**
     * Crea y envía un token de recuperación de contraseña.
     *
     * @param usuario el usuario que solicita recuperación
     * @return la verificación creada
     */
    public VerificacionEmail crearVerificacionRecuperacion(Usuario usuario) {
        log.info("Creando verificación de recuperación para usuario: {}", usuario.getEmail());

        // Eliminar verificaciones previas del mismo tipo si existen
        eliminarVerificacionesPrevias(usuario, TipoVerificacion.RECUPERACION);

        // Crear nueva verificación
        VerificacionEmail verificacion = new VerificacionEmail(usuario, TipoVerificacion.RECUPERACION);
        verificacion = verificacionRepository.save(verificacion);

        // Enviar email
        emailService.enviarEmailRecuperacion(
            usuario.getEmail(),
            usuario.getNombre(),
            verificacion.getToken()
        );

        log.info("Verificación de recuperación creada para: {}", usuario.getEmail());
        return verificacion;
    }

    /**
     * Verifica un token y activa la cuenta si es válido.
     *
     * @param token el token a verificar
     * @return true si la verificación fue exitosa, false en caso contrario
     * @throws IllegalArgumentException si el token es inválido o ha expirado
     */
    public boolean verificarToken(String token) {
        log.info("Verificando token: {}", token.substring(0, Math.min(10, token.length())) + "...");

        Optional<VerificacionEmail> optVerificacion = verificacionRepository.findByToken(token);

        if (optVerificacion.isEmpty()) {
            log.warn("Token no encontrado");
            throw new IllegalArgumentException("Token no válido");
        }

        VerificacionEmail verificacion = optVerificacion.get();

        // Verificar si el token ya fue usado
        if (verificacion.isUsado()) {
            log.warn("Token ya usado para usuario: {}", verificacion.getUsuario().getEmail());
            throw new IllegalArgumentException("Este enlace ya fue utilizado");
        }

        // Verificar si el token ha expirado
        if (verificacion.isExpirado()) {
            log.warn("Token expirado para usuario: {}", verificacion.getUsuario().getEmail());
            throw new IllegalArgumentException("Este enlace ha expirado. Solicita uno nuevo.");
        }

        // Marcar token como usado
        verificacion.marcarComoUsado();
        verificacionRepository.save(verificacion);

        // Activar cuenta si es verificación de registro
        if (verificacion.getTipo() == TipoVerificacion.REGISTRO) {
            Usuario usuario = verificacion.getUsuario();
            usuario.setEmailVerificado(true);
            usuarioService.guardar(usuario);

            // Enviar email de bienvenida
            emailService.enviarEmailBienvenida(usuario.getEmail(), usuario.getNombre());

            log.info("Cuenta verificada exitosamente para: {}", usuario.getEmail());
        }

        return true;
    }

    /**
     * Valida un token de recuperación de contraseña.
     *
     * @param token el token a validar
     * @return el usuario asociado al token
     * @throws IllegalArgumentException si el token es inválido o ha expirado
     */
    public Usuario validarTokenRecuperacion(String token) {
        log.info("Validando token de recuperación");

        Optional<VerificacionEmail> optVerificacion = verificacionRepository.findByToken(token);

        if (optVerificacion.isEmpty()) {
            log.warn("Token de recuperación no encontrado");
            throw new IllegalArgumentException("Token no válido");
        }

        VerificacionEmail verificacion = optVerificacion.get();

        // Verificar tipo
        if (verificacion.getTipo() != TipoVerificacion.RECUPERACION) {
            log.warn("Token no es de recuperación");
            throw new IllegalArgumentException("Token no válido para recuperación");
        }

        // Verificar si ya fue usado
        if (verificacion.isUsado()) {
            log.warn("Token de recuperación ya usado");
            throw new IllegalArgumentException("Este enlace ya fue utilizado");
        }

        // Verificar si ha expirado
        if (verificacion.isExpirado()) {
            log.warn("Token de recuperación expirado");
            throw new IllegalArgumentException("Este enlace ha expirado. Solicita uno nuevo.");
        }

        return verificacion.getUsuario();
    }

    /**
     * Marca un token de recuperación como usado (después de cambiar contraseña).
     *
     * @param token el token a marcar como usado
     */
    public void marcarTokenRecuperacionUsado(String token) {
        Optional<VerificacionEmail> optVerificacion = verificacionRepository.findByToken(token);

        if (optVerificacion.isPresent()) {
            VerificacionEmail verificacion = optVerificacion.get();
            verificacion.marcarComoUsado();
            verificacionRepository.save(verificacion);
            log.info("Token de recuperación marcado como usado");
        }
    }

    /**
     * Reenvía el email de verificación a un usuario.
     *
     * @param usuario el usuario
     * @return la nueva verificación creada
     */
    public VerificacionEmail reenviarVerificacion(Usuario usuario) {
        log.info("Reenviando verificación para usuario: {}", usuario.getEmail());

        // Verificar si ya está verificado
        if (usuario.isEmailVerificado()) {
            throw new IllegalArgumentException("La cuenta ya está verificada");
        }

        // Buscar verificación activa
        Optional<VerificacionEmail> optVerificacionActiva = verificacionRepository
            .findVerificacionActiva(usuario, TipoVerificacion.REGISTRO, LocalDateTime.now());

        if (optVerificacionActiva.isPresent()) {
            // Regenerar token de la verificación existente
            VerificacionEmail verificacion = optVerificacionActiva.get();
            verificacion.regenerarToken();
            verificacion = verificacionRepository.save(verificacion);

            // Reenviar email
            emailService.enviarEmailVerificacion(
                usuario.getEmail(),
                usuario.getNombre(),
                verificacion.getToken()
            );

            log.info("Verificación reenviada con token regenerado");
            return verificacion;
        } else {
            // Crear nueva verificación
            return crearVerificacionRegistro(usuario);
        }
    }

    /**
     * Elimina verificaciones previas no usadas de un usuario para un tipo específico.
     *
     * @param usuario el usuario
     * @param tipo el tipo de verificación
     */
    private void eliminarVerificacionesPrevias(Usuario usuario, TipoVerificacion tipo) {
        verificacionRepository.findByUsuarioAndTipo(usuario, tipo).stream()
            .filter(v -> !v.isUsado())
            .forEach(v -> {
                log.debug("Eliminando verificación previa: {}", v.getId());
                verificacionRepository.delete(v);
            });
    }

    /**
     * Limpia verificaciones expiradas (tarea de mantenimiento).
     * Debe ejecutarse periódicamente (ej: diariamente).
     */
    @Transactional
    public void limpiarVerificacionesExpiradas() {
        LocalDateTime fechaCorte = LocalDateTime.now().minusDays(7);
        log.info("Limpiando verificaciones anteriores a: {}", fechaCorte);

        verificacionRepository.deleteVerificacionesExpiradas(fechaCorte);
        log.info("Verificaciones expiradas eliminadas");
    }

    /**
     * Verifica si un usuario tiene verificaciones pendientes.
     *
     * @param usuario el usuario
     * @return true si tiene verificaciones pendientes, false en caso contrario
     */
    public boolean tieneVerificacionPendiente(Usuario usuario) {
        long count = verificacionRepository.countVerificacionesPendientes(usuario, LocalDateTime.now());
        return count > 0;
    }
}

