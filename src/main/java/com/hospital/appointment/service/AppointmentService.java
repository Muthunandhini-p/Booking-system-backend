package com.hospital.appointment.service;

import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.repository.AppointmentRepository;
import com.hospital.appointment.messaging.AppointmentProducer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository repo;
    private final AppointmentProducer producer;

    public AppointmentService(AppointmentRepository repo,
                              AppointmentProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }

    // ✅ BOOK APPOINTMENT
    public Appointment saveAppointment(Appointment appointment) {
        appointment.setStatus("BOOKED");

        Appointment saved = repo.save(appointment);

        producer.sendMessage(
                "Appointment BOOKED for " + saved.getName()
        );

        return saved;
    }

    // ✅ GET ALL APPOINTMENTS
    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    // 🔁 RESCHEDULE APPOINTMENT
    public Appointment reschedule(Long id, String date, String time) {
        Appointment appt = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appt.setDate(date);
        appt.setTime(time);
        appt.setStatus("RESCHEDULED");

        Appointment updated = repo.save(appt);

        producer.sendMessage(
                "Appointment RESCHEDULED for " + updated.getName()
        );

        return updated;
    }

    // ❌ CANCEL APPOINTMENT
    public void cancel(Long id) {
        repo.deleteById(id);

        producer.sendMessage(
                "Appointment CANCELLED with ID " + id
        );
    }
}
