package com.hospital.cprms.patient;

import com.hospital.cprms.common.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatient(Long id) {
        return patientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
    }

    public Patient createPatient(Patient patient) {
        if (patient.getPatientCode() == null || patient.getPatientCode().isBlank()) {
            patient.setPatientCode("PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existing = getPatient(id);
        existing.setFirstName(updatedPatient.getFirstName());
        existing.setLastName(updatedPatient.getLastName());
        existing.setPhoneNumber(updatedPatient.getPhoneNumber());
        existing.setEmail(updatedPatient.getEmail());
        existing.setAddress(updatedPatient.getAddress());
        existing.setDateOfBirth(updatedPatient.getDateOfBirth());
        existing.setGender(updatedPatient.getGender());
        existing.setBloodGroup(updatedPatient.getBloodGroup());
        existing.setEmergencyContactName(updatedPatient.getEmergencyContactName());
        existing.setEmergencyContactPhone(updatedPatient.getEmergencyContactPhone());
        return patientRepository.save(existing);
    }

    public void deletePatient(Long id) {
        patientRepository.delete(getPatient(id));
    }

    public List<Patient> searchPatients(String keyword) {
        return patientRepository
            .findByPatientCodeContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
                keyword,
                keyword,
                keyword,
                keyword
            );
    }
}
