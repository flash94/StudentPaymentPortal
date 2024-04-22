package com.nwamara.studentportal.service;

import com.nwamara.studentportal.config.StudentPortalProperties;
import com.nwamara.studentportal.dao.StudentEnrollmentRepository;
import com.nwamara.studentportal.dto.*;
import com.nwamara.studentportal.exception.NotFoundException;
import com.nwamara.studentportal.exception.StringResponseException;
import com.nwamara.studentportal.exception.StudentNotFoundException;
import com.nwamara.studentportal.exception.StudentWithRegNumberExistsException;
import com.nwamara.studentportal.persistence.Student;
import com.nwamara.studentportal.dao.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final ModelMapper modelMapper;
    private final WebClient webClient;
    private final StudentPortalProperties studentPortalProperties;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, StudentEnrollmentRepository studentEnrollmentRepository, ModelMapper modelMapper, WebClient webClient, StudentPortalProperties studentPortalProperties) {
        this.studentRepository = studentRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.modelMapper = modelMapper;
        this.webClient = webClient;
        this.studentPortalProperties = studentPortalProperties;
    }

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public StudentDto create(CreateStudentDto createStudentDto) {

        String studentRegistrationNumber = createStudentDto.getStudentRegistrationNumber();
        String userName = createStudentDto.getUserName();
        String email = createStudentDto.getEmail();
        Boolean isStudent = false;
        String password = this.passwordEncoder.encode(createStudentDto.getPassword());

        Student eStudent = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);
        if (eStudent != null) {
            throw new StudentWithRegNumberExistsException(studentRegistrationNumber);
        }
        Student eStudentWithUserName = studentRepository.findByUserName(userName);
        if (eStudentWithUserName != null) {
            throw new StringResponseException("Student with user name: " + userName + " exists already");
        }
        Student s = new Student(
                 email, userName, password, isStudent
        );

        Student student = studentRepository.save(s);
        StudentDto studentDto = modelMapper.map(student, StudentDto.class);
        return studentDto;
    }

    @Override
    public StudentDto UpdateStudent(CreateStudentDto updateRequest) {
        Student s = modelMapper.map(updateRequest, Student.class);
        Student student = studentRepository.save(s);
        return modelMapper.map(student, StudentDto.class);
    }

    @Override
    public StudentDto UpgradeStudentById(String id) {
        Student existingStudent = studentRepository.findById(id).orElseThrow(()->{return new StudentNotFoundException(id);});
        updateField((generateWalletIdentifier()), existingStudent::setStudentRegistrationNumber);
        updateField(true,existingStudent::setIsStudent);

        Student upgradedStudent = studentRepository.save(existingStudent);

        //create student in finance service
        CreateStudentFinanceAccount(upgradedStudent.getStudentRegistrationNumber());
        return modelMapper.map(upgradedStudent, StudentDto.class);

    }

    public CreateStudentFinanceAccountResponse CreateStudentFinanceAccount(String studentRgNumber){
        String url = studentPortalProperties.getFinanceAccountBaseUrl();
        //String extendUrl = url + "xxx";
        CreateStudentFinanceAccountRequestDto request = CreateStudentFinanceAccountRequestDto.builder()
                .studentId(studentRgNumber)
                .build();

        CreateStudentFinanceAccountResponse data =  webClient.post()
                .uri(url)
                .body(Mono.just(request), CreateStudentFinanceAccountRequestDto.class)
                .retrieve()
                .bodyToMono(CreateStudentFinanceAccountResponse.class)
                .timeout(Duration.ofMillis(60000))
                .block();
        System.out.println(data);
        return data;


    }

    @Override
    public StudentDto updateStudentByRegNumber(String studentRegistrationNumber, CreateStudentDto updateRequest) {
        //Student studentOptional = studentRepository.findByStudentRegNumber(studentRegNumber).orElseThrow(()->{return new StudentNotFoundException(id);});
        Student existingStudent = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);
        if (existingStudent == null) {
            throw new StudentNotFoundException(studentRegistrationNumber);
        }

        updateField(updateRequest.getFirstName(), existingStudent::setFirstName);
        updateField(updateRequest.getLastName(), existingStudent::setLastName);
        updateField(updateRequest.getMiddleName(), existingStudent::setMiddleName);
        updateField(updateRequest.getEmail(), existingStudent::setEmail);
        updateField(updateRequest.getDepartment(), existingStudent::setDepartment );

        Student updatedStudent = studentRepository.save(existingStudent);
        return modelMapper.map(updatedStudent, StudentDto.class);
    }

    private <T> void updateField(T value, Consumer<T> setter) {
        Optional.ofNullable(value).ifPresent(setter);
    }

    private String generateWalletIdentifier(){

        int numberOfFigures = 8;
        Random random = new Random();
        String studentId = String.valueOf(random.nextInt(1,10));

        while(studentId.length() < numberOfFigures){
            studentId += random.nextInt(0, 10);
        }

        Student existingStudent = studentRepository.findByStudentRegistrationNumber(studentId);

        if(existingStudent != null){
            return generateWalletIdentifier();
        }else{
            return studentId;
        }
    }
    @Override
    public StudentDto fetch(String studentRegistrationNumber) {
        Student student = studentRepository.findByStudentRegistrationNumber(studentRegistrationNumber);
        if (student == null) {
            throw new StudentNotFoundException(studentRegistrationNumber);
        }
        return modelMapper.map(student, StudentDto.class);
    }

    @Override
    public List<StudentDto> fetchStudents() {
        List<Student> students = studentRepository.findAll();

        if (students.isEmpty()) {
            throw new NotFoundException("No students found");
        }
        return students.stream()
                .map(student -> modelMapper.map(student, StudentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto delete(String studentRegistrationNumber) {
        return null;
    }

    @Override
    public LoginMessage studentLogin(LoginDto loginRequest) {
        String studentRegNumber = loginRequest.getStudentRegistrationNumber();
        String password = loginRequest.getPassword();
        Student student = studentRepository.findByStudentRegistrationNumber(studentRegNumber);
        if (student == null) {
            return new LoginMessage("User does not exist", false, studentRegNumber);
            //throw new StudentNotFoundException(studentRegNumber);
        }
        String encodedPassword = student.getPassword();
        boolean isPwdMatch = passwordEncoder.matches(password, encodedPassword);
        if (isPwdMatch) {
            return new LoginMessage("Login Success", true, studentRegNumber);
        } else {
            return new LoginMessage("Incorrect Password", false, studentRegNumber);
        }
    }

    @Override
    public CreateStudentFinanceAccountResponse checkGraduationEligibility(String studentRegistrationNumber) {
        String url = studentPortalProperties.getFinanceAccountBaseUrl();
        //String extendUrl = url + "xxx";

        CreateStudentFinanceAccountResponse data =  webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(CreateStudentFinanceAccountResponse.class)
                .timeout(Duration.ofMillis(60000))
                .block();

        System.out.println(data);
        return data;

    }
}
