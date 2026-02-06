package es.fempa.acd.demosecurityproductos.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidad Proyecto según modelo ER v2.0
 * Representa los proyectos publicados en los portfolios de los usuarios
 */
@Entity
@Table(name = "proyectos")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 500)
    private String tecnologias;

    @ElementCollection
    @CollectionTable(name = "proyecto_imagenes", joinColumns = @JoinColumn(name = "proyecto_id"))
    @Column(name = "ruta_imagen")
    private List<String> galeriaImagenes = new ArrayList<>();

    @Column(length = 500)
    private String enlaceWeb;

    @Column(nullable = false)
    private Integer totalLikes = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Relaciones
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Voto> votos = new HashSet<>();

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PublicacionRRSS> publicaciones = new HashSet<>();

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Favorito> favoritos = new HashSet<>();

    // Constructores
    public Proyecto() {
        this.totalLikes = 0;
        this.galeriaImagenes = new ArrayList<>();
    }

    public Proyecto(String titulo, String descripcion, String tecnologias, String enlaceWeb, Usuario usuario) {
        this();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tecnologias = tecnologias;
        this.enlaceWeb = enlaceWeb;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTecnologias() {
        return tecnologias;
    }

    public void setTecnologias(String tecnologias) {
        this.tecnologias = tecnologias;
    }

    public List<String> getGaleriaImagenes() {
        return galeriaImagenes;
    }

    public void setGaleriaImagenes(List<String> galeriaImagenes) {
        this.galeriaImagenes = galeriaImagenes;
    }

    public String getEnlaceWeb() {
        return enlaceWeb;
    }

    public void setEnlaceWeb(String enlaceWeb) {
        this.enlaceWeb = enlaceWeb;
    }

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<Voto> getVotos() {
        return votos;
    }

    public void setVotos(Set<Voto> votos) {
        this.votos = votos;
    }

    public Set<PublicacionRRSS> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(Set<PublicacionRRSS> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public Set<Favorito> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(Set<Favorito> favoritos) {
        this.favoritos = favoritos;
    }

    // Métodos de negocio
    public void incrementarLikes() {
        this.totalLikes++;
    }

    public void decrementarLikes() {
        if (this.totalLikes > 0) {
            this.totalLikes--;
        }
    }
}


