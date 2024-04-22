package com.nwamara.studentportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StudentWithRegNumberExistsAdvice {
    /**
     * ResponseBody signals that this advice is rendered straight into the response body.     *
     * ExceptionHandler configures the advice to only respond if an AccountNotFoundException is thrown.     *
     * ResponseStatus says to issue an HttpStatus.NOT_FOUND, i.e. an HTTP 404.
     *
     * The body of the advice generates the content.
     */

    @ResponseBody
    @ExceptionHandler(StudentWithRegNumberExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String studentWithRegNumberExistsHandler(StudentWithRegNumberExistsException ex) {
        return ex.getMessage();
    }
}
