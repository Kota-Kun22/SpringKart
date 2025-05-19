package org.example.springkart.project.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String,String> error= new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(err ->{

            String fieldName= ((FieldError) err).getField();

            String errorMessage= err.getDefaultMessage();

            error.put(fieldName,errorMessage);

        });
        return new ResponseEntity<Map<String,String>>(error, HttpStatus.BAD_REQUEST);
    }
}
