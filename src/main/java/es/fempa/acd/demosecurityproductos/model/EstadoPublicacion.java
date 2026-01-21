package es.fempa.acd.demosecurityproductos.model;

/**
 * Enum EstadoPublicacion según modelo ER v2.0
 * Representa los estados posibles de una publicación en redes sociales
 */
public enum EstadoPublicacion {
    PENDIENTE,  // La publicación está en cola de procesamiento
    PUBLICADO,  // Se compartió exitosamente en la red social
    ERROR       // Falló la publicación (problema de conectividad, tokens expirados, etc.)
}

