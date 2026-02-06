package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    Optional<Proyecto> findById(Long id);

    @Query("SELECT p FROM Proyecto p LEFT JOIN FETCH p.usuario WHERE p.id = :id")
    Optional<Proyecto> findByIdWithUsuario(@Param("id") Long id);

    List<Proyecto> findByUsuario(Usuario usuario);
}
