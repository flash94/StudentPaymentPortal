package com.nwamara.studentportal.dao;

import com.nwamara.studentportal.persistence.Course;
import com.nwamara.studentportal.persistence.StudentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, String> {

    // Custom query method to find courses by student id
    @Query("SELECT se.course FROM StudentEnrollment se WHERE se.student.id = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") String studentId);
    StudentEnrollment findByStudentIdAndCourseId (String studentId, String courseId);

    // You can add more custom query methods as needed
}
