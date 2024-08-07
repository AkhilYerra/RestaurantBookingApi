package com.example.resy.facade;

import com.example.resy.dao.ReservationDao;
import com.example.resy.dao.RestaurantDao;
import com.example.resy.data.Reservation;
import com.example.resy.data.Restaurant;
import com.example.resy.data.error.ReservationAlreadyExistsException;
import com.example.resy.data.filter.ReservationFilter;
import com.example.resy.data.request.CreateRequest;
import com.example.resy.data.request.SearchRequest;
import com.example.resy.transformers.SearchRequestToReservationFilterTransformer;
import com.example.resy.transformers.Transformer;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResyFacadeImpl implements ResyFacade{

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    RestaurantDao restaurantDao;

    private final Transformer<SearchRequest, ReservationFilter> reservationFilterTransformer = new SearchRequestToReservationFilterTransformer();

    @Override
    public List<Restaurant> searchForReservation(@RequestParam SearchRequest searchRequest) {

        if(!CollectionUtils.isEmpty(filterForReservations(reservationFilterTransformer.transformToB(searchRequest)))){
            throw new ReservationAlreadyExistsException("A reservation already exists at the specified time for the given users.");
        }
        //If All Users do not have an existing reservation at that time continue to find.
       return restaurantDao.findOpenRestaurantsAtTime(searchRequest);
    }

    @Override
    public void createReservation(Reservation reservation){
        Long createdId = reservationDao.createReservation(reservation);
        reservationDao.updateReservationUpdateTable(reservation.getUserIds(), createdId);
    }

    public List<Reservation> filterForReservations(ReservationFilter filter){
        return reservationDao.findReservations(filter);
    }
}
