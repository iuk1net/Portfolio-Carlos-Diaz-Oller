package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Proyecto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    // Ejemplo: Buscar productos por nombre (opcional)
    // List<Producto> findByNombreContainingIgnoreCase(String nombre);
	
	Optional<Proyecto> findById(Long id);
}
