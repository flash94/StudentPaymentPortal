package com.nwamara.studentportal.controller;

import com.nwamara.studentportal.Helper.Constant;
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

    @GetMapping("/profile/{studentId}")
    public String getStudentByStudentId(@PathVariable(name = "studentId", required = true) String studentId, Model model){
        //studentId = Constant.studentId;
        return studentService.findStudentById(studentId, model);
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

    @PostMapping("update/{student_id}")
    public String updateStudentByRegNumber(@PathVariable(name = "student_id", required = true)String studentId, @RequestParam String firstName,
                                           @RequestParam String lastName, Model model){
        CreateStudentDto dto = CreateStudentDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
        return studentService.updateStudentById(studentId, dto, model);
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
    public String checkStudentCanGraduate(@PathVariable(name = "reg_number", required = true)String regNumber, Model model){
        return  studentService.checkGraduationEligibility(regNumber, model);
    }

}
