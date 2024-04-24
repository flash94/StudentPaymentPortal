package com.nwamara.studentportal.service;

import com.nwamara.studentportal.config.StudentPortalProperties;
import com.nwamara.studentportal.dao.InvoiceRepository;
import com.nwamara.studentportal.dto.CreateInvoiceResponseDto;
import com.nwamara.studentportal.dto.StudentInvoiceResponseDto;
import com.nwamara.studentportal.persistence.Invoice;
import com.nwamara.studentportal.service.StudentFinanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentFinanceServiceImplTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private WebClient webClient;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private StudentPortalProperties studentPortalProperties;

    @InjectMocks
    private StudentFinanceServiceImpl studentFinanceService;

    @BeforeEach
    void setUp() {
        String studentId = "1";
        Invoice invoice = new Invoice();
        invoice.setId(studentId);
        invoice.setPaymentItemId("1");
        invoice.setInvoiceReference("INV-001");
        invoice.setAmount(100.0);

        invoiceRepository.save(invoice);
    }

    @Test
    void testPayInvoice() {
        // TODO: Implement test for payInvoice method
    }

    @Test
    void testCancelInvoice() {
        // TODO: Implement test for cancelInvoice method
    }

    @Test
    void testGetInvoiceByStudentIdAndCourseId() {
        // Given
        String studentId = "1";
        String courseId = "1";
        Invoice invoice = new Invoice();
        invoice.setId(studentId);
        invoice.setPaymentItemId(courseId);
        invoice.setInvoiceReference("INV-001");
        invoice.setAmount(100.0);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        StudentInvoiceResponseDto expectedDto = new StudentInvoiceResponseDto();
        expectedDto.setId(studentId);
        expectedDto.setPaymentItemId(courseId);
        expectedDto.setInvoiceReference("INV-001");
        expectedDto.setAmount(100.0);
        when(invoiceRepository.findByStudentIdAndPaymentItemId(studentId, courseId)).thenReturn(invoice);

        // When
        StudentInvoiceResponseDto result = studentFinanceService.getInvoiceByStudentIdAndCourseId(studentId, courseId);

        // Then
        assertEquals(expectedDto, result);
    }

    @Test
    void testGetAllInvoicesForStudent() {
        // Given
        String studentId = "1";


        StudentInvoiceResponseDto expectedDto = new StudentInvoiceResponseDto();
        expectedDto.setId(studentId);
        expectedDto.setPaymentItemId("1");
        expectedDto.setInvoiceReference("INV-001");
        expectedDto.setAmount(100.0);
        when(invoiceRepository.findByStudentId(studentId)).thenReturn(Collections.singletonList(new Invoice()));

        // When
        List<StudentInvoiceResponseDto> result = studentFinanceService.getAllInvoicesForStudent(studentId);

        // Then
        assertEquals(1, result.size());
        assertEquals(expectedDto, result.get(0));
    }
}
