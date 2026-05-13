package com.hospital.cprms.report;

import com.hospital.cprms.appointment.AppointmentRepository;
import com.hospital.cprms.billing.BillRepository;
import com.hospital.cprms.billing.PaymentStatus;
import com.hospital.cprms.medicalrecord.MedicalRecordRepository;
import com.hospital.cprms.patient.PatientRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;

    public ReportService(
        PatientRepository patientRepository,
        MedicalRecordRepository medicalRecordRepository,
        AppointmentRepository appointmentRepository,
        BillRepository billRepository
    ) {
        this.patientRepository = patientRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.appointmentRepository = appointmentRepository;
        this.billRepository = billRepository;
    }

    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalPatients", patientRepository.count());
        summary.put("totalMedicalRecords", medicalRecordRepository.count());
        summary.put("totalAppointments", appointmentRepository.count());
        summary.put("totalBills", billRepository.count());
        summary.put("pendingBills", billRepository.countByStatus(PaymentStatus.PENDING));
        summary.put("paidBills", billRepository.countByStatus(PaymentStatus.PAID));
        return summary;
    }
}
