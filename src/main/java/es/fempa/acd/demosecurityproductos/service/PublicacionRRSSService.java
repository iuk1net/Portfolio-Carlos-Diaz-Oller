package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.PublicacionRRSS;
import es.fempa.acd.demosecurityproductos.model.enums.EstadoPublicacion;
import es.fempa.acd.demosecurityproductos.repository.PublicacionRRSSRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio para gestión de publicaciones en redes sociales
 * Implementa las operaciones para compartir proyectos en RRSS
 *
 * @version 3.0.0 - LinkedIn Integration
 * COMPORTAMIENTO:
 * - ADMIN → Publica automáticamente en LinkedIn con API
 * - USUARIOS → Solo registra y genera URL para compartir manualmente
 */
@Service
public class PublicacionRRSSService {

    private final PublicacionRRSSRepository publicacionRRSSRepository;
    private final LinkedInService linkedInService;

    // Redes sociales soportadas
    private static final List<String> REDES_SOCIALES_VALIDAS =
        List.of("LinkedIn", "Twitter", "Facebook", "Instagram", "GitHub");

    public PublicacionRRSSService(PublicacionRRSSRepository publicacionRRSSRepository,
                                 LinkedInService linkedInService) {
        this.publicacionRRSSRepository = publicacionRRSSRepository;
        this.linkedInService = linkedInService;
    }

    /**
     * Publica un proyecto en una red social
     * Método relacionado con el UML: publicarEnRRSS() de Proyecto
     *
     * COMPORTAMIENTO PROFESIONAL v3.3:
     * - LinkedIn → Compartir manual (permite seleccionar página de empresa)
     * - Otras redes → Genera URL para compartir manualmente
     *
     * NOTA: La API de LinkedIn (ugcPosts) requiere permisos especiales que no están disponibles.
     * Por eso usamos el diálogo de compartir que permite publicar en página de empresa.
     *
     * @param proyecto el proyecto a publicar
     * @param redSocial nombre de la red social
     * @param esAdmin true si el usuario es administrador (ignorado)
     * @return la publicación creada
     * @throws IllegalArgumentException si la red social no es válida
     */
    @Transactional
    public PublicacionRRSS publicarEnRedSocial(Proyecto proyecto, String redSocial, boolean esAdmin) {
        // Validar red social
        if (!REDES_SOCIALES_VALIDAS.contains(redSocial)) {
            throw new IllegalArgumentException(
                "Red social no válida. Válidas: " + String.join(", ", REDES_SOCIALES_VALIDAS)
            );
        }

        // Crear la publicación con estado PENDIENTE
        PublicacionRRSS publicacion = new PublicacionRRSS(proyecto, redSocial);
        publicacion.setEstado(EstadoPublicacion.PENDIENTE);
        publicacion.setFechaPublicacion(LocalDateTime.now());

        // Guardar la publicación
        publicacion = publicacionRRSSRepository.save(publicacion);

        // TODAS LAS REDES → Compartir manual (incluyendo LinkedIn)
        // LinkedIn usa compartir manual porque la API requiere permisos no disponibles
        if ("LinkedIn".equals(redSocial)) {
            registrarPublicacionManualLinkedIn(publicacion, proyecto);
        } else {
            registrarPublicacionManual(publicacion, proyecto, redSocial);
        }

        return publicacion;
    }

    /**
     * Publica automáticamente en LinkedIn usando la API (solo ADMIN)
     */
    private void publicarEnLinkedInAutomatico(PublicacionRRSS publicacion, Proyecto proyecto) {
        try {
            // Llamar al servicio de LinkedIn
            Map<String, String> resultado = linkedInService.publicarPost(
                proyecto.getTitulo(),
                proyecto.getDescripcion(),
                proyecto.getId()
            );

            // Actualizar publicación con datos de LinkedIn
            publicacion.setIdExterno(resultado.get("id"));
            publicacion.setUrlPublicacion(resultado.get("url"));
            publicacion.setEstado(EstadoPublicacion.PUBLICADO);

        } catch (Exception e) {
            // Error en publicación
            publicacion.setEstado(EstadoPublicacion.ERROR);

            // Limitar mensaje de error a 950 caracteres (BD límite 1000)
            String mensajeError = e.getMessage();
            if (mensajeError != null && mensajeError.length() > 950) {
                mensajeError = mensajeError.substring(0, 947) + "...";
            }
            publicacion.setMensajeError(mensajeError);
        }

        publicacionRRSSRepository.save(publicacion);
    }

    /**
     * Registra una publicación manual para LinkedIn
     * Genera URL para compartir en LinkedIn (perfil o página de empresa)
     * El usuario puede seleccionar "Publicar como" en el diálogo de LinkedIn
     */
    private void registrarPublicacionManualLinkedIn(PublicacionRRSS publicacion, Proyecto proyecto) {
        // Generar URL de compartir para LinkedIn
        String urlCompartir = generarUrlCompartir(proyecto, "LinkedIn");

        // Marcar como PENDIENTE (el usuario completará la publicación manualmente)
        publicacion.setEstado(EstadoPublicacion.PENDIENTE);
        publicacion.setUrlPublicacion(urlCompartir);
        // No establecer mensaje de error - es compartir manual intencional

        publicacionRRSSRepository.save(publicacion);
    }

    /**
     * Registra una publicación manual (usuarios normales)
     * Genera URL para que el usuario comparta manualmente
     */
    private void registrarPublicacionManual(PublicacionRRSS publicacion, Proyecto proyecto, String redSocial) {
        // Generar URL de compartir según la red social
        String urlCompartir = generarUrlCompartir(proyecto, redSocial);

        // Marcar como PENDIENTE (usuario debe hacer la publicación manual)
        publicacion.setEstado(EstadoPublicacion.PENDIENTE);
        publicacion.setUrlPublicacion(urlCompartir);

        publicacionRRSSRepository.save(publicacion);
    }

    /**
     * Genera URL de compartir para cada red social
     * El usuario abrirá esta URL y publicará manualmente
     */
    private String generarUrlCompartir(Proyecto proyecto, String redSocial) {
        String urlProyecto = "http://localhost:8089/proyectos/" + proyecto.getId();
        String titulo = proyecto.getTitulo();
        String descripcion = proyecto.getDescripcion() != null ? proyecto.getDescripcion() : titulo;

        // Limitar descripción a 200 caracteres
        if (descripcion.length() > 200) {
            descripcion = descripcion.substring(0, 197) + "...";
        }

        return switch (redSocial) {
            case "LinkedIn" -> {
                // URL para compartir en LinkedIn (abre ventana de compartir)
                try {
                    yield "https://www.linkedin.com/sharing/share-offsite/?url=" +
                            java.net.URLEncoder.encode(urlProyecto, "UTF-8");
                } catch (Exception e) {
                    yield "https://www.linkedin.com/sharing/share-offsite/?url=" + urlProyecto;
                }
            }
            case "Twitter" -> {
                // URL para compartir en Twitter (ahora X)
                try {
                    String textoTwitter = "🚀 " + titulo + "\n\n" + urlProyecto;
                    // Limitar a 280 caracteres
                    if (textoTwitter.length() > 280) {
                        textoTwitter = "🚀 " + titulo.substring(0, 270) + "...\n\n" + urlProyecto;
                    }
                    yield "https://twitter.com/intent/tweet?text=" +
                            java.net.URLEncoder.encode(textoTwitter, "UTF-8");
                } catch (Exception e) {
                    yield "https://twitter.com/intent/tweet?text=" + urlProyecto;
                }
            }
            case "Facebook" -> {
                // URL para compartir en Facebook
                try {
                    yield "https://www.facebook.com/sharer/sharer.php?u=" +
                            java.net.URLEncoder.encode(urlProyecto, "UTF-8");
                } catch (Exception e) {
                    yield "https://www.facebook.com/sharer/sharer.php?u=" + urlProyecto;
                }
            }
            case "Instagram" -> {
                // Instagram no tiene compartir directo desde web
                // Redirigir al proyecto para que copien el link
                yield urlProyecto + "?share=instagram";
            }
            case "GitHub" -> {
                // GitHub no tiene compartir directo
                // Redirigir al proyecto para que copien el link
                yield urlProyecto + "?share=github";
            }
            default -> urlProyecto;
        };
    }

    /**
     * Obtiene todas las publicaciones de un proyecto
     *
     * @param proyecto el proyecto
     * @return lista de publicaciones
     */
    public List<PublicacionRRSS> obtenerPublicacionesPorProyecto(Proyecto proyecto) {
        return publicacionRRSSRepository.findByProyecto(proyecto);
    }

    /**
     * Obtiene publicaciones de un proyecto en una red social específica
     *
     * @param proyecto el proyecto
     * @param redSocial nombre de la red social
     * @return lista de publicaciones
     */
    public List<PublicacionRRSS> obtenerPublicacionesPorProyectoYRedSocial(
        Proyecto proyecto, String redSocial) {
        return publicacionRRSSRepository.findByProyectoAndRedSocial(proyecto, redSocial);
    }

    /**
     * Actualiza el estado de una publicación
     *
     * @param publicacionId ID de la publicación
     * @param nuevoEstado nuevo estado
     * @return la publicación actualizada
     */
    @Transactional
    public PublicacionRRSS actualizarEstadoPublicacion(
        Long publicacionId, EstadoPublicacion nuevoEstado) {

        PublicacionRRSS publicacion = publicacionRRSSRepository.findById(publicacionId)
            .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada"));

        publicacion.setEstado(nuevoEstado);
        return publicacionRRSSRepository.save(publicacion);
    }

    /**
     * Reintenta una publicación fallida
     *
     * @param publicacionId ID de la publicación
     * @return la publicación actualizada
     */
    @Transactional
    public PublicacionRRSS reintentarPublicacion(Long publicacionId) {
        PublicacionRRSS publicacion = publicacionRRSSRepository.findById(publicacionId)
            .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada"));

        // Solo reintentar si está en estado ERROR
        if (!publicacion.getEstado().name().equals("ERROR")) {
            throw new IllegalStateException("Solo se pueden reintentar publicaciones en estado ERROR");
        }

        // Reiniciar estado
        publicacion.setEstado(EstadoPublicacion.PENDIENTE);
        publicacion.setFechaPublicacion(LocalDateTime.now());
        publicacion = publicacionRRSSRepository.save(publicacion);

        // Reintentar publicación (simulación por ahora)
        publicacion.setEstado(EstadoPublicacion.PUBLICADO);
        return publicacionRRSSRepository.save(publicacion);
    }

    /**
     * Obtiene todas las publicaciones pendientes
     * Útil para procesamiento batch/asíncrono
     *
     * @return lista de publicaciones pendientes
     */
    public List<PublicacionRRSS> obtenerPublicacionesPendientes() {
        return publicacionRRSSRepository.findByEstadoOrderByFechaPublicacionAsc(
            EstadoPublicacion.PENDIENTE
        );
    }

    /**
     * Obtiene todas las publicaciones con errores
     *
     * @return lista de publicaciones con error
     */
    public List<PublicacionRRSS> obtenerPublicacionesConError() {
        return publicacionRRSSRepository.findByEstado(EstadoPublicacion.ERROR);
    }

    /**
     * Verifica si un proyecto ya fue publicado en una red social
     *
     * @param proyecto el proyecto
     * @param redSocial nombre de la red social
     * @return true si ya existe una publicación
     */
    public boolean yaPublicadoEn(Proyecto proyecto, String redSocial) {
        return publicacionRRSSRepository.existsByProyectoAndRedSocial(proyecto, redSocial);
    }

    /**
     * Cuenta cuántas publicaciones tiene un proyecto
     *
     * @param proyecto el proyecto
     * @return número de publicaciones
     */
    public long contarPublicaciones(Proyecto proyecto) {
        return publicacionRRSSRepository.countByProyecto(proyecto);
    }

    /**
     * Obtiene la última publicación de un proyecto en una red social
     *
     * @param proyecto el proyecto
     * @param redSocial nombre de la red social
     * @return Optional con la publicación más reciente
     */
    public Optional<PublicacionRRSS> obtenerUltimaPublicacion(
        Proyecto proyecto, String redSocial) {
        return publicacionRRSSRepository.findFirstByProyectoAndRedSocialOrderByFechaPublicacionDesc(
            proyecto, redSocial
        );
    }

    /**
     * Elimina una publicación
     *
     * @param publicacionId ID de la publicación
     */
    @Transactional
    public void eliminarPublicacion(Long publicacionId) {
        publicacionRRSSRepository.deleteById(publicacionId);
    }

    /**
     * Obtiene la lista de redes sociales soportadas
     *
     * @return lista de redes sociales
     */
    public List<String> obtenerRedesSocialesSoportadas() {
        return REDES_SOCIALES_VALIDAS;
    }
}

