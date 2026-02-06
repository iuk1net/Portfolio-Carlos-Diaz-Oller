package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.PublicacionRRSS;
import es.fempa.acd.demosecurityproductos.model.enums.EstadoPublicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad PublicacionRRSS
 * Gestiona las publicaciones de proyectos en redes sociales
 */
@Repository
public interface PublicacionRRSSRepository extends JpaRepository<PublicacionRRSS, Long> {

    /**
     * Obtiene todas las publicaciones de un proyecto
     * @param proyecto el proyecto
     * @return lista de publicaciones
     */
    List<PublicacionRRSS> findByProyecto(Proyecto proyecto);

    /**
     * Obtiene todas las publicaciones de un proyecto en una red social específica
     * @param proyecto el proyecto
     * @param redSocial nombre de la red social
     * @return lista de publicaciones
     */
    List<PublicacionRRSS> findByProyectoAndRedSocial(Proyecto proyecto, String redSocial);

    /**
     * Obtiene todas las publicaciones por estado
     * @param estado el estado de la publicación
     * @return lista de publicaciones
     */
    List<PublicacionRRSS> findByEstado(EstadoPublicacion estado);

    /**
     * Obtiene todas las publicaciones pendientes (para procesamiento)
     * @return lista de publicaciones pendientes
     */
    List<PublicacionRRSS> findByEstadoOrderByFechaPublicacionAsc(EstadoPublicacion estado);

    /**
     * Verifica si existe una publicación de un proyecto en una red social
     * @param proyecto el proyecto
     * @param redSocial nombre de la red social
     * @return true si existe
     */
    boolean existsByProyectoAndRedSocial(Proyecto proyecto, String redSocial);

    /**
     * Obtiene la última publicación de un proyecto en una red social
     * @param proyecto el proyecto
     * @param redSocial nombre de la red social
     * @return Optional con la publicación más reciente
     */
    Optional<PublicacionRRSS> findFirstByProyectoAndRedSocialOrderByFechaPublicacionDesc(
        Proyecto proyecto, String redSocial);

    /**
     * Cuenta cuántas publicaciones tiene un proyecto
     * @param proyecto el proyecto
     * @return número de publicaciones
     */
    long countByProyecto(Proyecto proyecto);
}

