package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.FavoritoRepository;
import es.fempa.acd.demosecurityproductos.repository.ProyectoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de Proyectos
 * Implementa las operaciones definidas en el UML
 */
@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final FavoritoRepository favoritoRepository;
    private final PublicacionRRSSService publicacionRRSSService;

    public ProyectoService(
        ProyectoRepository proyectoRepository,
        FavoritoRepository favoritoRepository,
        PublicacionRRSSService publicacionRRSSService) {
        this.proyectoRepository = proyectoRepository;
        this.favoritoRepository = favoritoRepository;
        this.publicacionRRSSService = publicacionRRSSService;
    }

    /**
     * Crea un nuevo proyecto
     * Método del UML: crearProyecto()
     *
     * @param proyecto el proyecto a crear
     * @param usuario el usuario propietario
     * @return el proyecto creado
     */
    @Transactional
    public Proyecto crearProyecto(Proyecto proyecto, Usuario usuario) {
        proyecto.setUsuario(usuario);
        proyecto.setTotalLikes(0);
        return proyectoRepository.save(proyecto);
    }

    /**
     * Actualiza un proyecto existente
     * Método del UML: actualizarProyecto()
     * Solo el propietario puede actualizar
     *
     * @param proyectoId ID del proyecto
     * @param proyectoActualizado datos actualizados
     * @param username usuario autenticado
     * @return el proyecto actualizado
     * @throws AccessDeniedException si no es el propietario
     */
    @Transactional
    public Proyecto actualizarProyecto(Long proyectoId, Proyecto proyectoActualizado, String username) {
        Proyecto proyecto = proyectoRepository.findByIdWithUsuario(proyectoId)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        // Verificar que el usuario es el propietario (usar email)
        if (!proyecto.getUsuario().getEmail().equals(username)) {
            throw new AccessDeniedException("No tienes permisos para actualizar este proyecto");
        }

        // Actualizar campos (NO actualizar galeriaImagenes desde aquí)
        proyecto.setTitulo(proyectoActualizado.getTitulo());
        proyecto.setDescripcion(proyectoActualizado.getDescripcion());
        proyecto.setTecnologias(proyectoActualizado.getTecnologias());
        proyecto.setEnlaceWeb(proyectoActualizado.getEnlaceWeb());

        // La galería se gestiona por separado con agregarImagenes() y eliminarImagen()

        return proyectoRepository.save(proyecto);
    }

    /**
     * Elimina un proyecto
     * Método del UML: eliminarProyecto()
     * Solo el propietario o admin pueden eliminar
     *
     * @param proyectoId ID del proyecto
     * @param username usuario autenticado
     * @param isAdmin si el usuario es administrador
     * @throws AccessDeniedException si no tiene permisos
     */
    @Transactional
    public void eliminarProyecto(Long proyectoId, String username, boolean isAdmin) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        // Verificar permisos: propietario o admin
        if (!isAdmin && !proyecto.getUsuario().getUsername().equals(username)) {
            throw new AccessDeniedException("No tienes permisos para eliminar este proyecto");
        }

        // Los favoritos se eliminarán automáticamente en cascada (orphanRemoval=true)
        proyectoRepository.delete(proyecto);
    }

    /**
     * Publica el proyecto en una red social
     * Método del UML: publicarEnRRSS()
     *
     * @param proyectoId ID del proyecto
     * @param redSocial nombre de la red social
     * @param username usuario autenticado
     * @throws AccessDeniedException si no es el propietario
     */
    @Transactional
    public void publicarEnRRSS(Long proyectoId, String redSocial, String username) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        // Verificar que el usuario es el propietario
        if (!proyecto.getUsuario().getUsername().equals(username)) {
            throw new AccessDeniedException("No tienes permisos para publicar este proyecto");
        }

        // Delegar a PublicacionRRSSService
        // Este método es llamado desde el UML antiguo, se asume que NO es admin
        publicacionRRSSService.publicarEnRedSocial(proyecto, redSocial, false);
    }

    /**
     * Obtiene el ranking global de proyectos ordenados por totalLikes
     * Método del UML: obtenerRanking()
     *
     * @return lista de proyectos ordenados por likes descendente
     */
    public List<Proyecto> obtenerRanking() {
        return proyectoRepository.findAll(Sort.by(Sort.Direction.DESC, "totalLikes"));
    }

    /**
     * Agrega una imagen a la galería del proyecto
     * Método del UML: agregarImagen()
     *
     * @param proyectoId ID del proyecto
     * @param rutaImagen ruta de la imagen
     * @param username usuario autenticado
     * @return el proyecto actualizado
     * @throws AccessDeniedException si no es el propietario
     */
    @Transactional
    public Proyecto agregarImagen(Long proyectoId, String rutaImagen, String username) {
        // Recargar el proyecto y su galería de imágenes dentro de la transacción
        Proyecto proyecto = proyectoRepository.findByIdWithUsuario(proyectoId)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        // Verificar que el usuario es el propietario (usar email directamente)
        if (!proyecto.getUsuario().getEmail().equals(username)) {
            throw new AccessDeniedException("No tienes permisos para modificar este proyecto");
        }

        // Añadir directamente a la lista existente (NO crear una nueva)
        proyecto.getGaleriaImagenes().add(rutaImagen);

        return proyectoRepository.save(proyecto);
    }

    /**
     * Agrega varias imágenes a la galería del proyecto en una sola transacción
     *
     * @param proyectoId ID del proyecto
     * @param rutasImagenes lista de rutas de las imágenes
     * @param username usuario autenticado
     * @return el proyecto actualizado
     * @throws AccessDeniedException si no es el propietario
     */
    @Transactional
    public Proyecto agregarImagenes(Long proyectoId, List<String> rutasImagenes, String username) {
        Proyecto proyecto = proyectoRepository.findByIdWithUsuario(proyectoId)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        if (!proyecto.getUsuario().getEmail().equals(username)) {
            throw new AccessDeniedException("No tienes permisos para modificar este proyecto");
        }

        // Añadir directamente a la lista existente (NO crear una nueva)
        // Esto evita que JPA haga DELETE + INSERT
        proyecto.getGaleriaImagenes().addAll(rutasImagenes);

        return proyectoRepository.save(proyecto);
    }

    /**
     * Elimina una imagen de la galería del proyecto
     *
     * @param proyectoId ID del proyecto
     * @param rutaImagen ruta de la imagen a eliminar
     * @param username usuario autenticado
     * @return el proyecto actualizado
     * @throws AccessDeniedException si no es el propietario
     */
    @Transactional
    public Proyecto eliminarImagen(Long proyectoId, String rutaImagen, String username) {
        Proyecto proyecto = proyectoRepository.findByIdWithUsuario(proyectoId)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        // Verificar que el usuario es el propietario (usar email)
        if (!proyecto.getUsuario().getEmail().equals(username)) {
            throw new AccessDeniedException("No tienes permisos para modificar este proyecto");
        }

        proyecto.getGaleriaImagenes().remove(rutaImagen);
        return proyectoRepository.save(proyecto);
    }

    /**
     * Incrementa el contador de likes de un proyecto
     * Método del UML: incrementarLikes()
     * Nota: Este método es llamado automáticamente por VotoService
     *
     * @param proyecto el proyecto
     */
    @Transactional
    public void incrementarLikes(Proyecto proyecto) {
        proyecto.setTotalLikes(proyecto.getTotalLikes() + 1);
        proyectoRepository.save(proyecto);
    }

    /**
     * Decrementa el contador de likes de un proyecto
     * Método del UML: decrementarLikes()
     * Nota: Este método es llamado automáticamente por VotoService
     *
     * @param proyecto el proyecto
     */
    @Transactional
    public void decrementarLikes(Proyecto proyecto) {
        if (proyecto.getTotalLikes() > 0) {
            proyecto.setTotalLikes(proyecto.getTotalLikes() - 1);
            proyectoRepository.save(proyecto);
        }
    }

    /**
     * Lista todos los proyectos
     *
     * @return lista de proyectos
     */
    public List<Proyecto> listarProyectos() {
        return proyectoRepository.findAll();
    }

    /**
     * Busca un proyecto por ID con su usuario cargado (JOIN FETCH)
     * Evita LazyInitializationException al acceder al usuario fuera de transacción
     *
     * @param id ID del proyecto
     * @return el proyecto encontrado con usuario cargado
     * @throws IllegalArgumentException si no existe
     */
    public Proyecto buscarPorId(Long id) {
        return proyectoRepository.findByIdWithUsuario(id)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con ID: " + id));
    }

    /**
     * Obtiene un proyecto por ID (Optional)
     *
     * @param id ID del proyecto
     * @return Optional con el proyecto
     */
    public Optional<Proyecto> obtenerProyectoPorId(Long id) {
        return proyectoRepository.findById(id);
    }

    /**
     * Elimina un proyecto (solo admin)
     *
     * @param id ID del proyecto
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void eliminarProyectoAdmin(Long id) {
        // Los favoritos se eliminarán automáticamente en cascada (orphanRemoval=true)
        proyectoRepository.deleteById(id);
    }

    /**
     * Lista todos los proyectos ordenados por votos (ranking)
     *
     * @return lista de proyectos ordenados por totalLikes DESC
     */
    public List<Proyecto> listarProyectosOrdenadosPorVotos() {
        return proyectoRepository.findAll(Sort.by(Sort.Direction.DESC, "totalLikes"));
    }

    /**
     * Lista proyectos de un usuario específico
     *
     * @param usuario el usuario
     * @return lista de proyectos del usuario
     */
    public List<Proyecto> listarProyectosPorUsuario(Usuario usuario) {
        return proyectoRepository.findByUsuario(usuario);
    }
}
