package com.example.resy.controller;

import com.example.resy.data.Reservation;
import com.example.resy.data.Restaurant;
import com.example.resy.data.request.CreateRequest;
import com.example.resy.data.request.SearchRequest;
import com.example.resy.facade.ResyFacade;
import com.example.resy.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ResyController {

    @Autowired
    ResyFacade resyFacade;

    @GetMapping("/reservation")
    public ResponseEntity<List<Restaurant>> getReservation(SearchRequest searchRequest) {
        try{
            RequestValidator.isSearchRequestValid(searchRequest);
            return ResponseEntity.ok(resyFacade.searchForReservation(searchRequest));
        }catch (Exception err){
            throw err;
        }
    }

    @PostMapping("/reservation")
    public ResponseEntity<Void> createReservation(@RequestBody Reservation reservation) {
        try {
            RequestValidator.isReservationCreateValid(reservation);
            resyFacade.createReservation(reservation);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/reservation/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        //Would need some kind of authorization test here. Ensure User A cant delete User B resy.
        resyFacade.deleteReservation(reservationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Need a basic filter call for reservations for a specific user for FE to use at some point.
    // Possible we can leverage the already existing getReservation and make the SQL query more dynamic so we can search only based on user id.

}
