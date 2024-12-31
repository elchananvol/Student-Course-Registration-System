package com.system.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserBaseException.class)
    public ResponseEntity<Map<String, Object>> handleBaseException(UserBaseException ex) {
        logger.info("User exception occurred: {}", ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getErrorCode());
        response.put("details", ex.getMessage());
        return new ResponseEntity<>(response, ex.getErrorCode());
    }
    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(Exception ex) {
        logger.error("Database error occurred: {}", ex.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("error", HttpStatus.BAD_REQUEST.toString());
        response.put("details", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        logger.error("An server error occurred: ", ex);
        Map<String, Object> response = new HashMap<>();
        response.put("error", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        response.put("details", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}