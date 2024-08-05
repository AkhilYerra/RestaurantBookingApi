package com.example.resy.facade;

import com.example.resy.dao.ReservationDao;
import com.example.resy.dao.RestaurantDao;
import com.example.resy.data.Reservation;
import com.example.resy.data.Restaurant;
import com.example.resy.data.request.CreateRequest;
import com.example.resy.data.request.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResyFacadeImpl implements ResyFacade{

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Override
    public List<Restaurant> searchForReservation(SearchRequest searchRequest) {
        if(!CollectionUtils.isEmpty(reservationDao.findExistingReservationsForUsers(searchRequest))){
            //TODO: Throw Exception for FE to handle that reservation already exists?
        }
        //If All Users do not have an existing reservation at that time continue to find.
       return restaurantDao.findOpenRestaurantsAtTime(searchRequest);
    }

    @Override
    public void createReservation(Reservation reservation){
        Long createdId = reservationDao.createReservation(reservation);
        reservationDao.updateReservationUpdateTable(reservation.getUserIds(), createdId);
    }
}
