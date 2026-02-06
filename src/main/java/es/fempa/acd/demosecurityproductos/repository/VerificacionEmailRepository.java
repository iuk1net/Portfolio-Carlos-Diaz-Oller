package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.VerificacionEmail;
import es.fempa.acd.demosecurityproductos.model.enums.TipoVerificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad VerificacionEmail.
 * Gestiona el acceso a datos de tokens de verificación de email.
 *
 * @author Carlos Díaz Oller
 * @version 2.6.0
 * @since 2026-02-06
 */
@Repository
public interface VerificacionEmailRepository extends JpaRepository<VerificacionEmail, Long> {

    /**
     * Busca una verificación por token.
     *
     * @param token el token a buscar
     * @return Optional con la verificación si existe
     */
    Optional<VerificacionEmail> findByToken(String token);

    /**
     * Busca verificaciones por usuario.
     *
     * @param usuario el usuario
     * @return lista de verificaciones del usuario
     */
    List<VerificacionEmail> findByUsuario(Usuario usuario);

    /**
     * Busca la verificación activa de un usuario (no usada y no expirada).
     *
     * @param usuario el usuario
     * @param tipo el tipo de verificación
     * @return Optional con la verificación activa si existe
     */
    @Query("SELECT v FROM VerificacionEmail v WHERE v.usuario = :usuario " +
           "AND v.tipo = :tipo AND v.usado = false AND v.fechaExpiracion > :ahora")
    Optional<VerificacionEmail> findVerificacionActiva(
            @Param("usuario") Usuario usuario,
            @Param("tipo") TipoVerificacion tipo,
            @Param("ahora") LocalDateTime ahora
    );

    /**
     * Busca verificaciones por usuario y tipo.
     *
     * @param usuario el usuario
     * @param tipo el tipo de verificación
     * @return lista de verificaciones
     */
    List<VerificacionEmail> findByUsuarioAndTipo(Usuario usuario, TipoVerificacion tipo);

    /**
     * Elimina verificaciones expiradas.
     *
     * @param fecha fecha de corte (eliminar anteriores a esta fecha)
     */
    @Query("DELETE FROM VerificacionEmail v WHERE v.fechaExpiracion < :fecha")
    void deleteVerificacionesExpiradas(@Param("fecha") LocalDateTime fecha);

    /**
     * Cuenta verificaciones pendientes de un usuario.
     *
     * @param usuario el usuario
     * @return cantidad de verificaciones pendientes
     */
    @Query("SELECT COUNT(v) FROM VerificacionEmail v WHERE v.usuario = :usuario " +
           "AND v.usado = false AND v.fechaExpiracion > :ahora")
    long countVerificacionesPendientes(@Param("usuario") Usuario usuario, @Param("ahora") LocalDateTime ahora);

    /**
     * Verifica si existe un token.
     *
     * @param token el token
     * @return true si existe, false en caso contrario
     */
    boolean existsByToken(String token);
}

