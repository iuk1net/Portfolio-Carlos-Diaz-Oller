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
        // Configurar ruta absoluta para imágenes de proyectos
        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations("file:./uploads/images/");

        // Configurar ruta para CVs de usuarios
        registry.addResourceHandler("/uploads/cvs/**")
                .addResourceLocations("file:./uploads/cvs/");
    }
}
