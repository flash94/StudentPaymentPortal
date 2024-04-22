package com.nwamara.studentportal.service;

import com.nwamara.studentportal.config.StudentPortalProperties;
import com.nwamara.studentportal.dao.InvoiceRepository;
import com.nwamara.studentportal.dto.CreateInvoiceResponseDto;
import com.nwamara.studentportal.dto.CreateStudentFinanceAccountResponse;
import com.nwamara.studentportal.dto.StudentDto;
import com.nwamara.studentportal.dto.StudentInvoiceResponseDto;
import com.nwamara.studentportal.persistence.Invoice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StudentFinanceServiceImpl implements StudentFinanceService{

    private final ModelMapper modelMapper;
    private final WebClient webClient;
    private final InvoiceRepository invoiceRepository;
    private final StudentPortalProperties studentPortalProperties;

    @Autowired
    public StudentFinanceServiceImpl(ModelMapper modelMapper, WebClient webClient, InvoiceRepository invoiceRepository, StudentPortalProperties studentPortalProperties) {
        this.modelMapper = modelMapper;
        this.webClient = webClient;
        this.invoiceRepository = invoiceRepository;
        this.studentPortalProperties = studentPortalProperties;
    }

    @Override
    public CreateInvoiceResponseDto payInvoice(String studentId, String invoiceReference) {
        String url = studentPortalProperties.getFinanceInvoicesBaseUrl();
        String extendUrl = url + invoiceReference + "/pay";

        CreateInvoiceResponseDto data =  webClient.put()
                .uri(extendUrl)
                .retrieve()
                .bodyToMono(CreateInvoiceResponseDto.class)
                .timeout(Duration.ofMillis(60000))
                .block();

        System.out.println(data);
        return data;
    }

    @Override
    public CreateInvoiceResponseDto cancelInvoice(String studentId, String invoiceReference) {
        String url = studentPortalProperties.getFinanceInvoicesBaseUrl();
        String extendUrl = url + invoiceReference + "/cancel";

        CreateInvoiceResponseDto data =  webClient.delete()
                .uri(extendUrl)
                .retrieve()
                .bodyToMono(CreateInvoiceResponseDto.class)
                .timeout(Duration.ofMillis(60000))
                .block();

        System.out.println(data);
        return data;
    }

    @Override
    public StudentInvoiceResponseDto getInvoiceByStudentIdAndCourseId(String studentId, String courseId) {
        Invoice studentInvoice = invoiceRepository.findByStudentIdAndPaymentItemId(studentId, courseId);
        return  modelMapper.map(studentInvoice, StudentInvoiceResponseDto.class);
    }

    @Override
    public List<StudentInvoiceResponseDto> getAllInvoicesForStudent(String studentId) {
        Iterable<Invoice> iList = invoiceRepository.findByStudentId(studentId);
        return StreamSupport.stream(iList.spliterator(), false)
                .map(invoice -> modelMapper.map(invoice, StudentInvoiceResponseDto.class))
               .collect(Collectors.toList());
        //return Collections.singletonList(modelMapper.map(x, StudentInvoiceResponseDto.class));

    }
}
