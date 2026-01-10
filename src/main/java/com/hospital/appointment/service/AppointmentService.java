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

    // ✅ BOOK APPOINTMENT
    public Appointment saveAppointment(Appointment appointment) {
        appointment.setStatus("BOOKED");

        Appointment saved = repo.save(appointment);

        // ✅ Send confirmation email
        try {
            emailService.sendEmail(
                    saved.getEmail(),
                    "Appointment Confirmed",
                    "Hello " + saved.getName() + ",\n\n" +
                            "Your appointment is CONFIRMED.\n\n" +
                            "Doctor: " + saved.getDoctor() + "\n" +
                            "Date: " + saved.getDate() + "\n" +
                            "Time: " + saved.getTime()
            );
        } catch (Exception e) {
            System.out.println("Booking mail failed: " + e.getMessage());
        }

        return saved;
    }

    // ✅ GET ALL
    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    // ✅ RESCHEDULE
    public Appointment reschedule(Long id, String date, String time) {
        Appointment appt = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appt.setDate(date);
        appt.setTime(time);
        appt.setStatus("RESCHEDULED");

        Appointment updated = repo.save(appt);

        try {
            emailService.sendEmail(
                    updated.getEmail(),
                    "Appointment Rescheduled",
                    "Hello " + updated.getName() + ",\n\n" +
                            "Your appointment has been RESCHEDULED.\n\n" +
                            "New Date: " + date + "\n" +
                            "New Time: " + time
            );
        } catch (Exception e) {
            System.out.println("Reschedule mail failed: " + e.getMessage());
        }

        return updated;
    }

    // ✅ CANCEL (FIXED — NO CRASH, MAIL ALWAYS SENT IF EXISTS)
    public void cancel(Long id) {

        Appointment appt = repo.findById(id).orElse(null);

        if (appt == null) {
            // Already deleted or wrong ID → no crash
            System.out.println("Cancel skipped: Appointment not found");
            return;
        }

        // ✅ Send mail FIRST
        try {
            emailService.sendEmail(
                    appt.getEmail(),
                    "Appointment Cancelled",
                    "Hello " + appt.getName() + ",\n\n" +
                            "Your appointment has been CANCELLED.\n\n" +
                            "Doctor: " + appt.getDoctor() + "\n" +
                            "Date: " + appt.getDate() + "\n" +
                            "Time: " + appt.getTime()
            );
        } catch (Exception e) {
            System.out.println("Cancel mail failed: " + e.getMessage());
        }

        // ✅ Then delete
        repo.deleteById(id);
    }
    public void resendConfirmation(Long id) {

        Appointment appt = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        emailService.sendEmail(
                appt.getEmail(),
                "Appointment Confirmation (Resent)",
                "Hello " + appt.getName() + ",\n\n" +
                        "This is a RESENT confirmation for your appointment.\n\n" +
                        "Doctor: " + appt.getDoctor() + "\n" +
                        "Date: " + appt.getDate() + "\n" +
                        "Time: " + appt.getTime()
        );
    }
}
