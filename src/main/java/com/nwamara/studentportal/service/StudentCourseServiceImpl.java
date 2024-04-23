package com.nwamara.studentportal.service;

import com.nwamara.studentportal.config.StudentPortalProperties;
import com.nwamara.studentportal.dao.CourseRepository;
import com.nwamara.studentportal.dao.InvoiceRepository;
import com.nwamara.studentportal.dao.StudentEnrollmentRepository;
import com.nwamara.studentportal.dao.StudentRepository;
import com.nwamara.studentportal.dto.GeneratedResponseMessage;
import com.nwamara.studentportal.dto.StudentDto;
import com.nwamara.studentportal.exception.CourseNotFoundException;
import com.nwamara.studentportal.exception.NotFoundException;
import com.nwamara.studentportal.exception.StudentNotFoundException;
import com.nwamara.studentportal.persistence.Course;
import com.nwamara.studentportal.persistence.Student;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {
    private final CourseRepository courseRepository;
    private final StudentPortalProperties studentPortalProperties;

    public StudentCourseServiceImpl(CourseRepository courseRepository, StudentPortalProperties studentPortalProperties) {
        this.courseRepository = courseRepository;
        this.studentPortalProperties = studentPortalProperties;
    }

    @Override
    public String fetchCourses(Model model) {

        List<Course> courses = courseRepository.findAll();

        if (courses.isEmpty()) {
            throw new NotFoundException("No Course Found");
        }

        model.addAttribute("courses", courses);
        return "courses";

    }

    @Override
    public String fetchCourseById(Model model, String courseId) {
         Course course = courseRepository.findById(courseId).orElseThrow(()->{return new CourseNotFoundException(courseId);});
         GeneratedResponseMessage responseMessage = new GeneratedResponseMessage();
         model.addAttribute("course", course);
         model.addAttribute("responseMessage", responseMessage);

         return "courseDetailPage";
    }

}
