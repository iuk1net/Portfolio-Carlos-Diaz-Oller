package es.fempa.acd.demosecurityproductos.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

// Solo para APIs REST, no para controladores web
@RestControllerAdvice(basePackages = "es.fempa.acd.demosecurityproductos.rest")
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(ex.getMessage());
    }

    // NO capturar AccessDeniedException aquí - dejar que Spring Security lo maneje

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        // Ignorar AccessDeniedException
        if (ex instanceof AccessDeniedException) {
            throw (AccessDeniedException) ex;
        }

        // Regresar un mensaje genérico de error con código 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Error interno en el servidor: " + ex.getMessage());
    }
}