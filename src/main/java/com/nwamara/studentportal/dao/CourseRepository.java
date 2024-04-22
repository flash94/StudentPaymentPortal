package com.nwamara.studentportal.dao;

import com.nwamara.studentportal.persistence.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
    // You can add custom query methods if needed
}
