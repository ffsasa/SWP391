package com.bookingBirthday.bookingbirthdayforkids.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Lỗi nhập entity:NotNull, Sai form email
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    //Lỗi nhập Id
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if ("id".equals(ex.getName()) && ex.getRequiredType() != null && Number.class.isAssignableFrom(ex.getRequiredType())) {
            return new ResponseEntity<>(String.format("Error: '%s' is not a valid ID. Please enter a number.", ex.getValue()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.format("The '%s' parameter must be of type '%s', but the value '%s' is provided.", ex.getName(), ex.getRequiredType().getSimpleName(), ex.getValue()), HttpStatus.BAD_REQUEST);
    }
}
