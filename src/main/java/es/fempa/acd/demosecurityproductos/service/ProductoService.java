package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Producto;
import es.fempa.acd.demosecurityproductos.repository.ProductoRepository;
import es.fempa.acd.demosecurityproductos.repository.FavoritoRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final FavoritoRepository favoritoRepository;

    public ProductoService(ProductoRepository productoRepository, FavoritoRepository favoritoRepository) {
        this.productoRepository = productoRepository;
        this.favoritoRepository = favoritoRepository;
    }

    // Crear un nuevo producto
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden guardar nuevos usuarios
    public Producto crearProducto(String nombre, String descripcion, double precio, String imagen) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setImagen(imagen);

        return productoRepository.save(producto);
    }

    // Listar todos los productos
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    // Método explícito para actualizar un producto existente
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden guardar nuevos usuarios
    public Producto actualizarProducto(Producto producto) {
        if (!productoRepository.existsById(producto.getId())) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + producto.getId());
        }
        return productoRepository.save(producto); // Guarda o actualiza según el ID
    }


    
    // Buscar un producto por su ID
    public Producto buscarPorId(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }
        return productoRepository.findById(id).get();
    }

    // Eliminar un producto
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden 
    public void eliminarProducto(Long id) {
    	// Verificar si hay referencias en la tabla Favorito
        if (favoritoRepository.existsByProductoId(id)) {// TODO: Comprobar si hay otro usuario que lo tiene en favoritos, sino borrar el producto
            throw new IllegalStateException("No se puede eliminar el producto porque está en la lista de favoritos de algún usuario.");
        }
        productoRepository.deleteById(id);
    }
    
    
    
    
}
