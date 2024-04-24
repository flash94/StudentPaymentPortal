package com.nwamara.studentportal.service;

import com.nwamara.studentportal.config.StudentPortalProperties;
import com.nwamara.studentportal.dao.StudentEnrollmentRepository;
import com.nwamara.studentportal.dao.StudentRepository;
import com.nwamara.studentportal.dto.*;
import com.nwamara.studentportal.exception.StringResponseException;
import com.nwamara.studentportal.exception.StudentNotFoundException;
import com.nwamara.studentportal.exception.StudentWithRegNumberExistsException;
import com.nwamara.studentportal.persistence.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentEnrollmentRepository studentEnrollmentRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private WebClient webClient;

    @Mock
    private StudentPortalProperties studentPortalProperties;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);

        // Mock behavior for common methods or entities used across multiple test cases
        Student student = new Student();
        student.setId("1");
        student.setUserName("john");

        studentRepository.save(student);

        //when(studentRepository.findById("1")).thenReturn(Optional.of(student));
        //when(studentRepository.findByStudentRegistrationNumber("123")).thenReturn(null);
        //when(studentRepository.findByUserName("john")).thenReturn(null);
    }

    @Test
    void testCreate() {
        // Given
        CreateStudentDto createStudentDto = new CreateStudentDto();
        createStudentDto.setStudentRegistrationNumber("123");
        createStudentDto.setUserName("john");
        createStudentDto.setEmail("john@example.com");
        createStudentDto.setPassword("password");

        //when(studentRepository.findByStudentRegistrationNumber("123")).thenReturn(null);
        //when(studentRepository.findByUserName("john")).thenReturn(null);

        Student savedStudent = new Student();
        savedStudent.setId("1");
        savedStudent.setStudentRegistrationNumber("123");
        savedStudent.setUserName("john");
        savedStudent.setEmail("john@example.com");

        //when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        // When
        String result = studentService.create(createStudentDto, mock(Model.class));

        // Then
        assertEquals("homepage", result);
        assertEquals("1", savedStudent.getId());
    }

    @Test
    void testCreate_StudentWithRegNumberExists() {
        // Given
        CreateStudentDto createStudentDto = new CreateStudentDto();
        createStudentDto.setStudentRegistrationNumber("123");

        when(studentRepository.findByStudentRegistrationNumber("123")).thenReturn(new Student());

        // When/Then
        assertThrows(StudentWithRegNumberExistsException.class, () -> {
            studentService.create(createStudentDto, mock(Model.class));
        });
    }

    @Test
    void testCreate_StudentWithUserNameExists() {
        // Given
        CreateStudentDto createStudentDto = new CreateStudentDto();
        createStudentDto.setUserName("john");

        Student student = new Student();
        student.setId("1");
       when(studentRepository.findByUserName("john")).thenReturn(student);

        // When/Then
        assertThrows(StringResponseException.class, () -> {
            studentService.create(createStudentDto, mock(Model.class));
        });
    }

    @Test
    void testFindStudentById() {
        // Given
        String studentId = "1";
        Student student = new Student();
        student.setId(studentId);
        student.setUserName("johnx");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(modelMapper.map(student, StudentDto.class)).thenReturn(new StudentDto());

        // When
        String result = studentService.findStudentById(studentId, mock(Model.class));

        // Then
        assertEquals("profile", result);
        verify(modelMapper, times(1)).map(student, StudentDto.class);
    }

    @Test
    void testFindStudentById_StudentNotFound() {
        // Given
        String studentId = "1";

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(StudentNotFoundException.class, () -> {
            studentService.findStudentById(studentId, mock(Model.class));
        });
    }

    @Test
    void testUpdateStudentById() {
        // Given
        String studentId = "1";
        CreateStudentDto updateRequest = new CreateStudentDto();
        updateRequest.setFirstName("John");
        updateRequest.setLastName("Doe");

        Student existingStudent = new Student();
        existingStudent.setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(modelMapper.map(any(Student.class), eq(StudentDto.class))).thenReturn(new StudentDto());

        // When
        String result = studentService.updateStudentById(studentId, updateRequest, mock(Model.class));

        // Then
        assertEquals("redirect:/api/v1/students/profile/1", result);
    }

    @Test
    void testUpdateStudentById_StudentNotFound() {
        // Given
        String studentId = "1";
        CreateStudentDto updateRequest = new CreateStudentDto();

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(StudentNotFoundException.class, () -> {
            studentService.updateStudentById(studentId, updateRequest, mock(Model.class));
        });
    }

    @Test
    void testUpdateStudentById_NullFields() {
        // Given
        String studentId = "1";
        CreateStudentDto updateRequest = new CreateStudentDto();

        Student existingStudent = new Student();
        existingStudent.setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(modelMapper.map(any(Student.class), eq(StudentDto.class))).thenReturn(new StudentDto());

        // When
        String result = studentService.updateStudentById(studentId, updateRequest, mock(Model.class));

        // Then
        assertEquals("redirect:/api/v1/students/profile/1", result);
    }


    @Test
    void testUpgradeStudentById_StudentNotFound() {
        // Given
        String studentId = "1";

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(StudentNotFoundException.class, () -> {
            studentService.findStudentById(studentId, mock(Model.class));
        });
    }

}

