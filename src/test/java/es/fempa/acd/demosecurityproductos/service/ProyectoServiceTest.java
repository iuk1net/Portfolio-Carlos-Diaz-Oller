package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.enums.Estado;
import es.fempa.acd.demosecurityproductos.model.enums.Rol;
import es.fempa.acd.demosecurityproductos.repository.ProyectoRepository;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para ProyectoService
 * Valida CRUD y reglas de permisos
 */
@SpringBootTest
@Transactional
class ProyectoServiceTest {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    private Usuario usuario1;
    private Usuario usuario2;
    private Usuario admin;

    @BeforeEach
    void setUp() {
        // Limpiar datos
        proyectoRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Crear usuarios de prueba
        usuario1 = new Usuario();
        usuario1.setNombre("Usuario Test 1");
        usuario1.setEmail("test1@test.com");
        usuario1.setContraseña("$2a$12$test");
        usuario1.setRol(Rol.USER);
        usuario1.setEstado(Estado.ACTIVO);
        usuario1 = usuarioRepository.save(usuario1);

        usuario2 = new Usuario();
        usuario2.setNombre("Usuario Test 2");
        usuario2.setEmail("test2@test.com");
        usuario2.setContraseña("$2a$12$test");
        usuario2.setRol(Rol.USER);
        usuario2.setEstado(Estado.ACTIVO);
        usuario2 = usuarioRepository.save(usuario2);

        admin = new Usuario();
        admin.setNombre("Admin Test");
        admin.setEmail("admin@test.com");
        admin.setContraseña("$2a$12$test");
        admin.setRol(Rol.ADMIN);
        admin.setEstado(Estado.ACTIVO);
        admin = usuarioRepository.save(admin);
    }

    @Test
    @DisplayName("Test 1: Crear proyecto correctamente")
    void testCrearProyecto() {
        // Given
        Proyecto proyecto = new Proyecto();
        proyecto.setTitulo("Proyecto Test");
        proyecto.setDescripcion("Descripción de prueba");
        proyecto.setTecnologias("Java, Spring Boot");
        proyecto.setEnlaceWeb("https://github.com/test");

        // When
        Proyecto proyectoCreado = proyectoService.crearProyecto(proyecto, usuario1);

        // Then
        assertNotNull(proyectoCreado.getId(), "El proyecto debe tener ID");
        assertEquals("Proyecto Test", proyectoCreado.getTitulo());
        assertEquals(usuario1.getId(), proyectoCreado.getUsuario().getId());
        assertEquals(0, proyectoCreado.getTotalLikes(), "Debe empezar con 0 likes");
    }

    @Test
    @DisplayName("Test 2: Listar todos los proyectos")
    void testListarProyectos() {
        // Given: crear 3 proyectos
        crearProyecto("Proyecto 1", usuario1);
        crearProyecto("Proyecto 2", usuario1);
        crearProyecto("Proyecto 3", usuario2);

        // When
        List<Proyecto> proyectos = proyectoService.listarProyectos();

        // Then
        assertEquals(3, proyectos.size(), "Debe haber 3 proyectos");
    }

    @Test
    @DisplayName("Test 3: Buscar proyecto por ID")
    void testBuscarProyectoPorId() {
        // Given
        Proyecto proyecto = crearProyecto("Proyecto Test", usuario1);

        // When
        Proyecto proyectoEncontrado = proyectoService.buscarPorId(proyecto.getId());

        // Then
        assertNotNull(proyectoEncontrado);
        assertEquals(proyecto.getId(), proyectoEncontrado.getId());
        assertEquals("Proyecto Test", proyectoEncontrado.getTitulo());
    }

    @Test
    @DisplayName("Test 4: Actualizar proyecto por el propietario")
    void testActualizarProyectoPorPropietario() {
        // Given: usuario1 crea un proyecto
        Proyecto proyecto = crearProyecto("Proyecto Original", usuario1);
        Long proyectoId = proyecto.getId();

        // When: usuario1 actualiza su proyecto
        Proyecto proyectoActualizado = new Proyecto();
        proyectoActualizado.setTitulo("Proyecto Actualizado");
        proyectoActualizado.setDescripcion("Nueva descripción");
        proyectoActualizado.setTecnologias("React, Node.js");
        proyectoActualizado.setEnlaceWeb("https://github.com/nuevo");

        Proyecto resultado = proyectoService.actualizarProyecto(
            proyectoId,
            proyectoActualizado,
            usuario1.getEmail()
        );

        // Then
        assertEquals("Proyecto Actualizado", resultado.getTitulo());
        assertEquals("Nueva descripción", resultado.getDescripcion());
        assertEquals("React, Node.js", resultado.getTecnologias());
    }

    @Test
    @DisplayName("Test 5: No permitir actualizar proyecto de otro usuario")
    void testNoActualizarProyectoAjeno() {
        // Given: usuario1 crea un proyecto
        Proyecto proyecto = crearProyecto("Proyecto de Usuario1", usuario1);

        // When/Then: usuario2 intenta actualizar el proyecto de usuario1
        Proyecto proyectoActualizado = new Proyecto();
        proyectoActualizado.setTitulo("Intento de modificación");

        assertThrows(
            AccessDeniedException.class,
            () -> proyectoService.actualizarProyecto(
                proyecto.getId(),
                proyectoActualizado,
                usuario2.getEmail()
            ),
            "Debe lanzar excepción al intentar actualizar proyecto ajeno"
        );
    }

    @Test
    @DisplayName("Test 6: Eliminar proyecto por el propietario")
    void testEliminarProyectoPorPropietario() {
        // Given: usuario1 crea un proyecto
        Proyecto proyecto = crearProyecto("Proyecto a Eliminar", usuario1);
        Long proyectoId = proyecto.getId();

        // When: usuario1 elimina su proyecto
        proyectoService.eliminarProyecto(proyectoId, usuario1.getEmail(), false);

        // Then: el proyecto debe estar eliminado
        assertThrows(
            IllegalArgumentException.class,
            () -> proyectoService.buscarPorId(proyectoId),
            "El proyecto no debe existir"
        );
    }

    @Test
    @DisplayName("Test 7: Admin puede eliminar cualquier proyecto")
    void testAdminPuedeEliminarCualquierProyecto() {
        // Given: usuario1 crea un proyecto
        Proyecto proyecto = crearProyecto("Proyecto de Usuario1", usuario1);
        Long proyectoId = proyecto.getId();

        // When: admin elimina el proyecto de usuario1
        proyectoService.eliminarProyecto(proyectoId, admin.getEmail(), true);

        // Then: el proyecto debe estar eliminado
        assertThrows(
            IllegalArgumentException.class,
            () -> proyectoService.buscarPorId(proyectoId)
        );
    }

    @Test
    @DisplayName("Test 8: No permitir eliminar proyecto de otro usuario sin ser admin")
    void testNoEliminarProyectoAjeno() {
        // Given: usuario1 crea un proyecto
        Proyecto proyecto = crearProyecto("Proyecto de Usuario1", usuario1);

        // When/Then: usuario2 intenta eliminar el proyecto de usuario1
        assertThrows(
            AccessDeniedException.class,
            () -> proyectoService.eliminarProyecto(
                proyecto.getId(),
                usuario2.getEmail(),
                false
            ),
            "Debe lanzar excepción al intentar eliminar proyecto ajeno"
        );
    }

    @Test
    @DisplayName("Test 9: Listar proyectos de un usuario específico")
    void testListarProyectosPorUsuario() {
        // Given: crear proyectos para diferentes usuarios
        crearProyecto("Proyecto 1 Usuario1", usuario1);
        crearProyecto("Proyecto 2 Usuario1", usuario1);
        crearProyecto("Proyecto 3 Usuario2", usuario2);

        // When: listar proyectos de usuario1
        List<Proyecto> proyectosUsuario1 = proyectoService.listarProyectosPorUsuario(usuario1);

        // Then
        assertEquals(2, proyectosUsuario1.size(), "Usuario1 debe tener 2 proyectos");
        assertTrue(
            proyectosUsuario1.stream().allMatch(p -> p.getUsuario().getId().equals(usuario1.getId())),
            "Todos los proyectos deben ser de usuario1"
        );
    }

    @Test
    @DisplayName("Test 10: Listar proyectos ordenados por votos (ranking)")
    void testListarProyectosOrdenadosPorVotos() {
        // Given: crear proyectos con diferentes votos
        Proyecto proyecto1 = crearProyecto("Proyecto 1", usuario1);
        Proyecto proyecto2 = crearProyecto("Proyecto 2", usuario1);
        Proyecto proyecto3 = crearProyecto("Proyecto 3", usuario2);

        proyecto1.setTotalLikes(10);
        proyecto2.setTotalLikes(5);
        proyecto3.setTotalLikes(15);
        proyectoRepository.save(proyecto1);
        proyectoRepository.save(proyecto2);
        proyectoRepository.save(proyecto3);

        // When: listar proyectos ordenados por votos
        List<Proyecto> ranking = proyectoService.listarProyectosOrdenadosPorVotos();

        // Then: deben estar ordenados de mayor a menor
        assertEquals(3, ranking.size());
        assertEquals(15, ranking.get(0).getTotalLikes(), "El primero debe tener 15 votos");
        assertEquals(10, ranking.get(1).getTotalLikes(), "El segundo debe tener 10 votos");
        assertEquals(5, ranking.get(2).getTotalLikes(), "El tercero debe tener 5 votos");
    }

    // Método auxiliar para crear proyectos de prueba
    private Proyecto crearProyecto(String titulo, Usuario usuario) {
        Proyecto proyecto = new Proyecto();
        proyecto.setTitulo(titulo);
        proyecto.setDescripcion("Descripción de " + titulo);
        proyecto.setTecnologias("Java, Spring Boot");
        proyecto.setEnlaceWeb("https://github.com/test");
        return proyectoService.crearProyecto(proyecto, usuario);
    }
}

