package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.CV;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad CV
 * Proporciona operaciones CRUD y consultas personalizadas para CVs
 */
@Repository
public interface CVRepository extends JpaRepository<CV, Long> {

    /**
     * Encuentra todos los CVs de un usuario
     * @param usuario el usuario propietario
     * @return lista de CVs del usuario
     */
    List<CV> findByUsuario(Usuario usuario);

    /**
     * Encuentra todos los CVs de un usuario ordenados por fecha de subida descendente
     * @param usuario el usuario propietario
     * @return lista de CVs ordenados por fecha (más reciente primero)
     */
    List<CV> findByUsuarioOrderByFechaSubidaDesc(Usuario usuario);

    /**
     * Encuentra el CV más reciente de un usuario
     * @param usuario el usuario propietario
     * @return Optional con el CV más reciente, o vacío si no existe
     */
    Optional<CV> findFirstByUsuarioOrderByFechaSubidaDesc(Usuario usuario);

    /**
     * Cuenta cuántos CVs tiene un usuario
     * @param usuario el usuario propietario
     * @return número de CVs
     */
    long countByUsuario(Usuario usuario);
}

