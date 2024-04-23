package com.nwamara.studentportal.service;

import com.nwamara.studentportal.dto.StudentDto;
import com.nwamara.studentportal.persistence.Course;
import org.springframework.ui.Model;

import java.util.List;

public interface StudentCourseService {
    public String fetchCourses(Model model);
    public String fetchCourseById(Model model, String courseId);
}
