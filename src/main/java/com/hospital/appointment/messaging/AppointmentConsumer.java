package com.hospital.appointment.messaging;
import com.hospital.appointment.config.RabbitMQConfig;
import com.hospital.appointment.notification.EmailService;
import com.hospital.appointment.notification.SmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class AppointmentConsumer {

    private final EmailService emailService;
    private final SmsService smsService;

    public AppointmentConsumer(EmailService emailService,
                               SmsService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {

        System.out.println("📩 Message from RabbitMQ: " + message);

        // SEND EMAIL
        emailService.sendEmail(
                "patient@gmail.com",   // for now fixed email
                "Hospital Appointment Update",
                message
        );
        smsService.sendSms("9876543210", message);

    }

}
