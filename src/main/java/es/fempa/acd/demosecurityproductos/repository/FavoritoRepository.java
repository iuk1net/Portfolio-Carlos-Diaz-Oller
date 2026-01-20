package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Favorito;
import es.fempa.acd.demosecurityproductos.model.Producto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    // Obtener los favoritos de un usuario
    List<Favorito> findByUsuario(Usuario usuario);

    // Verificar si un producto ya está en los favoritos de un usuario
    Optional<Favorito> findByUsuarioAndProducto(Usuario usuario, Producto producto);
    
    
    void deleteByProductoAndUsuario(Producto producto, Usuario usuario);

	boolean existsByProductoId(Long id);
    
}
