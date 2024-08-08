package com.example.resy.controller;

import com.example.resy.data.error.GenericException;
import com.example.resy.data.error.ReservationAlreadyExistsException;
import com.example.resy.data.error.ReservationLockException;
import com.example.resy.data.error.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReservationAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<GenericException> handleReservationAlreadyExistsException(ReservationAlreadyExistsException ex) {
        GenericException exception = new GenericException(ex.getMessage(), HttpStatus.CONFLICT.value(), "ReservationAlreadyExistsException");
        return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ReservationLockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<GenericException> handleReservationAlreadyExistsException(ReservationLockException ex) {
        GenericException exception = new GenericException(ex.getMessage(), HttpStatus.LOCKED.value(), "ReservationLockException");
        return new ResponseEntity<>(exception, HttpStatus.LOCKED);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<GenericException> handleReservationAlreadyExistsException(ValidationException ex) {
        GenericException exception = new GenericException(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), "ValidationException");
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

}

