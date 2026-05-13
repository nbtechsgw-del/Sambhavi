package com.hospital.cprms.billing;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByPatientIdOrderByBillingDateDesc(Long patientId);

    long countByStatus(PaymentStatus status);
}
