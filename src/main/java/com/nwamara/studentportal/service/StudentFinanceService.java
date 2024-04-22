package com.nwamara.studentportal.service;

import com.nwamara.studentportal.dto.CreateInvoiceResponseDto;
import com.nwamara.studentportal.dto.StudentInvoiceResponseDto;
import com.nwamara.studentportal.persistence.Invoice;

import java.util.List;

public interface StudentFinanceService {
    public CreateInvoiceResponseDto payInvoice (String studentId, String invoiceReference);
    public CreateInvoiceResponseDto cancelInvoice (String studentId, String invoiceReference);
    public StudentInvoiceResponseDto getInvoiceByStudentIdAndCourseId (String studentId, String courseId);
    public List<StudentInvoiceResponseDto> getAllInvoicesForStudent (String studentId);
}
