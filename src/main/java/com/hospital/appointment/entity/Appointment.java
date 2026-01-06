package com.hospital.appointment.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientId;
    private String name;
    private String gender;
    private String phone;
    private String email;
    private String department;
    private String doctor;
    private String date;
    private String time;
    private String status;
}
