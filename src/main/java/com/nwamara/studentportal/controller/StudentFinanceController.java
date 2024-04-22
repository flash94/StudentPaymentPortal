package com.nwamara.studentportal.controller;

import com.nwamara.studentportal.dto.CreateInvoiceResponseDto;
import com.nwamara.studentportal.dto.CreateStudentDto;
import com.nwamara.studentportal.dto.StudentDto;
import com.nwamara.studentportal.dto.StudentInvoiceResponseDto;
import com.nwamara.studentportal.persistence.Invoice;
import com.nwamara.studentportal.service.StudentFinanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student/finance/")
public class StudentFinanceController {
    private final StudentFinanceService studentFinanceService;

    @Autowired
    public StudentFinanceController(StudentFinanceService studentFinanceService) {
        this.studentFinanceService = studentFinanceService;
    }

    @GetMapping("/{studentId}/invoices")
    public ResponseEntity<List<StudentInvoiceResponseDto>> getAllInvoicesForStudent(@PathVariable String studentId) {
        List<StudentInvoiceResponseDto> invoices = studentFinanceService.getAllInvoicesForStudent(studentId);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{studentId}/invoices/{courseId}")
    public ResponseEntity<StudentInvoiceResponseDto> getInvoiceByStudentIdAndCourseId(@PathVariable String studentId,
                                                                    @PathVariable String courseId) {
        StudentInvoiceResponseDto invoice = studentFinanceService.getInvoiceByStudentIdAndCourseId(studentId, courseId);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{studentId}/invoices/pay/{invoiceReference}")
    public ResponseEntity<CreateInvoiceResponseDto> payStudentInvoice(@PathVariable(name = "studentId", required = true)String studentRegNumber,
                                                                      @PathVariable(name = "invoiceReference", required = true)String invoiceReference){
       CreateInvoiceResponseDto invoice = studentFinanceService.payInvoice(studentRegNumber, invoiceReference);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{studentId}/invoices/cancel/{invoiceReference}")
    public ResponseEntity<CreateInvoiceResponseDto> cancelStudentInvoice(@PathVariable(name = "studentId", required = true)String studentRegNumber,
                                        @PathVariable(name = "invoiceReference", required = true)String invoiceReference){
        CreateInvoiceResponseDto invoice = studentFinanceService.cancelInvoice(studentRegNumber, invoiceReference);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.internalServerError().build();
        }

    }
}
