package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.PublicacionRRSS;
import es.fempa.acd.demosecurityproductos.model.enums.EstadoPublicacion;
import es.fempa.acd.demosecurityproductos.repository.PublicacionRRSSRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de publicaciones en redes sociales
 * Implementa las operaciones para compartir proyectos en RRSS
 */
@Service
public class PublicacionRRSSService {

    private final PublicacionRRSSRepository publicacionRRSSRepository;

    // Redes sociales soportadas
    private static final List<String> REDES_SOCIALES_VALIDAS =
        List.of("LinkedIn", "Twitter", "Facebook", "Instagram", "GitHub");

    public PublicacionRRSSService(PublicacionRRSSRepository publicacionRRSSRepository) {
        this.publicacionRRSSRepository = publicacionRRSSRepository;
    }

    /**
     * Publica un proyecto en una red social
     * Método relacionado con el UML: publicarEnRRSS() de Proyecto
     *
     * @param proyecto el proyecto a publicar
     * @param redSocial nombre de la red social
     * @return la publicación creada
     * @throws IllegalArgumentException si la red social no es válida
     */
    @Transactional
    public PublicacionRRSS publicarEnRedSocial(Proyecto proyecto, String redSocial) {
        // Validar red social
        if (!REDES_SOCIALES_VALIDAS.contains(redSocial)) {
            throw new IllegalArgumentException(
                "Red social no válida. Válidas: " + String.join(", ", REDES_SOCIALES_VALIDAS)
            );
        }

        // Crear la publicación con estado PENDIENTE
        PublicacionRRSS publicacion = new PublicacionRRSS(proyecto, redSocial);
        publicacion.setEstado(EstadoPublicacion.PENDIENTE);
        publicacion.setFechaPublicacion(LocalDateTime.now());

        // Guardar la publicación
        publicacion = publicacionRRSSRepository.save(publicacion);

        // TODO: Aquí se integraría con la API de la red social
        // Por ahora, marcamos como PUBLICADO automáticamente
        // En producción, esto sería un proceso asíncrono
        simularPublicacion(publicacion);

        return publicacion;
    }

    /**
     * Simula la publicación en la red social
     * En producción, esto se integraría con APIs reales (OAuth, etc.)
     *
     * @param publicacion la publicación a procesar
     */
    private void simularPublicacion(PublicacionRRSS publicacion) {
        // Simulación: marcar como publicado
        // En producción esto sería asíncrono y podría fallar
        try {
            // Aquí iría la lógica de integración con la API
            // Por ejemplo: LinkedInAPI.post(proyecto.getTitulo(), proyecto.getDescripcion())

            publicacion.setEstado(EstadoPublicacion.PUBLICADO);
            publicacionRRSSRepository.save(publicacion);
        } catch (Exception e) {
            publicacion.setEstado(EstadoPublicacion.ERROR);
            publicacionRRSSRepository.save(publicacion);
        }
    }

    /**
     * Obtiene todas las publicaciones de un proyecto
     *
     * @param proyecto el proyecto
     * @return lista de publicaciones
     */
    public List<PublicacionRRSS> obtenerPublicacionesPorProyecto(Proyecto proyecto) {
        return publicacionRRSSRepository.findByProyecto(proyecto);
    }

    /**
     * Obtiene publicaciones de un proyecto en una red social específica
     *
     * @param proyecto el proyecto
     * @param redSocial nombre de la red social
     * @return lista de publicaciones
     */
    public List<PublicacionRRSS> obtenerPublicacionesPorProyectoYRedSocial(
        Proyecto proyecto, String redSocial) {
        return publicacionRRSSRepository.findByProyectoAndRedSocial(proyecto, redSocial);
    }

    /**
     * Actualiza el estado de una publicación
     *
     * @param publicacionId ID de la publicación
     * @param nuevoEstado el nuevo estado
     * @return la publicación actualizada
     * @throws IllegalArgumentException si la publicación no existe
     */
    @Transactional
    public PublicacionRRSS actualizarEstadoPublicacion(
        Long publicacionId, EstadoPublicacion nuevoEstado) {

        PublicacionRRSS publicacion = publicacionRRSSRepository.findById(publicacionId)
            .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada"));

        publicacion.setEstado(nuevoEstado);
        return publicacionRRSSRepository.save(publicacion);
    }

    /**
     * Reintenta una publicación fallida
     *
     * @param publicacionId ID de la publicación
     * @return la publicación actualizada
     * @throws IllegalArgumentException si la publicación no existe o no está en ERROR
     */
    @Transactional
    public PublicacionRRSS reintentarPublicacion(Long publicacionId) {
        PublicacionRRSS publicacion = publicacionRRSSRepository.findById(publicacionId)
            .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada"));

        if (publicacion.getEstado() != EstadoPublicacion.ERROR) {
            throw new IllegalArgumentException(
                "Solo se pueden reintentar publicaciones en estado ERROR"
            );
        }

        // Cambiar a PENDIENTE y reintentar
        publicacion.setEstado(EstadoPublicacion.PENDIENTE);
        publicacion.setFechaPublicacion(LocalDateTime.now());
        publicacion = publicacionRRSSRepository.save(publicacion);

        // Reintentar publicación
        simularPublicacion(publicacion);

        return publicacion;
    }

    /**
     * Obtiene todas las publicaciones pendientes
     * Útil para procesamiento batch/asíncrono
     *
     * @return lista de publicaciones pendientes
     */
    public List<PublicacionRRSS> obtenerPublicacionesPendientes() {
        return publicacionRRSSRepository.findByEstadoOrderByFechaPublicacionAsc(
            EstadoPublicacion.PENDIENTE
        );
    }

    /**
     * Obtiene todas las publicaciones con errores
     *
     * @return lista de publicaciones con error
     */
    public List<PublicacionRRSS> obtenerPublicacionesConError() {
        return publicacionRRSSRepository.findByEstado(EstadoPublicacion.ERROR);
    }

    /**
     * Verifica si un proyecto ya fue publicado en una red social
     *
     * @param proyecto el proyecto
     * @param redSocial nombre de la red social
     * @return true si ya existe una publicación
     */
    public boolean yaPublicadoEn(Proyecto proyecto, String redSocial) {
        return publicacionRRSSRepository.existsByProyectoAndRedSocial(proyecto, redSocial);
    }

    /**
     * Cuenta cuántas publicaciones tiene un proyecto
     *
     * @param proyecto el proyecto
     * @return número de publicaciones
     */
    public long contarPublicaciones(Proyecto proyecto) {
        return publicacionRRSSRepository.countByProyecto(proyecto);
    }

    /**
     * Obtiene la última publicación de un proyecto en una red social
     *
     * @param proyecto el proyecto
     * @param redSocial nombre de la red social
     * @return Optional con la publicación más reciente
     */
    public Optional<PublicacionRRSS> obtenerUltimaPublicacion(
        Proyecto proyecto, String redSocial) {
        return publicacionRRSSRepository.findFirstByProyectoAndRedSocialOrderByFechaPublicacionDesc(
            proyecto, redSocial
        );
    }

    /**
     * Elimina una publicación
     *
     * @param publicacionId ID de la publicación
     */
    @Transactional
    public void eliminarPublicacion(Long publicacionId) {
        publicacionRRSSRepository.deleteById(publicacionId);
    }

    /**
     * Obtiene las redes sociales soportadas
     *
     * @return lista de redes sociales válidas
     */
    public List<String> obtenerRedesSocialesSoportadas() {
        return REDES_SOCIALES_VALIDAS;
    }
}

