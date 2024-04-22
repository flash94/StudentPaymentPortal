package com.nwamara.studentportal.dao;

import com.nwamara.studentportal.persistence.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {
    //public List<Student> findByStudentRegNumber(String studentRegistrationNumber);
    public Student findByStudentRegistrationNumber(String studentRegistrationNumber);
    public Student findByUserName(String userName);
}
