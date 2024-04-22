package com.nwamara.studentportal.exception;

public class StudentNotFoundException extends RuntimeException{
    public StudentNotFoundException(String studentRegNumber) {
        super("Student with registration number: " + studentRegNumber + " not found");
    }
}
