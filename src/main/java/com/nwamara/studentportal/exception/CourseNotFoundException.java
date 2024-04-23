package com.nwamara.studentportal.exception;

public class CourseNotFoundException extends RuntimeException{
    public CourseNotFoundException(String courseId) {
        super("Course not found");
    }
}
