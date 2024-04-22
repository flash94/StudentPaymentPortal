package com.nwamara.studentportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EnrollmentResult {
    private String studentId;
    private String enrollmentId;
    private String courseId;
    private String invoiceReference;

    // Constructors, getters, and setters
}
