package com.proxyseller.twitter.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class DefaultControllerAdvice {

    @ExceptionHandler(NoSuchElementException)
    ResponseEntity<Map<String, String>> handleException(NoSuchElementException e) {
        def response = new HashMap<>()
        response.put("error", "No such element by id")
        response.put("description", e.getMessage())
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PropertyNotFoundException)
    ResponseEntity<Map<String, String>> handleException(PropertyNotFoundException e) {
        def response = new HashMap<>()
        response.put("error", "Property not found by")
        response.put("description", "Property not found by id " + e.getMessage())
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception)
    ResponseEntity<Map<String, String>> handleException(Exception e) {
        def response = new HashMap<>()
        response.put("error", "Exception")
        response.put("description", e.getMessage())
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST)
    }
}
