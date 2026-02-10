package es.fempa.acd.demosecurityproductos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para páginas legales e informativas
 * Requerido para LinkedIn App Verification
 *
 * @version 1.0
 * @date 07/02/2026
 */
@Controller
public class LegalController {

    /**
     * Página de Privacy Policy
     * Requerida por LinkedIn para app verification
     */
    @GetMapping("/privacy")
    public String privacyPolicy() {
        return "legal/privacy";
    }

    /**
     * Página de Terms of Service
     * Requerida por LinkedIn para app verification
     */
    @GetMapping("/terms")
    public String termsOfService() {
        return "legal/terms";
    }

    /**
     * Página de contacto/soporte
     */
    @GetMapping("/support")
    public String support() {
        return "legal/support";
    }
}

