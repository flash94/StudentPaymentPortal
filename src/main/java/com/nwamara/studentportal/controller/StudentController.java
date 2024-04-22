package com.nwamara.studentportal.controller;

import com.nwamara.studentportal.dto.*;
import com.nwamara.studentportal.service.StudentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("")
    public Iterable<StudentDto> getStudents(){
        return studentService.fetchStudents();
    }

    @GetMapping("/{reg_number}")
    public StudentDto getStudentByRegNumber(@PathVariable(name = "reg_number", required = true)String regNumber){
        return studentService.fetch(regNumber);
    }

    @PostMapping("")
    public StudentDto createStudent(@RequestBody @Valid CreateStudentDto dto){
        return studentService.create(dto);
    }

    @PutMapping("/{reg_number}")
    public StudentDto updateStudentByRegNumber(@PathVariable(name = "reg_number", required = true)String regNumber, @RequestBody @Valid CreateStudentDto dto){
        return studentService.updateStudentByRegNumber(regNumber, dto);
    }

    @PutMapping("upgrade/{id}")
    public StudentDto updateStudentByRegNumber(@PathVariable(name = "id", required = true)String id){
        return studentService.UpgradeStudentById(id);
    }

    @PostMapping("/login")
    public LoginMessage loginStudent(@RequestBody @Valid LoginDto dto){
        return  studentService.studentLogin(dto);
    }

    @GetMapping("/graduate/{reg_number}")
    public CreateStudentFinanceAccountResponse checkStudentCanGraduate(@PathVariable(name = "reg_number", required = true)String regNumber){
        return  studentService.checkGraduationEligibility(regNumber);
    }

}
