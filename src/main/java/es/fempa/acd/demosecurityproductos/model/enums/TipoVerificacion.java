package es.fempa.acd.demosecurityproductos.model.enums;

/**
 * Enum que define los tipos de verificación de email disponibles.
 *
 * @author Carlos Díaz Oller
 * @version 2.6.0
 * @since 2026-02-06
 */
public enum TipoVerificacion {

    /**
     * Verificación para activación de cuenta nueva.
     * Se envía cuando un usuario se registra por primera vez.
     */
    REGISTRO,

    /**
     * Verificación para recuperación de contraseña.
     * Se envía cuando un usuario solicita resetear su contraseña.
     */
    RECUPERACION
}

