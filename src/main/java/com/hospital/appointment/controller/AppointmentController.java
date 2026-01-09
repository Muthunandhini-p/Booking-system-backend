package com.hospital.appointment.controller;

import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.notification.EmailService;
import com.hospital.appointment.service.AppointmentService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin(
        origins = {
                "https://booking-system-frontend-virid.vercel.app",
                "http://localhost:5173"
        }
)

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService service;
    private final EmailService emailService;

    public AppointmentController(AppointmentService service,EmailService emailService) {
        this.service = service;
        this.emailService = emailService;
    }

    // ✅ BOOK
    @PostMapping
    public Appointment book(@RequestBody Appointment appointment) {

        return service.saveAppointment(appointment);
    }
    // ✅ GET ALL
    @GetMapping
    public List<Appointment> getAll() {
        return service.getAllAppointments();
    }

    // 🔁 RESCHEDULE
    @PutMapping("/{id}")
    public Appointment reschedule(
            @PathVariable Long id,
            @RequestParam String date,
            @RequestParam String time
    ) {
        return service.reschedule(id, date, time);
    }

    // ❌ CANCEL
    @DeleteMapping("/{id}")
    public void cancel(@PathVariable Long id) {
        service.cancel(id);
    }
    @GetMapping("/test-mail")
    public String testMail() {
        emailService.sendEmail(
                "yourgmail@gmail.com",
                "SendGrid Test Mail",
                "🎉 If you got this, SendGrid works!"
        );
        return "Mail sent via SendGrid";
    }
}
