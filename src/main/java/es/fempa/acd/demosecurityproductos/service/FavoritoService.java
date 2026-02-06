package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Favorito;
import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.FavoritoRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestión de favoritos
 * Implementa las operaciones definidas en el UML
 */
@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;

    public FavoritoService(FavoritoRepository favoritoRepository) {
        this.favoritoRepository = favoritoRepository;
    }

    /**
     * Agrega un proyecto a los favoritos de un usuario
     * Método del UML: agregarFavorito()
     *
     * @param usuario el usuario
     * @param proyecto el proyecto a marcar como favorito
     * @return el favorito creado
     * @throws IllegalArgumentException si el proyecto ya está en favoritos
     */
    @Transactional
    public Favorito agregarFavorito(Usuario usuario, Proyecto proyecto) {
        if (favoritoRepository.findByUsuarioAndProyecto(usuario, proyecto).isPresent()) {
            throw new IllegalArgumentException("El proyecto ya está en favoritos");
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setProyecto(proyecto);

        return favoritoRepository.save(favorito);
    }

    /**
     * Lista los favoritos de un usuario
     * Método del UML: listarFavoritosPorUsuario()
     *
     * @param usuario el usuario
     * @return lista de favoritos del usuario
     */
    public List<Favorito> listarFavoritosPorUsuario(Usuario usuario) {
        return favoritoRepository.findByUsuario(usuario);
    }

    /**
     * Elimina un favorito por ID
     *
     * @param favoritoId ID del favorito
     */
    @Transactional
    public void eliminarFavorito(Long favoritoId) {
        favoritoRepository.deleteById(favoritoId);
    }

    /**
     * Elimina un favorito verificando permisos del usuario
     * Método del UML: eliminarFavorito()
     *
     * @param favoritoId ID del favorito
     * @param username nombre del usuario autenticado
     * @throws AccessDeniedException si no tiene permisos
     */
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

    /**
     * Verifica si un proyecto está en los favoritos de un usuario
     *
     * @param usuario el usuario
     * @param proyecto el proyecto
     * @return true si está en favoritos, false en caso contrario
     */
    public boolean esFavorito(Usuario usuario, Proyecto proyecto) {
        return favoritoRepository.findByUsuarioAndProyecto(usuario, proyecto).isPresent();
    }

    /**
     * Elimina un favorito dado un usuario y un proyecto
     *
     * @param usuario el usuario
     * @param proyecto el proyecto
     */
    @Transactional
    public void eliminarFavorito(Usuario usuario, Proyecto proyecto) {
        Favorito favorito = favoritoRepository.findByUsuarioAndProyecto(usuario, proyecto)
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no está en favoritos"));
        favoritoRepository.delete(favorito);
    }
}
