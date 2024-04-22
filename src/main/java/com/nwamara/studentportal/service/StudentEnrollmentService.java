package com.nwamara.studentportal.service;

import com.nwamara.studentportal.dto.CreateStudentDto;
import com.nwamara.studentportal.dto.EnrollmentResult;
import com.nwamara.studentportal.dto.StudentDto;
import com.nwamara.studentportal.persistence.Course;

import java.util.List;

public interface StudentEnrollmentService {
    public EnrollmentResult enrollStudentInCourse(String id, Course course);
    public List<Course> fetchStudentEnrolledCourses(String id);
}
