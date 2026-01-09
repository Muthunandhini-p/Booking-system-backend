package com.hospital.appointment.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailService {

    private final WebClient webClient;

    public EmailService(@Value("${BREVO_API_KEY}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.brevo.com/v3")
                .defaultHeader("api-key", apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void sendEmail(String to, String subject, String body) {

        Map<String, Object> payload = Map.of(
                "sender", Map.of(
                        "name", "Hospital Appointment",
                        "email", "nandhulogesh9173@gmail.com"
                ),
                "to", new Object[]{
                        Map.of("email", to)
                },
                "subject", subject,
                "htmlContent", body.replace("\n", "<br>")
        );

        webClient.post()
                .uri("/smtp/email")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(res -> System.out.println("✅ Email sent via Brevo"))
                .doOnError(err -> System.out.println("❌ Email failed: " + err.getMessage()))
                .block();
    }
}
