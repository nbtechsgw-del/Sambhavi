package com.hospital.cprms.billing;

import com.hospital.cprms.common.exception.ResourceNotFoundException;
import com.hospital.cprms.patient.Patient;
import com.hospital.cprms.patient.PatientService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    private final BillRepository billRepository;
    private final PatientService patientService;

    public BillingService(BillRepository billRepository, PatientService patientService) {
        this.billRepository = billRepository;
        this.patientService = patientService;
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public List<Bill> getBillsByPatient(Long patientId) {
        patientService.getPatient(patientId);
        return billRepository.findByPatientIdOrderByBillingDateDesc(patientId);
    }

    public Bill createBill(Long patientId, Bill bill) {
        Patient patient = patientService.getPatient(patientId);
        bill.setPatient(patient);
        return billRepository.save(bill);
    }

    public Bill updatePaymentStatus(Long billId, PaymentStatus status) {
        Bill bill = billRepository.findById(billId)
            .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id " + billId));
        bill.setStatus(status);
        return billRepository.save(bill);
    }
}
