package com.example.resy.controller;

import com.example.resy.data.Reservation;
import com.example.resy.data.Restaurant;
import com.example.resy.data.request.CreateRequest;
import com.example.resy.data.request.SearchRequest;
import com.example.resy.facade.ResyFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ResyController {

    @Autowired
    ResyFacade resyFacade;

    @GetMapping("/reservation")
    public ResponseEntity<List<Restaurant>> getReservation(SearchRequest searchRequest) {
       return ResponseEntity.ok(resyFacade.searchForReservation(searchRequest));
    }

    @PostMapping("/reservation")
    public ResponseEntity<Void> createReservation(@RequestBody Reservation reservation) {
            resyFacade.createReservation(reservation);
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/reservation")
    public ResponseEntity<Void> deleteReservation(@RequestBody Long reservationId){
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
