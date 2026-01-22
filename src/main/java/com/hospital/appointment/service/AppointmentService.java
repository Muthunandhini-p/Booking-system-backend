package com.hospital.appointment.service;

import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.notification.EmailService;
import com.hospital.appointment.notification.SmsService;
import com.hospital.appointment.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository repo;
    private final EmailService emailService;
    private final SmsService smsService;

    public AppointmentService(
            AppointmentRepository repo,
            EmailService emailService,
            SmsService smsService
    ) {
        this.repo = repo;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    // ✅ BOOK APPOINTMENT
    public Appointment saveAppointment(Appointment appointment) {

        appointment.setStatus("BOOKED");
        Appointment saved = repo.save(appointment);

        // 📧 EMAIL
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

        // 📱 SMS
        try {
            smsService.sendSms(
                    "+91" + saved.getPhone(),
                    "Appointment Confirmed\nDoctor: " + saved.getDoctor()
                            + "\nDate: " + saved.getDate()
                            + "\nTime: " + saved.getTime()
            );
        } catch (Exception e) {
            System.out.println("Booking SMS failed: " + e.getMessage());
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

        // 📧 EMAIL
        emailService.sendEmail(
                updated.getEmail(),
                "Appointment Rescheduled",
                "Hello " + updated.getName() + ",\n\n" +
                        "Your appointment has been RESCHEDULED.\n\n" +
                        "New Date: " + date + "\n" +
                        "New Time: " + time
        );

        // 📱 SMS
        smsService.sendSms(
                "+91" + updated.getPhone(),
                "Appointment Rescheduled\nNew Date: " + date
                        + "\nNew Time: " + time
        );

        return updated;
    }

    // ✅ CANCEL
    public void cancel(Long id) {

        Appointment appt = repo.findById(id).orElse(null);
        if (appt == null) return;

        // 📧 EMAIL
        emailService.sendEmail(
                appt.getEmail(),
                "Appointment Cancelled",
                "Hello " + appt.getName() + ",\n\n" +
                        "Your appointment has been CANCELLED.\n\n" +
                        "Doctor: " + appt.getDoctor()
        );

        // 📱 SMS
        smsService.sendSms(
                "+91" + appt.getPhone(),
                "Your appointment has been CANCELLED.\nDoctor: "
                        + appt.getDoctor()
        );

        repo.deleteById(id);
    }

    // ✅ RESEND
    public void resendConfirmation(Long id) {

        Appointment appt = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // 📧 EMAIL
        emailService.sendEmail(
                appt.getEmail(),
                "Appointment Confirmation (Resent)",
                "Hello " + appt.getName() + ",\n\n" +
                        "This is a RESENT confirmation.\n\n" +
                        "Doctor: " + appt.getDoctor()
        );

        // 📱 SMS
        smsService.sendSms(
                "+91" + appt.getPhone(),
                "Appointment Confirmation (Resent)\nDoctor: "
                        + appt.getDoctor()
        );
    }
}
