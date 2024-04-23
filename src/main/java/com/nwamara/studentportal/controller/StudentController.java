package com.nwamara.studentportal.controller;

import com.nwamara.studentportal.dto.*;
import com.nwamara.studentportal.service.StudentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
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

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("createStudentDto", new CreateStudentDto());
        return "register";
    }
    @PostMapping("/register")
    public String createStudent(@ModelAttribute("createStudentDto") @Valid CreateStudentDto createStudentDto,Model model){
        return studentService.create(createStudentDto, model);
    }

    @PutMapping("/{reg_number}")
    public StudentDto updateStudentByRegNumber(@PathVariable(name = "reg_number", required = true)String regNumber, @RequestBody @Valid CreateStudentDto dto){
        return studentService.updateStudentByRegNumber(regNumber, dto);
    }

    @PutMapping("upgrade/{id}")
    public StudentDto updateStudentByRegNumber(@PathVariable(name = "id", required = true)String id){
        return studentService.UpgradeStudentById(id);
    }

    @GetMapping({"", "/login", "/"})
    public String showLoginForm(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String loginStudent(@ModelAttribute("loginDto") @Valid LoginDto dto, Model model){
        return studentService.studentLogin(dto, model);
    }

    @GetMapping("/graduate/{reg_number}")
    public CreateStudentFinanceAccountResponse checkStudentCanGraduate(@PathVariable(name = "reg_number", required = true)String regNumber){
        return  studentService.checkGraduationEligibility(regNumber);
    }

}
