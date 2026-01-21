package es.fempa.acd.demosecurityproductos.model;

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

