package com.bookingBirthday.bookingbirthdayforkids.exception;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseObj> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if ("id".equals(ex.getName()) && ex.getRequiredType() != null && Number.class.isAssignableFrom(ex.getRequiredType())) {
            Object value = ex.getValue();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), value.toString() + " is not a valid ID. Please enter a number", null));
        }
        return null;
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ResponseObj> handleMissingRequestPart(MissingServletRequestPartException ex) {
        String partName = ex.getRequestPartName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), partName + " is required and must not be null.", null));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseObj> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Error: Malformed JSON. Please provide valid JSON data.", null));
    }
}
