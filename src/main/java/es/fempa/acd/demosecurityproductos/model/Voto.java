package es.fempa.acd.demosecurityproductos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Voto según modelo ER v2.0
 * Representa los votos (likes) emitidos por usuarios a proyectos
 * REGLA: Un usuario solo puede votar una vez por proyecto
 */
@Entity
@Table(
    name = "votos",
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_proyecto"})
)
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @Column(nullable = false)
    private LocalDateTime fechaVoto;

    // Constructores
    public Voto() {
        this.fechaVoto = LocalDateTime.now();
    }

    public Voto(Usuario usuario, Proyecto proyecto) {
        this();
        this.usuario = usuario;
        this.proyecto = proyecto;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public LocalDateTime getFechaVoto() {
        return fechaVoto;
    }

    public void setFechaVoto(LocalDateTime fechaVoto) {
        this.fechaVoto = fechaVoto;
    }

    @PrePersist
    protected void onCreate() {
        if (this.fechaVoto == null) {
            this.fechaVoto = LocalDateTime.now();
        }
    }
}


