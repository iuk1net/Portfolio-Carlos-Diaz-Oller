package es.fempa.acd.demosecurityproductos.model;

import es.fempa.acd.demosecurityproductos.model.enums.TipoVerificacion;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad VerificacionEmail según modelo ER v2.6.0
 * Gestiona los tokens de verificación de email para registro y recuperación de contraseña.
 *
 * @author Carlos Díaz Oller
 * @version 2.6.0
 * @since 2026-02-06
 */
@Entity
@Table(name = "verificaciones_email")
public class VerificacionEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false, unique = true, length = 100)
    private String token;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    private boolean usado = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoVerificacion tipo;

    // Constructores
    public VerificacionEmail() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaExpiracion = LocalDateTime.now().plusHours(24);
        this.token = UUID.randomUUID().toString();
    }

    public VerificacionEmail(Usuario usuario, TipoVerificacion tipo) {
        this();
        this.usuario = usuario;
        this.tipo = tipo;
    }

    // Métodos de negocio

    /**
     * Verifica si el token ha expirado.
     *
     * @return true si el token ha expirado, false en caso contrario
     */
    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(fechaExpiracion);
    }

    /**
     * Verifica si el token es válido (no usado y no expirado).
     *
     * @return true si el token es válido, false en caso contrario
     */
    public boolean isValido() {
        return !usado && !isExpirado();
    }

    /**
     * Marca el token como usado.
     */
    public void marcarComoUsado() {
        this.usado = true;
    }

    /**
     * Regenera el token y extiende la fecha de expiración.
     */
    public void regenerarToken() {
        this.token = UUID.randomUUID().toString();
        this.fechaCreacion = LocalDateTime.now();
        this.fechaExpiracion = LocalDateTime.now().plusHours(24);
        this.usado = false;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    public TipoVerificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoVerificacion tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "VerificacionEmail{" +
                "id=" + id +
                ", usuario=" + (usuario != null ? usuario.getEmail() : "null") +
                ", tipo=" + tipo +
                ", usado=" + usado +
                ", expirado=" + isExpirado() +
                '}';
    }
}

