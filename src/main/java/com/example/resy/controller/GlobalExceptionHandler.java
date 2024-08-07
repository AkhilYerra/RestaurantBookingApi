package com.example.resy.controller;

import com.example.resy.data.error.GenericException;
import com.example.resy.data.error.ReservationAlreadyExistsException;
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

}

