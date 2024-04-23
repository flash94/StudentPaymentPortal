package com.nwamara.studentportal.controller;

import com.nwamara.studentportal.dto.CreateStudentDto;
import com.nwamara.studentportal.service.StudentCourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/students")
public class CourseController {
    private final StudentCourseService studentCourseService;

    public CourseController(StudentCourseService studentCourseService) {
        this.studentCourseService = studentCourseService;
    }

    @GetMapping("/courses")
    public String listAllCourses(Model model) {
        return studentCourseService.fetchCourses(model);
    }

    @GetMapping("/courses/{course_id}")
    public String getSingleCourse(Model model, @PathVariable(name = "course_id", required = true)String courseId) {
        return studentCourseService.fetchCourseById(model, courseId);
    }


}
