package es.fempa.acd.demosecurityproductos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad CV según modelo ER v2.0
 * Representa los archivos de curriculum vitae subidos por los usuarios
 */
@Entity
@Table(name = "cvs")
public class CV {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String tipoArchivo; // PDF, DOCX, TXT

    @Column(nullable = false, length = 500)
    private String rutaServidor; // Ruta física del archivo en el servidor

    @Column(nullable = false)
    private LocalDateTime fechaSubida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Constructores
    public CV() {
        this.fechaSubida = LocalDateTime.now();
    }

    public CV(String tipoArchivo, String rutaServidor, Usuario usuario) {
        this();
        this.tipoArchivo = tipoArchivo;
        this.rutaServidor = rutaServidor;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getRutaServidor() {
        return rutaServidor;
    }

    public void setRutaServidor(String rutaServidor) {
        this.rutaServidor = rutaServidor;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @PrePersist
    protected void onCreate() {
        if (this.fechaSubida == null) {
            this.fechaSubida = LocalDateTime.now();
        }
    }
}

