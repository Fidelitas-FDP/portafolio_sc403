package com.fdp.portafolio_sc403.service;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class CorreoService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.sender.email}")
    private String senderEmail;

    @Value("${resend.sender.name}")
    private String senderName;

    private final TemplateEngine templateEngine;

    public CorreoService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Motor interno de envío utilizando el SDK v4.x oficial de Resend.
     */
    public void enviarCorreoHtml(String to, String subject, String htmlBody) {
        try {
            Resend resend = new Resend(apiKey);

            // Estándar RFC 5322 para inyección estructurada del nombre
            String senderFormatted = String.format("%s <%s>", senderName, senderEmail);

            // El SDK v4 utiliza CreateEmailOptions en lugar de SendEmailRequest
            CreateEmailOptions options = CreateEmailOptions.builder()
                    .from(senderFormatted)
                    .addTo(to)
                    .subject(subject)
                    .html(htmlBody)
                    .build();

            CreateEmailResponse response = resend.emails().send(options);
            System.out.println("Correo enviado de forma exitosa vía Resend SDK. ID: " + response.getId());

        } catch (Exception e) {
            System.err.println("Error en la ejecución del SDK de Resend: " + e.getMessage());
        }
    }

    /**
     * Orquestador Thymeleaf String-Template Resolver.
     */
    public void enviarCorreoTemplate(String to, String subject, String templatePath, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        String htmlBody = templateEngine.process(templatePath, context);
        enviarCorreoHtml(to, subject, htmlBody);
    }
}