package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Producto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Ejemplo: Buscar productos por nombre (opcional)
    // List<Producto> findByNombreContainingIgnoreCase(String nombre);
	
	Optional<Producto> findById(Long id);
}
