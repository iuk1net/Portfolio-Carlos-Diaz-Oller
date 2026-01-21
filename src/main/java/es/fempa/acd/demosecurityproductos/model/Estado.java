package es.fempa.acd.demosecurityproductos.model;

/**
 * Enum Estado según modelo ER v2.0
 * Representa los estados posibles de una cuenta de usuario
 */
public enum Estado {
    ACTIVO,     // Usuario puede usar la plataforma normalmente
    BLOQUEADO   // Usuario suspendido por el administrador, sin acceso
}

