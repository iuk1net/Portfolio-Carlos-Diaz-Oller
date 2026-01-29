package es.fempa.acd.demosecurityproductos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Configuración de Spring MVC
 * Configura el servidor para servir archivos estáticos desde carpetas externas
 *
 * @author Portfolio Social v2.0
 * @version 1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload.images.dir:uploads/images}")
    private String imageUploadDir;

    @Value("${app.upload.dir:uploads/cvs}")
    private String cvUploadDir;

    /**
     * Configura los manejadores de recursos para servir archivos estáticos
     * Permite acceder a imágenes y CVs subidos por los usuarios
     *
     * @param registry Registro de manejadores de recursos
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configurar ruta para imágenes de proyectos
        String imageLocation = Paths.get(imageUploadDir).toUri().toString();
        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations(imageLocation);

        // Configurar ruta para CVs de usuarios
        String cvLocation = Paths.get(cvUploadDir).toUri().toString();
        registry.addResourceHandler("/uploads/cvs/**")
                .addResourceLocations(cvLocation);
    }
}

