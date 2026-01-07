package com.hospital.appointment.service;

import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.repository.AppointmentRepository;
import com.hospital.appointment.messaging.AppointmentProducer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository repo;

    public AppointmentService(AppointmentRepository repo) {
        this.repo = repo;
    }

    public Appointment saveAppointment(Appointment appointment) {
        appointment.setStatus("BOOKED");
        return repo.save(appointment);
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
        return repo.save(appt);
    }

    public void cancel(Long id) {
        repo.deleteById(id);
    }
}
