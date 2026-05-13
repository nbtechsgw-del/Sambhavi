package com.hospital.cprms.appointment;

import com.hospital.cprms.common.exception.ResourceNotFoundException;
import com.hospital.cprms.patient.Patient;
import com.hospital.cprms.patient.PatientService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientService patientService;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientService patientService) {
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        patientService.getPatient(patientId);
        return appointmentRepository.findByPatientIdOrderByAppointmentDateTimeDesc(patientId);
    }

    public Appointment createAppointment(Long patientId, Appointment appointment) {
        Patient patient = patientService.getPatient(patientId);
        appointment.setPatient(patient);
        return appointmentRepository.save(appointment);
    }

    public Appointment updateStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + appointmentId));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }
}
