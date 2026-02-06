package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Favorito;
import es.fempa.acd.demosecurityproductos.model.Proyecto;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.enums.Estado;
import es.fempa.acd.demosecurityproductos.model.enums.Rol;
import es.fempa.acd.demosecurityproductos.repository.FavoritoRepository;
import es.fempa.acd.demosecurityproductos.repository.ProyectoRepository;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para FavoritoService
 * Valida el sistema de favoritos independiente de votación
 */
@SpringBootTest
@Transactional
class FavoritoServiceTest {

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private FavoritoRepository favoritoRepository;

    private Usuario usuario1;
    private Usuario usuario2;
    private Proyecto proyecto1;
    private Proyecto proyecto2;

    @BeforeEach
    void setUp() {
        // Limpiar datos
        favoritoRepository.deleteAll();
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

        // Crear proyectos de prueba
        proyecto1 = new Proyecto();
        proyecto1.setTitulo("Proyecto Test 1");
        proyecto1.setDescripcion("Descripción 1");
        proyecto1.setTecnologias("Java");
        proyecto1.setUsuario(usuario1);
        proyecto1.setTotalLikes(0);
        proyecto1 = proyectoRepository.save(proyecto1);

        proyecto2 = new Proyecto();
        proyecto2.setTitulo("Proyecto Test 2");
        proyecto2.setDescripcion("Descripción 2");
        proyecto2.setTecnologias("Python");
        proyecto2.setUsuario(usuario1);
        proyecto2.setTotalLikes(0);
        proyecto2 = proyectoRepository.save(proyecto2);
    }

    @Test
    @DisplayName("Test 1: Agregar favorito correctamente")
    void testAgregarFavorito() {
        // Given: usuario2 quiere marcar proyecto1 como favorito
        assertFalse(favoritoService.esFavorito(usuario2, proyecto1));

        // When: agregar a favoritos
        Favorito favorito = favoritoService.agregarFavorito(usuario2, proyecto1);

        // Then: el favorito se crea correctamente
        assertNotNull(favorito.getId(), "El favorito debe tener ID");
        assertEquals(usuario2.getId(), favorito.getUsuario().getId());
        assertEquals(proyecto1.getId(), favorito.getProyecto().getId());
        assertTrue(favoritoService.esFavorito(usuario2, proyecto1));
    }

    @Test
    @DisplayName("Test 2: Eliminar favorito correctamente")
    void testEliminarFavorito() {
        // Given: usuario2 tiene proyecto1 en favoritos
        favoritoService.agregarFavorito(usuario2, proyecto1);
        assertTrue(favoritoService.esFavorito(usuario2, proyecto1));

        // When: eliminar de favoritos
        favoritoService.eliminarFavorito(usuario2, proyecto1);

        // Then: el favorito se elimina
        assertFalse(favoritoService.esFavorito(usuario2, proyecto1));
        assertEquals(0, favoritoRepository.count(), "No debe haber favoritos");
    }

    @Test
    @DisplayName("Test 3: Agregar y eliminar favorito (toggle manual)")
    void testAgregarYEliminarFavorito() {
        // Given: proyecto1 NO está en favoritos de usuario2
        assertFalse(favoritoService.esFavorito(usuario2, proyecto1));

        // When: agregar a favoritos
        favoritoService.agregarFavorito(usuario2, proyecto1);

        // Then: debe estar en favoritos
        assertTrue(favoritoService.esFavorito(usuario2, proyecto1));

        // When: eliminar de favoritos
        favoritoService.eliminarFavorito(usuario2, proyecto1);

        // Then: no debe estar en favoritos
        assertFalse(favoritoService.esFavorito(usuario2, proyecto1));
    }

    @Test
    @DisplayName("Test 4: Verificar si es favorito")
    void testEsFavorito() {
        // Given: proyecto1 NO es favorito
        assertFalse(favoritoService.esFavorito(usuario2, proyecto1));

        // When: agregar a favoritos
        favoritoService.agregarFavorito(usuario2, proyecto1);

        // Then: debe ser favorito
        assertTrue(favoritoService.esFavorito(usuario2, proyecto1));
    }

    @Test
    @DisplayName("Test 5: Listar favoritos de un usuario")
    void testListarFavoritosPorUsuario() {
        // Given: usuario2 agrega 2 proyectos a favoritos
        favoritoService.agregarFavorito(usuario2, proyecto1);
        favoritoService.agregarFavorito(usuario2, proyecto2);

        // When: listar favoritos de usuario2
        List<Favorito> favoritos = favoritoService.listarFavoritosPorUsuario(usuario2);

        // Then: debe tener 2 favoritos
        assertEquals(2, favoritos.size(), "Usuario2 debe tener 2 favoritos");
        assertTrue(
            favoritos.stream().anyMatch(f -> f.getProyecto().getId().equals(proyecto1.getId())),
            "Debe incluir proyecto1"
        );
        assertTrue(
            favoritos.stream().anyMatch(f -> f.getProyecto().getId().equals(proyecto2.getId())),
            "Debe incluir proyecto2"
        );
    }

    @Test
    @DisplayName("Test 6: Usuario sin favoritos")
    void testUsuarioSinFavoritos() {
        // Given: usuario2 no tiene favoritos
        // When: listar favoritos
        List<Favorito> favoritos = favoritoService.listarFavoritosPorUsuario(usuario2);

        // Then: lista vacía
        assertTrue(favoritos.isEmpty(), "La lista debe estar vacía");
    }

    @Test
    @DisplayName("Test 7: Múltiples usuarios marcan el mismo proyecto")
    void testMultiplesUsuariosMismoProyecto() {
        // Given/When: usuario1 y usuario2 marcan proyecto1 como favorito
        favoritoService.agregarFavorito(usuario1, proyecto1);
        favoritoService.agregarFavorito(usuario2, proyecto1);

        // Then: ambos deben tener proyecto1 en favoritos
        assertTrue(favoritoService.esFavorito(usuario1, proyecto1));
        assertTrue(favoritoService.esFavorito(usuario2, proyecto1));
        assertEquals(2, favoritoRepository.count(), "Debe haber 2 favoritos");
    }

    @Test
    @DisplayName("Test 8: Favoritos independientes de votación")
    void testFavoritosIndependientesDeVotacion() {
        // Given: usuario2 marca proyecto1 como favorito
        favoritoService.agregarFavorito(usuario2, proyecto1);

        // Then: el favorito existe pero el proyecto sigue con 0 likes
        assertTrue(favoritoService.esFavorito(usuario2, proyecto1));
        assertEquals(0, proyecto1.getTotalLikes(), "Los favoritos no deben afectar totalLikes");
    }

    @Test
    @DisplayName("Test 9: Eliminar favorito que no existe")
    void testEliminarFavoritoInexistente() {
        // Given: usuario2 NO tiene proyecto1 en favoritos
        assertFalse(favoritoService.esFavorito(usuario2, proyecto1));

        // When/Then: intentar eliminar debe lanzar excepción
        assertThrows(
            IllegalArgumentException.class,
            () -> favoritoService.eliminarFavorito(usuario2, proyecto1),
            "Debe lanzar excepción al eliminar favorito inexistente"
        );
    }

    @Test
    @DisplayName("Test 10: Agregar favorito duplicado")
    void testAgregarFavoritoDuplicado() {
        // Given: usuario2 ya tiene proyecto1 en favoritos
        favoritoService.agregarFavorito(usuario2, proyecto1);

        // When/Then: intentar agregar de nuevo debe lanzar excepción
        assertThrows(
            IllegalArgumentException.class,
            () -> favoritoService.agregarFavorito(usuario2, proyecto1),
            "Debe lanzar excepción al agregar favorito duplicado"
        );
    }

    @Test
    @DisplayName("Test 11: Usuario puede marcar su propio proyecto como favorito")
    void testUsuarioPuedeMarcarSuProyectoFavorito() {
        // Given: usuario1 es dueño de proyecto1
        assertEquals(usuario1.getId(), proyecto1.getUsuario().getId());

        // When: usuario1 marca su propio proyecto como favorito
        Favorito favorito = favoritoService.agregarFavorito(usuario1, proyecto1);

        // Then: debe permitirlo (diferente a votación)
        assertNotNull(favorito);
        assertTrue(favoritoService.esFavorito(usuario1, proyecto1));
    }
}

