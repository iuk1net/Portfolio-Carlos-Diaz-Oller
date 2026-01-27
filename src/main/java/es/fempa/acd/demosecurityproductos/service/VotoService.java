package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.Voto;
import es.fempa.acd.demosecurityproductos.repository.ProyectoRepository;
import es.fempa.acd.demosecurityproductos.repository.VotoRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestión de votos (likes)
 * Implementa las operaciones definidas en el UML
 * REGLA DE NEGOCIO: Un usuario solo puede votar una vez por proyecto
 */
@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final ProyectoRepository proyectoRepository;

    public VotoService(VotoRepository votoRepository, ProyectoRepository proyectoRepository) {
        this.votoRepository = votoRepository;
        this.proyectoRepository = proyectoRepository;
    }

    /**
     * Registra un voto de un usuario a un proyecto
     * Método definido en UML: votar()
     *
     * @param usuario el usuario que vota
     * @param proyecto el proyecto a votar
     * @return el voto creado
     * @throws IllegalArgumentException si el usuario ya votó por este proyecto
     */
    @Transactional
    public Voto votar(Usuario usuario, Proyecto proyecto) {
        // Validar que no exista ya un voto
        if (votoRepository.existsByUsuarioAndProyecto(usuario, proyecto)) {
            throw new IllegalArgumentException("Ya has votado por este proyecto");
        }

        // Crear el voto
        Voto voto = new Voto(usuario, proyecto);
        voto = votoRepository.save(voto);

        // Incrementar el contador de likes del proyecto
        proyecto.setTotalLikes(proyecto.getTotalLikes() + 1);
        proyectoRepository.save(proyecto);

        return voto;
    }

    /**
     * Elimina el voto de un usuario a un proyecto
     * Método complementario a votar()
     *
     * @param usuario el usuario que quita su voto
     * @param proyecto el proyecto
     * @throws IllegalArgumentException si no existe el voto
     */
    @Transactional
    public void quitarVoto(Usuario usuario, Proyecto proyecto) {
        // Buscar el voto
        Voto voto = votoRepository.findByUsuarioAndProyecto(usuario, proyecto)
            .orElseThrow(() -> new IllegalArgumentException("No has votado por este proyecto"));

        // Eliminar el voto
        votoRepository.delete(voto);

        // Decrementar el contador de likes del proyecto
        if (proyecto.getTotalLikes() > 0) {
            proyecto.setTotalLikes(proyecto.getTotalLikes() - 1);
            proyectoRepository.save(proyecto);
        }
    }

    /**
     * Alterna el voto: si existe lo quita, si no existe lo crea
     *
     * @param usuario el usuario
     * @param proyecto el proyecto
     * @return true si se añadió el voto, false si se quitó
     */
    @Transactional
    public boolean toggleVoto(Usuario usuario, Proyecto proyecto) {
        if (votoRepository.existsByUsuarioAndProyecto(usuario, proyecto)) {
            quitarVoto(usuario, proyecto);
            return false;
        } else {
            votar(usuario, proyecto);
            return true;
        }
    }

    /**
     * Verifica si un usuario ya votó por un proyecto
     *
     * @param usuario el usuario
     * @param proyecto el proyecto
     * @return true si ya votó
     */
    public boolean verificarVoto(Usuario usuario, Proyecto proyecto) {
        return votoRepository.existsByUsuarioAndProyecto(usuario, proyecto);
    }

    /**
     * Obtiene todos los votos de un proyecto
     *
     * @param proyecto el proyecto
     * @return lista de votos
     */
    public List<Voto> obtenerVotosPorProyecto(Proyecto proyecto) {
        return votoRepository.findByProyecto(proyecto);
    }

    /**
     * Obtiene todos los votos emitidos por un usuario
     *
     * @param usuario el usuario
     * @return lista de votos del usuario
     */
    public List<Voto> obtenerVotosPorUsuario(Usuario usuario) {
        return votoRepository.findByUsuario(usuario);
    }

    /**
     * Cuenta los votos de un proyecto
     *
     * @param proyecto el proyecto
     * @return número de votos
     */
    public long contarVotos(Proyecto proyecto) {
        return votoRepository.countByProyecto(proyecto);
    }

    /**
     * Elimina un voto por su ID (solo admin o propietario)
     *
     * @param votoId ID del voto
     * @param username usuario autenticado
     * @throws AccessDeniedException si no tiene permisos
     */
    @Transactional
    public void eliminarVotoPorId(Long votoId, String username) {
        Voto voto = votoRepository.findById(votoId)
            .orElseThrow(() -> new IllegalArgumentException("Voto no encontrado"));

        // Verificar que el voto pertenece al usuario autenticado
        if (!voto.getUsuario().getUsername().equals(username)) {
            throw new AccessDeniedException("No tienes permisos para eliminar este voto");
        }

        Proyecto proyecto = voto.getProyecto();
        votoRepository.delete(voto);

        // Decrementar totalLikes
        if (proyecto.getTotalLikes() > 0) {
            proyecto.setTotalLikes(proyecto.getTotalLikes() - 1);
            proyectoRepository.save(proyecto);
        }
    }

    /**
     * Verifica si un usuario ya votó por un proyecto (alias de verificarVoto)
     *
     * @param usuario el usuario
     * @param proyecto el proyecto
     * @return true si ya votó
     */
    public boolean usuarioYaVoto(Usuario usuario, Proyecto proyecto) {
        return votoRepository.existsByUsuarioAndProyecto(usuario, proyecto);
    }
}
