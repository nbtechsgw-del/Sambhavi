package com.hospital.cprms.medicalrecord;

import com.hospital.cprms.patient.Patient;
import com.hospital.cprms.patient.PatientService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientService patientService;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, PatientService patientService) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientService = patientService;
    }

    public List<MedicalRecord> getRecordsByPatient(Long patientId) {
        patientService.getPatient(patientId);
        return medicalRecordRepository.findByPatientIdOrderByRecordedAtDesc(patientId);
    }

    public MedicalRecord addRecord(Long patientId, MedicalRecord medicalRecord) {
        Patient patient = patientService.getPatient(patientId);
        medicalRecord.setPatient(patient);
        return medicalRecordRepository.save(medicalRecord);
    }
}
