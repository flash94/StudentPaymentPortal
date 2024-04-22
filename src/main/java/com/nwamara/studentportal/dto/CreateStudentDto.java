package com.nwamara.studentportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateStudentDto {
    private String id;
    private String studentRegistrationNumber;
    private String userName;
    private Boolean isStudent;
    private String lastName;
    private String firstName;
    private String middleName;
    private String department;
    private String email;
    private String password;
}
