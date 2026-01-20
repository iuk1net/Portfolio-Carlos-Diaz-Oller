package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Favorito;
import es.fempa.acd.demosecurityproductos.model.Producto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.FavoritoRepository;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;

    public FavoritoService(FavoritoRepository favoritoRepository) {
        this.favoritoRepository = favoritoRepository;
    }

    // Agregar un producto a los favoritos de un usuario
    @Transactional
    public Favorito agregarFavorito(Usuario usuario, Producto producto) {
        if (favoritoRepository.findByUsuarioAndProducto(usuario, producto).isPresent()) {
            throw new IllegalArgumentException("El producto ya está en favoritos");
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setProducto(producto);

        return favoritoRepository.save(favorito);
    }

    // Listar los favoritos de un usuario
    public List<Favorito> listarFavoritosPorUsuario(Usuario usuario) {
        return favoritoRepository.findByUsuario(usuario);
    }

    // Eliminar un favorito
    public void eliminarFavorito(Long favoritoId) {
        favoritoRepository.deleteById(favoritoId);
    }

    
    
    @Transactional
    public void eliminarFavorito(Long favoritoId, String username) {
        // Buscar el favorito por ID
        Favorito favorito = favoritoRepository.findById(favoritoId)
                .orElseThrow(() -> new IllegalArgumentException("Favorito no encontrado"));

        // Verificar que el favorito pertenece al usuario autenticado
        if (!favorito.getUsuario().getUsername().equals(username)) {
            throw new AccessDeniedException("No tienes permisos para eliminar este favorito.");
        }

        // Eliminar el favorito
        favoritoRepository.delete(favorito);
    }


    
}
