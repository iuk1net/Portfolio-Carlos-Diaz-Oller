package es.fempa.acd.demosecurityproductos.model;

import es.fempa.acd.demosecurityproductos.model.enums.EstadoPublicacion;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad PublicacionRRSS según modelo ER v2.0
 * Registra las publicaciones de proyectos en redes sociales
 */
@Entity
@Table(name = "publicaciones_rrss")
public class PublicacionRRSS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @Column(nullable = false, length = 50)
    private String redSocial; // LinkedIn, Twitter, Facebook, Instagram, etc.

    @Column(nullable = false)
    private LocalDateTime fechaPublicacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPublicacion estado; // PENDIENTE, PUBLICADO, ERROR

    // ========================================
    // CAMPOS NUEVOS v3.0.0 - LinkedIn Integration
    // ========================================

    /**
     * ID externo de la publicación en la red social
     * Ejemplo LinkedIn: "urn:li:ugcPost:123456789"
     */
    @Column(name = "id_externo", length = 200)
    private String idExterno;

    /**
     * URL pública de la publicación en la red social
     * Permite acceder directamente a la publicación
     */
    @Column(name = "url_publicacion", length = 500)
    private String urlPublicacion;

    /**
     * Mensaje de error si la publicación falló
     * Se guarda para debugging y reintentos
     */
    @Column(name = "mensaje_error", length = 1000)
    private String mensajeError;

    // Constructores
    public PublicacionRRSS() {
        this.fechaPublicacion = LocalDateTime.now();
        this.estado = EstadoPublicacion.PENDIENTE;
    }

    public PublicacionRRSS(Proyecto proyecto, String redSocial) {
        this();
        this.proyecto = proyecto;
        this.redSocial = redSocial;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getRedSocial() {
        return redSocial;
    }

    public void setRedSocial(String redSocial) {
        this.redSocial = redSocial;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public EstadoPublicacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPublicacion estado) {
        this.estado = estado;
    }

    // ========================================
    // Getters y Setters - Campos nuevos v3.0.0
    // ========================================

    public String getIdExterno() {
        return idExterno;
    }

    public void setIdExterno(String idExterno) {
        this.idExterno = idExterno;
    }

    public String getUrlPublicacion() {
        return urlPublicacion;
    }

    public void setUrlPublicacion(String urlPublicacion) {
        this.urlPublicacion = urlPublicacion;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    @PrePersist
    protected void onCreate() {
        if (this.fechaPublicacion == null) {
            this.fechaPublicacion = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = EstadoPublicacion.PENDIENTE;
        }
    }
}

