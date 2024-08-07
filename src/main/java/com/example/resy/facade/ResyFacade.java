package com.example.resy.facade;
import com.example.resy.data.Reservation;
import com.example.resy.data.Restaurant;
import com.example.resy.data.request.SearchRequest;

import java.util.List;

public interface ResyFacade {

    List<Restaurant> searchForReservation(SearchRequest searchRequest);

    void createReservation(Reservation reservation) throws InterruptedException;
}
