package com.nwamara.studentportal.exception;

public class StudentWithRegNumberExistsException extends RuntimeException{
    public StudentWithRegNumberExistsException(String studentRegNumber) {
        super("Student with registration number: " + studentRegNumber + " already exists");
    }
}
