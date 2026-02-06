
package es.fempa.acd.demosecurityproductos.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatusCode;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {


    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Solo para errores no manejados por @ControllerAdvice
        model.addAttribute("error", "Ha ocurrido un error inesperado");
        return "error/error";
    }
	
	
//    @GetMapping("/error")
//    public String handleError(HttpServletRequest request, Model model) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
//
//        if (status != null) {
//            int statusCode = Integer.parseInt(status.toString());
//            String errorMessage = message != null ? message : "Ha ocurrido un error";
//            
//            model.addAttribute("error", errorMessage);
//            
//            return switch (statusCode) {
//                case 404 -> "error/404";
//                case 403 -> "error/403";
//                case 500 -> "error/500";
//                default -> "error/error";
//            };
//        }
//        
//        model.addAttribute("error", "Ha ocurrido un error inesperado");
//        return "error/error";
//    }
    
    
//	
//	
//    @GetMapping("/error/403")
//    public String error403(Model model) {
//        // Retorna la vista templates/error/403.html
//        model.addAttribute("error", "Access forbidden - You don't have permission to access this page");
//        return "error/403";
//    }
//    @GetMapping("/error/404")
//    public String error404(Model model) {
//    	// Retorna la vista templates/error/404.html
//        model.addAttribute("error", "Page not found - The page you are looking for might have been removed, had its name changed or is temporarily unavailable");
//    	return "error/404";
//    }
//    @GetMapping("/error/500")
//    public String error500(Model model) {
//    	// Retorna la vista templates/error/500.html
//        model.addAttribute("error", "Internal server error - An unexpected error occurred");
//    	return "error/500";
//    }
    
}
