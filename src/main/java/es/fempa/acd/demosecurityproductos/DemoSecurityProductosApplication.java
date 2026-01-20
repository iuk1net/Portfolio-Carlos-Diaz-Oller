package es.fempa.acd.demosecurityproductos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.fempa.acd.demosecurityproductos.model.Favorito;
import es.fempa.acd.demosecurityproductos.model.Producto;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.FavoritoRepository;
import es.fempa.acd.demosecurityproductos.repository.ProductoRepository;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;

@SpringBootApplication
public class DemoSecurityProductosApplication {

	
	@Autowired
	private UsuarioRepository userRepository;
	@Autowired
	private FavoritoRepository favoritoRepository;
	@Autowired
	private ProductoRepository productRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(DemoSecurityProductosApplication.class, args);
	}

	// insert a test Product in the database
	@Bean
	public CommandLineRunner insertProduct(ProductoRepository productRepository) {
		return (args) -> {
			Producto product = new Producto();
			product.setNombre("Producto 1");
			product.setDescripcion("Descripcion de producto 1");
			product.setPrecio(130.0);
			product.setImagen("http://miweb.com/img.jpg");
			productRepository.save(product);
		};
	}

	//insert a admin test User in the database
	@Bean
	public CommandLineRunner inserAdmintUser(UsuarioRepository userRepository) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPassword = encoder.encode("123");

		return (args) -> {
			Usuario user = new Usuario();
			user.setUsername("admin");
			user.setEmail("admin@gmail.com");
			user.setPassword(hashedPassword);
			user.setRol(Rol.ADMIN);
			userRepository.save(user);
		};
	}
	
	//insert a cliente  test User in the database
	@Bean
	public CommandLineRunner inserClientetUser(UsuarioRepository userRepository) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPassword = encoder.encode("123");

		return (args) -> {
			Usuario user = new Usuario();
			user.setUsername("cliente");
			user.setEmail("cliente@gmail.com");
			user.setPassword(hashedPassword);
			user.setRol(Rol.CLIENTE);
			userRepository.save(user);
		};
	}
	
	
	//insert a cliente  test User in the database
	@Bean
	public CommandLineRunner insertaFavorito(FavoritoRepository favoritoRepository) {

		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPassword = encoder.encode("123");
		Usuario user = new Usuario();
		user.setUsername("favorito");
		user.setEmail("favorito@gmail.com");
		user.setPassword(hashedPassword);
		user.setRol(Rol.ADMIN);
		userRepository.save(user);
		
		Producto product = new Producto();
		product.setNombre("Producto 2");
		product.setDescripcion("Descripcion de producto 2");
		product.setPrecio(10.0);
		product.setImagen("http://miweb.com/img.jpg");
		productRepository.save(product);
		
		return (args) -> {
            //inserta producto 1 como favorito usuario 1
			Favorito favorito = new Favorito(user, product);
			favoritoRepository.save(favorito);
		};
	}
	
	
}
