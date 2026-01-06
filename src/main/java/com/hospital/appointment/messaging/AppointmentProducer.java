package com.hospital.appointment.messaging;

import com.hospital.appointment.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AppointmentProducer {

    private final RabbitTemplate rabbitTemplate;

    public AppointmentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.QUEUE_NAME,
                message
        );
    }
}
