package es.fempa.acd.demosecurityproductos.config;

import java.time.LocalDateTime;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.access.AccessDeniedException;

import es.fempa.acd.demosecurityproductos.model.ErrorResponse;

// DESHABILITADO 

// Este controlador maneja las excepciones de los controladores REST, devolviendo un JSON con los detalles del error

//@RestControllerAdvice(basePackages = "es.fempa.acd.demosecurityproductos.rest")
public class RestExceptionHandler {

//	@ExceptionHandler(ResourceNotFoundException.class)
//	public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
//		ErrorResponse error = new ErrorResponse(
//				HttpStatus.NOT_FOUND.value(),
//				ex.getMessage(),
//				LocalDateTime.now(),
//				((ServletWebRequest) request).getRequest().getRequestURI()
//				);
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//	}
//
//	@ExceptionHandler(IllegalArgumentException.class)
//	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
//		ErrorResponse error = new ErrorResponse(
//				HttpStatus.BAD_REQUEST.value(),
//				ex.getMessage(),
//				LocalDateTime.now(),
//				((ServletWebRequest) request).getRequest().getRequestURI()
//				);
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//	}
//
//	@ExceptionHandler(AccessDeniedException.class)
//	public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
//		ErrorResponse error = new ErrorResponse(
//				HttpStatus.FORBIDDEN.value(),
//				"Acceso denegado",
//				LocalDateTime.now(),
//				((ServletWebRequest) request).getRequest().getRequestURI()
//				);
//		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
//	}
//
//
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
//		ErrorResponse error = new ErrorResponse(
//				HttpStatus.INTERNAL_SERVER_ERROR.value(),
//				ex.getMessage(),
//				LocalDateTime.now(),
//				((ServletWebRequest) request).getRequest().getRequestURI()
//				);
//		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//	}

}
