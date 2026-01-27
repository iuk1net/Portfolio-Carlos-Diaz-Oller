package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Favorito;
import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    // Obtener los favoritos de un usuario
    List<Favorito> findByUsuario(Usuario usuario);

    // Verificar si un proyecto ya está en los favoritos de un usuario
    Optional<Favorito> findByUsuarioAndProyecto(Usuario usuario, Proyecto proyecto);
    
    // Eliminar un favorito por proyecto y usuario
    void deleteByProyectoAndUsuario(Proyecto proyecto, Usuario usuario);

    // Verificar si existe un favorito para un proyecto específico
    boolean existsByProyectoId(Long id);

}
