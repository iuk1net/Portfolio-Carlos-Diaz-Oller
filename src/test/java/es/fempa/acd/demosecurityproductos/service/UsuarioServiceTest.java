package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.enums.Estado;
import es.fempa.acd.demosecurityproductos.model.enums.Rol;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para UsuarioService
 * Valida la creación, seguridad y gestión de usuarios
 */
@SpringBootTest
@Transactional
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Limpiar datos de test
        usuarioRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test 1: Crear usuario correctamente")
    void testCrearUsuario() {
        // Given
        String nombre = "Usuario Test";
        String email = "test@test.com";
        String password = "123456";
        Rol rol = Rol.USER;

        // When
        Usuario usuario = usuarioService.crearUsuario(nombre, email, password, rol);

        // Then
        assertNotNull(usuario.getId(), "El usuario debe tener ID");
        assertEquals(nombre, usuario.getNombre());
        assertEquals(email, usuario.getEmail());
        assertEquals(rol, usuario.getRol());
        assertEquals(Estado.ACTIVO, usuario.getEstado(), "El usuario debe estar activo por defecto");

        // Verificar que la contraseña está cifrada
        assertNotEquals(password, usuario.getContraseña(), "La contraseña debe estar cifrada");
        assertTrue(
            passwordEncoder.matches(password, usuario.getContraseña()),
            "La contraseña cifrada debe coincidir con la original"
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test 2: No permitir email duplicado")
    void testNoPermitirEmailDuplicado() {
        // Given: crear primer usuario
        usuarioService.crearUsuario("Usuario 1", "test@test.com", "123", Rol.USER);

        // When/Then: intentar crear otro usuario con el mismo email
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> usuarioService.crearUsuario("Usuario 2", "test@test.com", "456", Rol.USER),
            "Debe lanzar excepción al intentar crear usuario con email duplicado"
        );

        assertEquals("El email ya está en uso", exception.getMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test 3: Buscar usuario por email")
    void testBuscarUsuarioPorEmail() {
        // Given: crear usuario
        usuarioService.crearUsuario("Usuario Test", "test@test.com", "123", Rol.USER);

        // When: buscar por email
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail("test@test.com");

        // Then
        assertTrue(usuarioOpt.isPresent(), "El usuario debe existir");
        assertEquals("test@test.com", usuarioOpt.get().getEmail());
        assertEquals("Usuario Test", usuarioOpt.get().getNombre());
    }

    @Test
    @DisplayName("Test 4: Buscar usuario por email inexistente")
    void testBuscarUsuarioPorEmailInexistente() {
        // When: buscar usuario que no existe
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail("noexiste@test.com");

        // Then
        assertFalse(usuarioOpt.isPresent(), "No debe encontrar el usuario");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test 5: Listar todos los usuarios")
    void testListarUsuarios() {
        // Given: crear varios usuarios
        usuarioService.crearUsuario("Usuario 1", "user1@test.com", "123", Rol.USER);
        usuarioService.crearUsuario("Usuario 2", "user2@test.com", "123", Rol.USER);
        usuarioService.crearUsuario("Admin 1", "admin@test.com", "123", Rol.ADMIN);

        // When
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        // Then
        assertEquals(3, usuarios.size(), "Debe haber 3 usuarios");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test 6: Bloquear usuario")
    void testBloquearUsuario() {
        // Given: crear usuario activo
        Usuario usuario = usuarioService.crearUsuario("Usuario Test", "test@test.com", "123", Rol.USER);
        assertEquals(Estado.ACTIVO, usuario.getEstado());

        // When: bloquear usuario
        usuarioService.bloquearUsuario(usuario.getId());

        // Then: verificar que está bloqueado
        Usuario usuarioBloqueado = usuarioService.obtenerUsuarioPorId(usuario.getId());
        assertEquals(Estado.BLOQUEADO, usuarioBloqueado.getEstado());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test 7: Desbloquear usuario")
    void testDesbloquearUsuario() {
        // Given: crear usuario y bloquearlo
        Usuario usuario = usuarioService.crearUsuario("Usuario Test", "test@test.com", "123", Rol.USER);
        usuarioService.bloquearUsuario(usuario.getId());
        assertEquals(Estado.BLOQUEADO, usuarioService.obtenerUsuarioPorId(usuario.getId()).getEstado());

        // When: desbloquear usuario
        usuarioService.desbloquearUsuario(usuario.getId());

        // Then: verificar que está activo
        Usuario usuarioActivo = usuarioService.obtenerUsuarioPorId(usuario.getId());
        assertEquals(Estado.ACTIVO, usuarioActivo.getEstado());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test 8: Obtener usuario por ID")
    void testObtenerUsuarioPorId() {
        // Given: crear usuario
        Usuario usuario = usuarioService.crearUsuario("Usuario Test", "test@test.com", "123", Rol.USER);

        // When: obtener por ID
        Usuario usuarioEncontrado = usuarioService.obtenerUsuarioPorId(usuario.getId());

        // Then
        assertNotNull(usuarioEncontrado);
        assertEquals(usuario.getId(), usuarioEncontrado.getId());
        assertEquals("Usuario Test", usuarioEncontrado.getNombre());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test 9: Actualizar usuario")
    void testActualizarUsuario() {
        // Given: crear usuario
        Usuario usuario = usuarioService.crearUsuario("Usuario Original", "test@test.com", "123", Rol.USER);
        Long usuarioId = usuario.getId();

        // When: actualizar datos del usuario
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Usuario Actualizado");
        usuarioActualizado.setEmail("nuevo@test.com");
        usuarioActualizado.setRol(Rol.ADMIN);

        usuarioService.actualizarUsuario(usuarioId, usuarioActualizado);

        // Then: verificar cambios
        Usuario usuarioModificado = usuarioService.obtenerUsuarioPorId(usuarioId);
        assertEquals("Usuario Actualizado", usuarioModificado.getNombre());
        assertEquals("nuevo@test.com", usuarioModificado.getEmail());
        assertEquals(Rol.ADMIN, usuarioModificado.getRol());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test 10: Eliminar usuario")
    void testEliminarUsuario() {
        // Given: crear usuario
        Usuario usuario = usuarioService.crearUsuario("Usuario Test", "test@test.com", "123", Rol.USER);
        Long usuarioId = usuario.getId();

        // When: eliminar usuario
        usuarioService.eliminarUsuario(usuarioId);

        // Then: verificar que no existe
        assertThrows(
            IllegalArgumentException.class,
            () -> usuarioService.obtenerUsuarioPorId(usuarioId),
            "El usuario no debe existir"
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test 11: Crear usuario con rol ADMIN")
    void testCrearUsuarioAdmin() {
        // Given/When
        Usuario admin = usuarioService.crearUsuario("Admin Test", "admin@test.com", "123", Rol.ADMIN);

        // Then
        assertEquals(Rol.ADMIN, admin.getRol());
        assertEquals(Estado.ACTIVO, admin.getEstado());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Test 12: Contraseña cifrada con BCrypt")
    void testContraseñaCifradaConBCrypt() {
        // Given/When
        String passwordOriginal = "password123";
        Usuario usuario = usuarioService.crearUsuario("Usuario Test", "test@test.com", passwordOriginal, Rol.USER);

        // Then: verificar que está cifrada con BCrypt (empieza con $2a$)
        assertTrue(
            usuario.getContraseña().startsWith("$2a$") || usuario.getContraseña().startsWith("$2b$"),
            "La contraseña debe estar cifrada con BCrypt"
        );
        assertTrue(
            passwordEncoder.matches(passwordOriginal, usuario.getContraseña()),
            "La contraseña debe ser verificable"
        );
    }
}

