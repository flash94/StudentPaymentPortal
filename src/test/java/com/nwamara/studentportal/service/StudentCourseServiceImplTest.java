package com.nwamara.studentportal.service;

import com.nwamara.studentportal.config.StudentPortalProperties;
import com.nwamara.studentportal.dao.CourseRepository;
import com.nwamara.studentportal.dao.StudentRepository;
import com.nwamara.studentportal.exception.CourseNotFoundException;
import com.nwamara.studentportal.exception.NotFoundException;
import com.nwamara.studentportal.persistence.Course;
import com.nwamara.studentportal.persistence.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentCourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentPortalProperties studentPortalProperties;

    @Mock
    private Model model;

    @InjectMocks
    private StudentCourseServiceImpl studentCourseService;

    @BeforeEach
    void setUp() {
        /// Mock behavior for dependencies
        Course course1 = new Course();
        course1.setId("1");
        course1.setCourseTitle("Course 1");

        Course course2 = new Course();
        course2.setId("2");
        course2.setCourseTitle("Course 2");

        //when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        Student student = new Student();
        student.setUserName("student1");
        student.setEmail("student1@example.com");
        student.setPassword("password1");
        student.setStudentRegistrationNumber("123");

        //when(studentRepository.findByUserName("student1")).thenReturn(student);
    }

    @Test
    void testFetchCourses_withEmptyCourseList_throwsNotFoundException() {
        // Arrange
        when(courseRepository.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> studentCourseService.fetchCourses(model));
    }

    @Test
    void testFetchCourses_withNonEmptyCourseList_returnsCoursesPage() {
        // Arrange
        when(courseRepository.findAll()).thenReturn(Arrays.asList(new Course(), new Course()));

        // Act
        String viewName = studentCourseService.fetchCourses(model);

        // Assert
        assertEquals("courses", viewName);
        verify(model).addAttribute(eq("courses"), anyList());
    }

    @Test
    void testFetchCourseById_withValidCourseId_returnsCourseDetailPage() {
        // Arrange
        String courseId = "123";
        Course course = new Course();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        String viewName = studentCourseService.fetchCourseById(model, courseId);

        // Assert
        assertEquals("courseDetailPage", viewName);
        verify(model).addAttribute(eq("course"), eq(course));
        verify(model).addAttribute(eq("responseMessage"), any());
    }

    @Test
    void testFetchCourseById_withInvalidCourseId_throwsCourseNotFoundException() {
        // Arrange
        String courseId = "123";
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> studentCourseService.fetchCourseById(model, courseId));
    }
}
