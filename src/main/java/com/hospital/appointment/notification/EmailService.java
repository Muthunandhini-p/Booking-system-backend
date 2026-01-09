package com.hospital.appointment.notification;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    public void sendEmail(String to, String subject, String body) {

        System.out.println("📨 Preparing SendGrid email...");
        System.out.println("➡ To: " + to);

        Email from = new Email("nandhulogesh9173@gmail.com"); // ✅ VERIFIED SENDER
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println("📧 SendGrid Status Code: " + response.getStatusCode());
            System.out.println("📧 SendGrid Response Body: " + response.getBody());

        } catch (IOException e) {
            System.out.println("❌ SendGrid mail failed");
            e.printStackTrace();
        }
    }
}
