package com.nwamara.studentportal.controller;

import com.nwamara.studentportal.Helper.Constant;
import com.nwamara.studentportal.dto.CreateStudentDto;
import com.nwamara.studentportal.dto.EnrollmentResult;
import com.nwamara.studentportal.dto.StudentDto;
import com.nwamara.studentportal.persistence.Course;
import com.nwamara.studentportal.service.StudentEnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/api/v1/enroll")
public class StudentEnrollmentController {
    private final StudentEnrollmentService studentEnrollmentService;

    @Autowired
    public StudentEnrollmentController(StudentEnrollmentService studentEnrollmentService) {
        this.studentEnrollmentService = studentEnrollmentService;
    }

    @PostMapping("/course/{courseId}")
    public String enrollStudentInCourse(String id, @PathVariable String courseId, Model model, RedirectAttributes redirAttrs){
        id = Constant.studentId;
        return studentEnrollmentService.enrollStudentInCourse(id,courseId, model, redirAttrs);
    }

    @GetMapping("/course")
    public String getStudentEnrolledCourses(String id, Model model){
        id = Constant.studentId;
        return studentEnrollmentService.fetchStudentEnrolledCourses(id, model);
    }

    @GetMapping("/course/{course_id}")
    public String getSingleCourse(Model model, @PathVariable(name = "course_id", required = true)String courseId) {
        return studentEnrollmentService.fetchCourseById(model, courseId);
    }


}
