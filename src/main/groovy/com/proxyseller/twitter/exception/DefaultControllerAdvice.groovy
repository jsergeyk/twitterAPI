package com.proxyseller.twitter.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class DefaultControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<Map<String, String>> handleException(NoSuchElementException e) {
        Map<String, String> response = new HashMap<>()
        response.put("error", "No such element by id")
        response.put("description", e.getMessage())
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Map<String, String>> handleException(Exception e) {
        Map<String, String> response = new HashMap<>()
        response.put("error", "Exception")
        response.put("description", e.getMessage())
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST)
    }
}
