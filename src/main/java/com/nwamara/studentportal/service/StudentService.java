package com.nwamara.studentportal.service;

import com.nwamara.studentportal.dto.*;
import com.nwamara.studentportal.persistence.Student;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    public String create(CreateStudentDto createStudentDto, Model model);
    public StudentDto UpdateStudent(CreateStudentDto updateRequest);
    public StudentDto UpgradeStudentById(String id);
    public StudentDto updateStudentByRegNumber (String studentRegistrationNumber, CreateStudentDto updateRequest);
    public StudentDto fetch (String studentRegistrationNumber);
    public List<StudentDto> fetchStudents();
    public StudentDto delete (String studentRegistrationNumber);
    public String studentLogin(LoginDto loginRequest, Model model);
    public CreateStudentFinanceAccountResponse checkGraduationEligibility(String studentRegistrationNumber);
}
