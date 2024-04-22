package com.nwamara.studentportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class StudentDto {
    private String id;
    private String studentRegistrationNumber;
    private String lastName;
    private String firstName;
    private String middleName;
    private String department;
    private String email;
    private String userName;
    private Boolean isStudent;
}

