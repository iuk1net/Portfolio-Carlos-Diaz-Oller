package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.Voto;
import es.fempa.acd.demosecurityproductos.model.enums.Estado;
import es.fempa.acd.demosecurityproductos.model.enums.Rol;
import es.fempa.acd.demosecurityproductos.repository.ProyectoRepository;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import es.fempa.acd.demosecurityproductos.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para VotoService
 * Valida las reglas de negocio críticas del sistema de votación
 */
@SpringBootTest
@Transactional // Rollback después de cada test
class VotoServiceTest {

    @Autowired
    private VotoService votoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private VotoRepository votoRepository;

    private Usuario usuario1;
    private Usuario usuario2;
    private Proyecto proyecto1;

    @BeforeEach
    void setUp() {
        // Limpiar datos de test
        votoRepository.deleteAll();
        proyectoRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Crear usuarios de prueba
        usuario1 = new Usuario();
        usuario1.setNombre("Usuario Test 1");
        usuario1.setEmail("test1@test.com");
        usuario1.setContraseña("$2a$12$test"); // BCrypt hash
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

        // Crear proyecto de prueba
        proyecto1 = new Proyecto();
        proyecto1.setTitulo("Proyecto Test");
        proyecto1.setDescripcion("Descripción de prueba");
        proyecto1.setTecnologias("Java, Spring Boot");
        proyecto1.setUsuario(usuario1);
        proyecto1.setTotalLikes(0);
        proyecto1 = proyectoRepository.save(proyecto1);
    }

    @Test
    @DisplayName("Test 1: Votar un proyecto correctamente")
    void testVotarProyecto() {
        // Given: usuario2 quiere votar el proyecto de usuario1
        assertEquals(0, proyecto1.getTotalLikes(), "El proyecto debe empezar con 0 votos");

        // When: usuario2 vota el proyecto
        Voto voto = votoService.votar(usuario2, proyecto1);

        // Then: el voto se crea correctamente
        assertNotNull(voto, "El voto no debe ser null");
        assertNotNull(voto.getId(), "El voto debe tener ID");
        assertEquals(usuario2.getId(), voto.getUsuario().getId(), "El voto debe pertenecer a usuario2");
        assertEquals(proyecto1.getId(), voto.getProyecto().getId(), "El voto debe pertenecer al proyecto1");
        assertNotNull(voto.getFechaVoto(), "El voto debe tener fecha");

        // Verificar que el contador de likes se incrementó
        Proyecto proyectoActualizado = proyectoRepository.findById(proyecto1.getId()).orElseThrow();
        assertEquals(1, proyectoActualizado.getTotalLikes(), "El proyecto debe tener 1 voto");
    }

    @Test
    @DisplayName("Test 2: No permitir voto duplicado")
    void testNoPermitirVotoDuplicado() {
        // Given: usuario2 ya votó el proyecto
        votoService.votar(usuario2, proyecto1);

        // When/Then: intentar votar de nuevo debe lanzar excepción
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> votoService.votar(usuario2, proyecto1),
            "Debe lanzar excepción al intentar votar dos veces"
        );

        assertEquals("Ya has votado por este proyecto", exception.getMessage());

        // Verificar que el contador no se incrementó de nuevo
        Proyecto proyectoActualizado = proyectoRepository.findById(proyecto1.getId()).orElseThrow();
        assertEquals(1, proyectoActualizado.getTotalLikes(), "El proyecto debe tener solo 1 voto");
    }

    @Test
    @DisplayName("Test 3: No permitir votar el propio proyecto")
    void testNoVotarProyectoPropio() {
        // Given: usuario1 es el dueño del proyecto1
        assertEquals(usuario1.getId(), proyecto1.getUsuario().getId());

        // When/Then: intentar votar su propio proyecto debe lanzar excepción
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> votoService.votar(usuario1, proyecto1),
            "Debe lanzar excepción al intentar votar el propio proyecto"
        );

        assertEquals("No puedes votar tu propio proyecto", exception.getMessage());

        // Verificar que no se creó ningún voto
        assertEquals(0, votoRepository.count(), "No debe haber ningún voto");
        assertEquals(0, proyecto1.getTotalLikes(), "El proyecto debe tener 0 votos");
    }

    @Test
    @DisplayName("Test 4: Quitar voto correctamente")
    void testQuitarVoto() {
        // Given: usuario2 ha votado el proyecto
        votoService.votar(usuario2, proyecto1);
        Proyecto proyectoConVoto = proyectoRepository.findById(proyecto1.getId()).orElseThrow();
        assertEquals(1, proyectoConVoto.getTotalLikes(), "El proyecto debe tener 1 voto");

        // When: usuario2 quita su voto
        votoService.quitarVoto(usuario2, proyectoConVoto);

        // Then: el voto se elimina correctamente
        assertEquals(0, votoRepository.count(), "No debe haber ningún voto");

        // Verificar que el contador de likes se decrementó
        Proyecto proyectoActualizado = proyectoRepository.findById(proyecto1.getId()).orElseThrow();
        assertEquals(0, proyectoActualizado.getTotalLikes(), "El proyecto debe tener 0 votos");
    }

    @Test
    @DisplayName("Test 5: No permitir quitar voto que no existe")
    void testQuitarVotoInexistente() {
        // Given: usuario2 NO ha votado el proyecto
        assertEquals(0, votoRepository.count(), "No debe haber votos");

        // When/Then: intentar quitar un voto que no existe debe lanzar excepción
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> votoService.quitarVoto(usuario2, proyecto1),
            "Debe lanzar excepción al intentar quitar un voto inexistente"
        );

        assertEquals("No has votado por este proyecto", exception.getMessage());
    }

    @Test
    @DisplayName("Test 6: Toggle voto - añadir voto")
    void testToggleVotoAñadir() {
        // Given: usuario2 NO ha votado el proyecto
        assertFalse(votoService.usuarioYaVoto(usuario2, proyecto1));

        // When: hacer toggle (añadir voto)
        boolean resultado = votoService.toggleVoto(usuario2, proyecto1);

        // Then: debe retornar true y crear el voto
        assertTrue(resultado, "Debe retornar true cuando se añade el voto");
        assertTrue(votoService.usuarioYaVoto(usuario2, proyecto1), "El usuario debe haber votado");
        assertEquals(1, proyecto1.getTotalLikes() + votoRepository.count() - 1, "El proyecto debe tener 1 voto");
    }

    @Test
    @DisplayName("Test 7: Toggle voto - quitar voto")
    void testToggleVotoQuitar() {
        // Given: usuario2 YA ha votado el proyecto
        votoService.votar(usuario2, proyecto1);
        assertTrue(votoService.usuarioYaVoto(usuario2, proyecto1));

        // When: hacer toggle (quitar voto)
        boolean resultado = votoService.toggleVoto(usuario2, proyecto1);

        // Then: debe retornar false y eliminar el voto
        assertFalse(resultado, "Debe retornar false cuando se quita el voto");
        assertFalse(votoService.usuarioYaVoto(usuario2, proyecto1), "El usuario no debe haber votado");
    }

    @Test
    @DisplayName("Test 8: Verificar que usuario ya votó")
    void testUsuarioYaVoto() {
        // Given: usuario2 NO ha votado
        assertFalse(votoService.usuarioYaVoto(usuario2, proyecto1));

        // When: usuario2 vota
        votoService.votar(usuario2, proyecto1);

        // Then: verificar que sí ha votado
        assertTrue(votoService.usuarioYaVoto(usuario2, proyecto1));
    }

    @Test
    @DisplayName("Test 9: Obtener votos de un usuario")
    void testObtenerVotosPorUsuario() {
        // Given: crear otro proyecto
        Proyecto proyecto2 = new Proyecto();
        proyecto2.setTitulo("Proyecto Test 2");
        proyecto2.setDescripcion("Otra descripción");
        proyecto2.setUsuario(usuario1);
        proyecto2.setTotalLikes(0);
        proyecto2 = proyectoRepository.save(proyecto2);

        // When: usuario2 vota ambos proyectos
        votoService.votar(usuario2, proyecto1);
        votoService.votar(usuario2, proyecto2);

        // Then: obtener votos del usuario
        var votos = votoService.obtenerVotosPorUsuario(usuario2);
        assertEquals(2, votos.size(), "El usuario debe tener 2 votos");
    }

    @Test
    @DisplayName("Test 10: Múltiples usuarios votan un proyecto")
    void testMultiplesUsuariosVotanProyecto() {
        // Given: crear usuario3
        Usuario usuario3 = new Usuario();
        usuario3.setNombre("Usuario Test 3");
        usuario3.setEmail("test3@test.com");
        usuario3.setContraseña("$2a$12$test");
        usuario3.setRol(Rol.USER);
        usuario3.setEstado(Estado.ACTIVO);
        usuario3 = usuarioRepository.save(usuario3);

        // When: usuario2 y usuario3 votan el proyecto
        votoService.votar(usuario2, proyecto1);
        votoService.votar(usuario3, proyecto1);

        // Then: el proyecto debe tener 2 votos
        Proyecto proyectoActualizado = proyectoRepository.findById(proyecto1.getId()).orElseThrow();
        assertEquals(2, proyectoActualizado.getTotalLikes(), "El proyecto debe tener 2 votos");
    }
}

