package com.nwamara.studentportal.controller;

import com.nwamara.studentportal.dto.CreateStudentDto;
import com.nwamara.studentportal.dto.EnrollmentResult;
import com.nwamara.studentportal.dto.StudentDto;
import com.nwamara.studentportal.persistence.Course;
import com.nwamara.studentportal.service.StudentEnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enroll")
public class StudentEnrollmentController {
    private final StudentEnrollmentService studentEnrollmentService;

    @Autowired
    public StudentEnrollmentController(StudentEnrollmentService studentEnrollmentService) {
        this.studentEnrollmentService = studentEnrollmentService;
    }

    @PostMapping("/{id}")
    public EnrollmentResult enrollStudentInCourse(@PathVariable(name = "id", required = true)String id, @RequestBody @Valid Course course){
        return studentEnrollmentService.enrollStudentInCourse(id,course);
    }

    @GetMapping("/{id}")
    public List<Course> getStudentEnrolledCourses(@PathVariable(name = "id", required = true)String id){
        return studentEnrollmentService.fetchStudentEnrolledCourses(id);
    }


}
