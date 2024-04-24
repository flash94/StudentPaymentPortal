package com.nwamara.studentportal.service;

import com.nwamara.studentportal.dao.CourseRepository;
import com.nwamara.studentportal.dao.StudentEnrollmentRepository;
import com.nwamara.studentportal.dao.StudentRepository;
import com.nwamara.studentportal.exception.CourseNotFoundException;
import com.nwamara.studentportal.exception.NotFoundException;
import com.nwamara.studentportal.exception.StudentNotFoundException;
import com.nwamara.studentportal.persistence.Course;
import com.nwamara.studentportal.persistence.Student;
import com.nwamara.studentportal.persistence.StudentEnrollment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentEnrollmentServiceImplTest {

    @Mock
    private StudentEnrollmentRepository studentEnrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private RedirectAttributes redirAttrs;

    @InjectMocks
    private StudentEnrollmentServiceImpl studentEnrollmentService;

    @BeforeEach
    void setUp() {
        // Create and save sample data before each test
        Student student = new Student();
        student.setId("1");

        Course course1 = new Course();
        course1.setId("1");

        Course course2 = new Course();
        course2.setId("2");

        StudentEnrollment enrollment1 = new StudentEnrollment();
        enrollment1.setStudent(student);
        enrollment1.setCourse(course1);
        enrollment1.setCreatedAt(LocalDateTime.now());

        StudentEnrollment enrollment2 = new StudentEnrollment();
        enrollment2.setStudent(student);
        enrollment2.setCourse(course2);
        enrollment2.setCreatedAt(LocalDateTime.now());

        studentEnrollmentRepository.save(enrollment1);
        studentEnrollmentRepository.save(enrollment2);
    }


    @Test
    void enrollStudentInCourse_WithInvalidStudent_ThrowsStudentNotFoundException() {
        // Arrange
        String studentId = "1";
        String courseId = "2";
        Model model = mock(Model.class);

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(StudentNotFoundException.class, () -> {
            studentEnrollmentService.enrollStudentInCourse(studentId, courseId, model, null);
        });
    }

    @Test
    void enrollStudentInCourse_WithInvalidCourse_ThrowsCourseNotFoundException() {
        // Arrange
        String studentId = "1";
        String courseId = "2";
        Model model = mock(Model.class);
        Student student = new Student();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(CourseNotFoundException.class, () -> {
            studentEnrollmentService.enrollStudentInCourse(studentId, courseId, model, null);
        });
    }

    @Test
    void fetchStudentEnrolledCourses_WithValidStudentId_ReturnsEnrolledCourses() {
        // Arrange
        String studentId = "1";
        Model model = mock(Model.class);
        List<Course> courses = new ArrayList<>();
        courses.add(new Course());

        when(studentEnrollmentRepository.findCoursesByStudentId(studentId)).thenReturn(courses);

        // Act
        String result = studentEnrollmentService.fetchStudentEnrolledCourses(studentId, model);

        // Assert
        assertNotNull(result);
        assertEquals("enrollments", result);
        verify(studentEnrollmentRepository, times(1)).findCoursesByStudentId(studentId);
    }

    @Test
    void fetchStudentEnrolledCourses_WithInvalidStudentId_ThrowsNotFoundException() {
        // Arrange
        String studentId = "1";
        Model model = mock(Model.class);

        when(studentEnrollmentRepository.findCoursesByStudentId(studentId)).thenReturn(new ArrayList<>());

        // Act + Assert
        assertThrows(NotFoundException.class, () -> {
            studentEnrollmentService.fetchStudentEnrolledCourses(studentId, model);
        });
    }

    @Test
    void fetchCourseById_WithValidCourseId_ReturnsCourseDetailPage() {
        // Arrange
        String courseId = "1";
        Model model = mock(Model.class);
        Course course = new Course();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        String result = studentEnrollmentService.fetchCourseById(model, courseId);

        // Assert
        assertNotNull(result);
        assertEquals("enrollmentDetailPage", result);
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void fetchCourseById_WithInvalidCourseId_ThrowsCourseNotFoundException() {
        // Arrange
        String courseId = "1";
        Model model = mock(Model.class);

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(CourseNotFoundException.class, () -> {
            studentEnrollmentService.fetchCourseById(model, courseId);
        });
    }
}
