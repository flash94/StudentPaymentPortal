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
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.nwamara.studentportal.Helper.Constant.studentId;

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

    @Override
    public String create(CreateStudentDto createStudentDto, Model model) {

        String studentRegistrationNumber = createStudentDto.getStudentRegistrationNumber();
        String userName = createStudentDto.getUserName();
        String email = createStudentDto.getEmail();
        Boolean isStudent = false;
        String password = BCrypt.hashpw(createStudentDto.getPassword(), BCrypt.gensalt());
        //String password = this.passwordEncoder.encode(createStudentDto.getPassword());

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
        model.addAttribute("studentData", studentDto);
        studentId = student.getId();
        return "homepage";
    }

    @Override
    public String findStudentById(String studentId, Model model) {
        Student student = studentRepository.findById(studentId).orElseThrow(()->{return new StudentNotFoundException(studentId);});
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        StudentDto studentProfile = modelMapper.map(student, StudentDto.class);
        model.addAttribute("studentProfile", studentProfile);
        model.addAttribute("editMode", false);
        GeneratedResponseMessage responseMessage = new GeneratedResponseMessage();

        model.addAttribute("responseMessage", responseMessage);
        return "profile";
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
    public String updateStudentById(String studentId, CreateStudentDto updateRequest, Model model) {
        //Student studentOptional = studentRepository.findByStudentRegNumber(studentRegNumber).orElseThrow(()->{return new StudentNotFoundException(id);});
        Student existingStudent = studentRepository.findById(studentId).orElseThrow(()->{return new StudentNotFoundException(studentId);});

        updateField(updateRequest.getFirstName(), existingStudent::setFirstName);
        updateField(updateRequest.getLastName(), existingStudent::setLastName);
        updateField(updateRequest.getMiddleName(), existingStudent::setMiddleName);
        updateField(updateRequest.getEmail(), existingStudent::setEmail);
        updateField(updateRequest.getDepartment(), existingStudent::setDepartment );

        Student updatedStudent = studentRepository.save(existingStudent);
        StudentDto updatedStudentDto = modelMapper.map(updatedStudent, StudentDto.class);
        GeneratedResponseMessage responseMessage = new GeneratedResponseMessage();
        responseMessage.setSuccessMessage("Student profile updated successfully");

        model.addAttribute("responseMessage", responseMessage);
        model.addAttribute("studentProfile", updatedStudentDto);
        return "redirect:/api/v1/students/profile/" + updatedStudent.getId();
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
    public String studentLogin(LoginDto loginRequest, Model model) {
        String userName = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        Student student = studentRepository.findByUserName(userName);
        StudentDto studentDto = modelMapper.map(student, StudentDto.class);
        if (student == null) {
            //return new LoginMessage("User does not exist", false, studentRegNumber);
            throw new StringResponseException("Student with username" + userName + "does not exist");
        }
        String encodedPassword = student.getPassword();
        boolean isPwdMatch = BCrypt.checkpw(password, encodedPassword);
        //boolean isPwdMatch = passwordEncoder.matches(password, encodedPassword);
        if (isPwdMatch) {
            LoginMessage loginMessage = new LoginMessage("Login Success", true, userName);
            model.addAttribute("loginData", loginMessage);
            model.addAttribute("studentData", studentDto);
            studentId = student.getId();
            return "homepage";

        } else {
            throw  new StringResponseException("Incorrect Password");
        }
    }

    @Override
    public String checkGraduationEligibility(String studentRegistrationNumber, Model model) {
        String url = studentPortalProperties.getFinanceAccountBaseUrl();
        String extendUrl = url + "student/" + studentRegistrationNumber;

        CreateStudentFinanceAccountResponse data =  webClient.get()
                .uri(extendUrl)
                .retrieve()
                .bodyToMono(CreateStudentFinanceAccountResponse.class)
                .timeout(Duration.ofMillis(60000))
                .block();

        System.out.println(data);
        model.addAttribute("graduationData", data);
        return "graduation";

    }
}
