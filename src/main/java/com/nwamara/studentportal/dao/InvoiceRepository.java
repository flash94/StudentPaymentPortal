package com.nwamara.studentportal.dao;

import com.nwamara.studentportal.dto.StudentInvoiceResponseDto;
import com.nwamara.studentportal.persistence.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    Iterable<Invoice> findByStudentId(String studentId);
    Invoice findByStudentIdAndPaymentItemId(String studentId, String courseId);
}
