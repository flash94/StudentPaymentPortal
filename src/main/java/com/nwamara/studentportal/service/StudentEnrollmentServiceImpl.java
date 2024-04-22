package com.nwamara.studentportal.service;

import com.nwamara.studentportal.config.StudentPortalProperties;
import com.nwamara.studentportal.dao.InvoiceRepository;
import com.nwamara.studentportal.dao.StudentEnrollmentRepository;
import com.nwamara.studentportal.dao.StudentRepository;
import com.nwamara.studentportal.dto.*;
import com.nwamara.studentportal.exception.StringResponseException;
import com.nwamara.studentportal.exception.StudentNotFoundException;
import com.nwamara.studentportal.persistence.Course;
import com.nwamara.studentportal.persistence.Invoice;
import com.nwamara.studentportal.persistence.Student;
import com.nwamara.studentportal.persistence.StudentEnrollment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService{

    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final InvoiceRepository invoiceRepository;
    private final StudentService studentService;
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    private final WebClient webClient;
    private final StudentPortalProperties studentPortalProperties;


    @Autowired
    public StudentEnrollmentServiceImpl(StudentEnrollmentRepository studentEnrollmentRepository, InvoiceRepository invoiceRepository, StudentService studentService, ModelMapper modelMapper, StudentRepository studentRepository, WebClient webClient, StudentPortalProperties studentPortalProperties) {
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.invoiceRepository = invoiceRepository;
        this.studentService = studentService;
        this.modelMapper = modelMapper;
        this.studentRepository = studentRepository;
        this.webClient = webClient;
        this.studentPortalProperties = studentPortalProperties;
    }
    @Override
    public EnrollmentResult enrollStudentInCourse(String id, Course course) {
        Student existingStudent = studentRepository.findById(id).orElseThrow(()->{return new StudentNotFoundException(id);});
        List<Course> enrolledCourses = studentEnrollmentRepository.findCoursesByStudentId(id);
        if(enrolledCourses.isEmpty()){
            studentService.UpgradeStudentById(id);
            return studentEnrollment(existingStudent, course);
        }
        return studentEnrollment(existingStudent, course);

    }

    @Override
    public List<Course> fetchStudentEnrolledCourses(String id) {
        return studentEnrollmentRepository.findCoursesByStudentId(id);
    }


    public EnrollmentResult studentEnrollment(Student student, Course course) {

        StudentEnrollment checkEnrollment = studentEnrollmentRepository.findByStudentIdAndCourseId(student.getId(), course.getId());
        if(checkEnrollment != null){
            throw  new StringResponseException("Student already enrolled in this course");
        }

        StudentEnrollment enrollment = new StudentEnrollment();
        //Student xStudent = modelMapper.map(studentDto, Student.class);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        StudentEnrollment savedEnrollment = studentEnrollmentRepository.save(enrollment);

        CreateInvoiceResponseDto invoiceResponseDto = savedEnrollment.getId() != null ?
                generateInvoice(student.getStudentRegistrationNumber(), course, student) : null;

        if (invoiceResponseDto == null) {
            throw new StringResponseException("Invoice generation failed: try again.");
        }
        EnrollmentResult result = new EnrollmentResult();
        result.setStudentId(student.getId());
        result.setEnrollmentId(savedEnrollment.getId());
        result.setCourseId(course.getId());
        result.setInvoiceReference(invoiceResponseDto.getReference());

        return result;
    }

    public CreateInvoiceResponseDto generateInvoice(String studentRegNumber, Course course, Student student){
        String url = studentPortalProperties.getFinanceInvoicesBaseUrl();
        String formattedDueDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        CreateInvoiceRequestDto invoice = new CreateInvoiceRequestDto();
        invoice.setAmount(course.getCourseFee());
        invoice.setDueDate(LocalDate.parse(formattedDueDate));
        invoice.setType("TUITION_FEES");

        CreateStudentFinanceAccountRequestDto account = new CreateStudentFinanceAccountRequestDto();
        account.setStudentId(studentRegNumber);
        invoice.setAccount(account);


        System.out.println(invoice);
        CreateInvoiceResponseDto data =  webClient.post()
                .uri(url)
                .bodyValue(invoice)
                .retrieve()
                .bodyToMono(CreateInvoiceResponseDto.class)
                .timeout(Duration.ofMillis(60000))
                .block();
        System.out.println(data);

        if(data.getReference() != null){
            saveInvoiceCopy(data, student, course);
        }
        return data;
    }

    public Invoice saveInvoiceCopy (CreateInvoiceResponseDto data, Student student, Course course){
        Invoice invoice = new Invoice();

        invoice.setStudent(student);
        invoice.setInvoiceReference(data.getReference());
        invoice.setAmount(data.getAmount());
        invoice.setPaymentItemId(course.getId());

        return invoiceRepository.save(invoice);

    }

}
