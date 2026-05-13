package com.hospital.cprms.config;

import com.hospital.cprms.appointment.Appointment;
import com.hospital.cprms.appointment.AppointmentRepository;
import com.hospital.cprms.appointment.AppointmentStatus;
import com.hospital.cprms.billing.Bill;
import com.hospital.cprms.billing.BillRepository;
import com.hospital.cprms.billing.PaymentStatus;
import com.hospital.cprms.medicalrecord.MedicalRecord;
import com.hospital.cprms.medicalrecord.MedicalRecordRepository;
import com.hospital.cprms.patient.Patient;
import com.hospital.cprms.patient.PatientRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(
        PatientRepository patientRepository,
        MedicalRecordRepository medicalRecordRepository,
        AppointmentRepository appointmentRepository,
        BillRepository billRepository
    ) {
        return args -> {
            if (patientRepository.count() > 0) {
                return;
            }

            Patient patient = new Patient();
            patient.setPatientCode("PAT-1001");
            patient.setFirstName("Rahul");
            patient.setLastName("Sharma");
            patient.setPhoneNumber("9876543210");
            patient.setEmail("rahul.sharma@example.com");
            patient.setAddress("Delhi, India");
            patient.setDateOfBirth(LocalDate.of(1996, 8, 17));
            patient.setGender("Male");
            patient.setBloodGroup("B+");
            patient.setEmergencyContactName("Anita Sharma");
            patient.setEmergencyContactPhone("9123456780");
            Patient savedPatient = patientRepository.save(patient);

            MedicalRecord record = new MedicalRecord();
            record.setPatient(savedPatient);
            record.setDiagnosis("Seasonal viral fever");
            record.setTreatment("Hydration and rest");
            record.setAllergies("No known allergies");
            record.setPrescription("Paracetamol 650mg");
            record.setLabReportUrl("https://example.local/lab-report/1001");
            record.setDoctorName("Dr. Mehta");
            medicalRecordRepository.save(record);

            Appointment appointment = new Appointment();
            appointment.setPatient(savedPatient);
            appointment.setDoctorName("Dr. Mehta");
            appointment.setDepartment("General Medicine");
            appointment.setAppointmentDateTime(LocalDateTime.now().plusDays(1));
            appointment.setStatus(AppointmentStatus.SCHEDULED);
            appointment.setNotes("Follow-up consultation");
            appointmentRepository.save(appointment);

            Bill bill = new Bill();
            bill.setPatient(savedPatient);
            bill.setDescription("Consultation and lab review");
            bill.setAmount(new BigDecimal("1200.00"));
            bill.setStatus(PaymentStatus.PENDING);
            bill.setBillingDate(LocalDate.now());
            billRepository.save(bill);
        };
    }
}
