package com.nwamara.studentportal.service;

import com.nwamara.studentportal.dto.CreateStudentDto;
import com.nwamara.studentportal.dto.EnrollmentResult;
import com.nwamara.studentportal.dto.StudentDto;
import com.nwamara.studentportal.persistence.Course;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface StudentEnrollmentService {
    public String enrollStudentInCourse(String id, String courseId, Model model, RedirectAttributes redirAttrs);
    public String fetchStudentEnrolledCourses(String id, Model model);
    public String fetchCourseById(Model model, String courseId);
}
