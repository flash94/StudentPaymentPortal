package com.nwamara.studentportal.service;

import com.nwamara.studentportal.dto.*;
import com.nwamara.studentportal.persistence.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    public StudentDto create(CreateStudentDto createStudentDto);
    public StudentDto UpdateStudent(CreateStudentDto updateRequest);
    public StudentDto UpgradeStudentById(String id);
    public StudentDto updateStudentByRegNumber (String studentRegistrationNumber, CreateStudentDto updateRequest);
    public StudentDto fetch (String studentRegistrationNumber);
    public List<StudentDto> fetchStudents();
    public StudentDto delete (String studentRegistrationNumber);
    public LoginMessage studentLogin(LoginDto loginRequest);
    public CreateStudentFinanceAccountResponse checkGraduationEligibility(String studentRegistrationNumber);
}
