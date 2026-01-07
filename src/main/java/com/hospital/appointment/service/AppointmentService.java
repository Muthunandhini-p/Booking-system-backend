

package com.hospital.appointment.service;

import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.notification.EmailService;
import com.hospital.appointment.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository repo;
    private final EmailService emailService;

    public AppointmentService(AppointmentRepository repo,
                              EmailService emailService) {
        this.repo = repo;
        this.emailService = emailService;
    }

    // ✅ BOOK
    public Appointment saveAppointment(Appointment appointment) {
        appointment.setStatus("BOOKED");

        Appointment saved = repo.save(appointment);

        // ✅ SEND EMAIL DIRECTLY
        emailService.sendEmail(
                saved.getEmail(),
                "Appointment Confirmed",
                "Hello " + saved.getName() + ",\n\n" +
                        "Your appointment is confirmed.\n" +
                        "Doctor: " + saved.getDoctor() + "\n" +
                        "Date: " + saved.getDate() + "\n" +
                        "Time: " + saved.getTime()
        );

        return saved;
    }

    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    public Appointment reschedule(Long id, String date, String time) {
        Appointment appt = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appt.setDate(date);
        appt.setTime(time);
        appt.setStatus("RESCHEDULED");

        Appointment updated = repo.save(appt);

        emailService.sendEmail(
                updated.getEmail(),
                "Appointment Rescheduled",
                "Your appointment has been rescheduled.\n" +
                        "New Date: " + date + "\n" +
                        "New Time: " + time
        );

        return updated;
    }

    public void cancel(Long id) {

        Appointment appt = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        repo.deleteById(id);

        emailService.sendEmail(
                appt.getEmail(),
                "Appointment Cancelled",
                "Hello " + appt.getName() + ",\n\n" +
                        "Your appointment has been CANCELLED.\n" +
                        "Doctor: " + appt.getDoctor() + "\n" +
                        "Date: " + appt.getDate() + "\n" +
                        "Time: " + appt.getTime()
        );
    }
}
