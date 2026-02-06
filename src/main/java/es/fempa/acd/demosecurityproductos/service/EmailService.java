package es.fempa.acd.demosecurityproductos.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Servicio para el envío de emails.
 * Gestiona el envío de correos electrónicos usando JavaMailSender.
 *
 * @author Carlos Díaz Oller
 * @version 2.6.0
 * @since 2026-02-06
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${verificacion.email.url-base:http://localhost:8089}")
    private String urlBase;

    /**
     * Envía un email simple de texto plano.
     *
     * @param to destinatario
     * @param subject asunto
     * @param text contenido del mensaje
     */
    public void enviarEmailSimple(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Email simple enviado a: {}", to);
        } catch (Exception e) {
            log.error("Error al enviar email simple a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error al enviar email: " + e.getMessage(), e);
        }
    }

    /**
     * Envía un email HTML.
     *
     * @param to destinatario
     * @param subject asunto
     * @param htmlContent contenido HTML del mensaje
     */
    public void enviarEmailHtml(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = es HTML

            mailSender.send(message);
            log.info("Email HTML enviado a: {}", to);
        } catch (MessagingException e) {
            log.error("Error al enviar email HTML a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error al enviar email HTML: " + e.getMessage(), e);
        }
    }

    /**
     * Envía un email de verificación de cuenta.
     *
     * @param to destinatario
     * @param nombreUsuario nombre del usuario
     * @param token token de verificación
     */
    public void enviarEmailVerificacion(String to, String nombreUsuario, String token) {
        String subject = "Verifica tu cuenta - Portfolio Social";
        String urlVerificacion = urlBase + "/verificar-email?token=" + token;

        String htmlContent = construirEmailVerificacion(nombreUsuario, urlVerificacion);

        enviarEmailHtml(to, subject, htmlContent);
        log.info("Email de verificación enviado a: {}", to);
    }

    /**
     * Envía un email de recuperación de contraseña.
     *
     * @param to destinatario
     * @param nombreUsuario nombre del usuario
     * @param token token de recuperación
     */
    public void enviarEmailRecuperacion(String to, String nombreUsuario, String token) {
        String subject = "Recuperación de contraseña - Portfolio Social";
        String urlRecuperacion = urlBase + "/recuperar-password?token=" + token;

        String htmlContent = construirEmailRecuperacion(nombreUsuario, urlRecuperacion);

        enviarEmailHtml(to, subject, htmlContent);
        log.info("Email de recuperación enviado a: {}", to);
    }

    /**
     * Envía un email de bienvenida tras verificación exitosa.
     *
     * @param to destinatario
     * @param nombreUsuario nombre del usuario
     */
    public void enviarEmailBienvenida(String to, String nombreUsuario) {
        String subject = "¡Bienvenido a Portfolio Social!";

        String htmlContent = construirEmailBienvenida(nombreUsuario);

        enviarEmailHtml(to, subject, htmlContent);
        log.info("Email de bienvenida enviado a: {}", to);
    }

    // Métodos privados para construir HTML de los emails

    private String construirEmailVerificacion(String nombreUsuario, String urlVerificacion) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #007bff; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .button { display: inline-block; padding: 12px 30px; background-color: #28a745; 
                              color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                    .footer { text-align: center; padding: 20px; font-size: 12px; color: #666; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🎯 Verifica tu cuenta</h1>
                    </div>
                    <div class="content">
                        <h2>Hola %s,</h2>
                        <p>Gracias por registrarte en <strong>Portfolio Social</strong>.</p>
                        <p>Para activar tu cuenta, por favor haz clic en el siguiente botón:</p>
                        <p style="text-align: center;">
                            <a href="%s" class="button">✅ Verificar mi cuenta</a>
                        </p>
                        <p>O copia y pega este enlace en tu navegador:</p>
                        <p style="word-break: break-all; background-color: #fff; padding: 10px; border: 1px solid #ddd;">
                            %s
                        </p>
                        <p><strong>⚠️ Este enlace expira en 24 horas.</strong></p>
                        <p>Si no creaste esta cuenta, puedes ignorar este mensaje.</p>
                    </div>
                    <div class="footer">
                        <p>Portfolio Social © 2026 | Carlos Díaz Oller</p>
                        <p>Este es un email automático, por favor no respondas a este mensaje.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nombreUsuario, urlVerificacion, urlVerificacion);
    }

    private String construirEmailRecuperacion(String nombreUsuario, String urlRecuperacion) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #dc3545; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .button { display: inline-block; padding: 12px 30px; background-color: #ffc107; 
                              color: #333; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                    .footer { text-align: center; padding: 20px; font-size: 12px; color: #666; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🔒 Recuperación de contraseña</h1>
                    </div>
                    <div class="content">
                        <h2>Hola %s,</h2>
                        <p>Hemos recibido una solicitud para restablecer la contraseña de tu cuenta.</p>
                        <p>Para crear una nueva contraseña, haz clic en el siguiente botón:</p>
                        <p style="text-align: center;">
                            <a href="%s" class="button">🔑 Restablecer contraseña</a>
                        </p>
                        <p>O copia y pega este enlace en tu navegador:</p>
                        <p style="word-break: break-all; background-color: #fff; padding: 10px; border: 1px solid #ddd;">
                            %s
                        </p>
                        <p><strong>⚠️ Este enlace expira en 24 horas.</strong></p>
                        <p><strong>Si no solicitaste restablecer tu contraseña, ignora este mensaje.</strong> 
                           Tu contraseña no cambiará.</p>
                    </div>
                    <div class="footer">
                        <p>Portfolio Social © 2026 | Carlos Díaz Oller</p>
                        <p>Este es un email automático, por favor no respondas a este mensaje.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nombreUsuario, urlRecuperacion, urlRecuperacion);
    }

    private String construirEmailBienvenida(String nombreUsuario) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #28a745; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .button { display: inline-block; padding: 12px 30px; background-color: #007bff; 
                              color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                    .footer { text-align: center; padding: 20px; font-size: 12px; color: #666; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🎉 ¡Bienvenido!</h1>
                    </div>
                    <div class="content">
                        <h2>Hola %s,</h2>
                        <p>¡Tu cuenta ha sido verificada exitosamente!</p>
                        <p>Ya puedes acceder a todas las funcionalidades de <strong>Portfolio Social</strong>:</p>
                        <ul>
                            <li>✅ Crear y publicar proyectos</li>
                            <li>✅ Subir tu CV</li>
                            <li>✅ Votar proyectos de otros usuarios</li>
                            <li>✅ Guardar proyectos favoritos</li>
                            <li>✅ Compartir en redes sociales</li>
                        </ul>
                        <p style="text-align: center;">
                            <a href="%s" class="button">🚀 Ir a mi dashboard</a>
                        </p>
                        <p>¡Esperamos que disfrutes de la plataforma!</p>
                    </div>
                    <div class="footer">
                        <p>Portfolio Social © 2026 | Carlos Díaz Oller</p>
                        <p>Este es un email automático, por favor no respondas a este mensaje.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nombreUsuario, urlBase + "/usuario/dashboard");
    }
}

