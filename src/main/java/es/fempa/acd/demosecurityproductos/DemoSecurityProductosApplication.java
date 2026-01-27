package es.fempa.acd.demosecurityproductos;

import es.fempa.acd.demosecurityproductos.model.Proyecto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.fempa.acd.demosecurityproductos.model.Favorito;
import es.fempa.acd.demosecurityproductos.model.enums.Rol;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.FavoritoRepository;
import es.fempa.acd.demosecurityproductos.repository.ProyectoRepository;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;

@SpringBootApplication
public class DemoSecurityProductosApplication {

	
	@Autowired
	private UsuarioRepository userRepository;
	@Autowired
	private FavoritoRepository favoritoRepository;
	@Autowired
	private ProyectoRepository productRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(DemoSecurityProductosApplication.class, args);
	}

	// insert a test Product in the database
	@Bean
	public CommandLineRunner insertProduct(ProyectoRepository productRepository) {
		return (args) -> {
			Usuario admin = userRepository.findByEmail("admin@gmail.com").orElse(null);
			if (admin != null) {
				Proyecto product = new Proyecto();
				product.setTitulo("Proyecto de Ejemplo 1");
				product.setDescripcion("Descripción del proyecto de ejemplo 1");
				product.setTecnologias("Java, Spring Boot, PostgreSQL");
				product.setEnlaceWeb("https://github.com/ejemplo/proyecto1");
				product.setUsuario(admin);
				productRepository.save(product);
			}
		};
	}

	//insert a admin test User in the database
	@Bean
	public CommandLineRunner inserAdmintUser(UsuarioRepository userRepository) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPassword = encoder.encode("123");

		return (args) -> {
			if (!userRepository.existsByEmail("admin@gmail.com")) {
				Usuario user = new Usuario();
				user.setNombre("Administrador");
				user.setEmail("admin@gmail.com");
				user.setContraseña(hashedPassword);
				user.setRol(Rol.ADMIN);
				userRepository.save(user);
				System.out.println("✓ Usuario ADMIN creado: admin@gmail.com / 123");
			}
		};
	}
	
	//insert a user test User in the database
	@Bean
	public CommandLineRunner inserUserTest(UsuarioRepository userRepository) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPassword = encoder.encode("123");

		return (args) -> {
			if (!userRepository.existsByEmail("user@gmail.com")) {
				Usuario user = new Usuario();
				user.setNombre("Usuario Cliente");
				user.setEmail("user@gmail.com");
				user.setContraseña(hashedPassword);
				user.setRol(Rol.USER);
				userRepository.save(user);
				System.out.println("✓ Usuario USER creado: user@gmail.com / 123");
			}
		};
	}
	
	
	//insert a favorito test
	@Bean
	public CommandLineRunner insertaFavorito(FavoritoRepository favoritoRepository) {

		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPassword = encoder.encode("123");

		return (args) -> {
			if (!userRepository.existsByEmail("favorito@gmail.com")) {
				Usuario user = new Usuario();
				user.setNombre("Usuario Favorito");
				user.setEmail("favorito@gmail.com");
				user.setContraseña(hashedPassword);
				user.setRol(Rol.USER);
				userRepository.save(user);

				Proyecto product = new Proyecto();
				product.setTitulo("Proyecto de Ejemplo 2");
				product.setDescripcion("Descripción del proyecto de ejemplo 2");
				product.setTecnologias("React, Node.js, MongoDB");
				product.setEnlaceWeb("https://github.com/ejemplo/proyecto2");
				product.setUsuario(user);
				productRepository.save(product);

				// Insertar favorito
				Favorito favorito = new Favorito();
				favorito.setUsuario(user);
				favorito.setProyecto(product);
				favoritoRepository.save(favorito);

				System.out.println("✓ Usuario FAVORITO creado: favorito@gmail.com / 123");
			}
		};
	}

}
