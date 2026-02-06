package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Voto
 * Gestiona las operaciones de votación (likes) en proyectos
 */
@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    /**
     * Verifica si un usuario ya votó por un proyecto específico
     * @param usuario el usuario que vota
     * @param proyecto el proyecto votado
     * @return Optional con el voto si existe
     */
    Optional<Voto> findByUsuarioAndProyecto(Usuario usuario, Proyecto proyecto);

    /**
     * Obtiene todos los votos de un proyecto
     * @param proyecto el proyecto
     * @return lista de votos del proyecto
     */
    List<Voto> findByProyecto(Proyecto proyecto);

    /**
     * Obtiene todos los votos emitidos por un usuario
     * @param usuario el usuario
     * @return lista de votos del usuario
     */
    List<Voto> findByUsuario(Usuario usuario);

    /**
     * Cuenta cuántos votos tiene un proyecto
     * @param proyecto el proyecto
     * @return número de votos
     */
    long countByProyecto(Proyecto proyecto);

    /**
     * Verifica si existe un voto de un usuario para un proyecto
     * @param usuario el usuario
     * @param proyecto el proyecto
     * @return true si ya existe el voto
     */
    boolean existsByUsuarioAndProyecto(Usuario usuario, Proyecto proyecto);

    /**
     * Elimina un voto específico de un usuario para un proyecto
     * @param usuario el usuario
     * @param proyecto el proyecto
     */
    void deleteByUsuarioAndProyecto(Usuario usuario, Proyecto proyecto);
}

