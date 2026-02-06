package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.enums.Rol;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Crear un nuevo usuario
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden crear usuarios
    public Usuario crearUsuario(String nombre, String email, String password, Rol rol) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password)); // Encriptar contraseña
        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }

    // Listar todos los usuarios
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden listar usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Buscar un usuario por su email
    // Este método NO tiene restricción porque se usa para buscar el propio perfil del usuario autenticado
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden eliminar usuarios
	public void eliminarUsuario(Long id) {
		usuarioRepository.deleteById(id);
		
	}

    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    @Transactional
    public void actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setRol(usuarioActualizado.getRol());

        usuarioRepository.save(usuario);
    }

    /**
     * Bloquea un usuario
     * Método del UML: bloquearUsuario()
     * Solo los administradores pueden bloquear usuarios
     *
     * @param id ID del usuario a bloquear
     * @throws IllegalArgumentException si el usuario no existe
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void bloquearUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setEstado(es.fempa.acd.demosecurityproductos.model.enums.Estado.BLOQUEADO);
        usuarioRepository.save(usuario);
    }

    /**
     * Desbloquea un usuario (cambiar estado a ACTIVO)
     * Solo los administradores pueden desbloquear usuarios
     *
     * @param id ID del usuario a desbloquear
     * @throws IllegalArgumentException si el usuario no existe
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void desbloquearUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setEstado(es.fempa.acd.demosecurityproductos.model.enums.Estado.ACTIVO);
        usuarioRepository.save(usuario);
    }

    /**
     * Guarda un usuario (usado para actualizar emailVerificado desde VerificacionEmailService).
     *
     * @param usuario el usuario a guardar
     * @return el usuario guardado
     */
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

}
