package com.example.restservice.advice;

import com.example.restservice.exceptions.NameConflictException;
import com.example.restservice.exceptions.NotFoundException;
import com.example.restservice.exceptions.SaveFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GreetingExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleResourceNotFound() {}

    @ExceptionHandler(SaveFailedException.class)
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    public void handleSaveFailed() {}

    @ExceptionHandler(NameConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleNameConflict() {}
}
