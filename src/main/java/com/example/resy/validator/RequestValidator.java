package com.example.resy.validator;


import com.example.resy.data.Reservation;
import com.example.resy.data.error.ValidationException;
import com.example.resy.data.request.SearchRequest;
import org.springframework.util.CollectionUtils;

public class RequestValidator {


    public static void isSearchRequestValid(SearchRequest request){
        if(CollectionUtils.isEmpty(request.getUserIds())){
            throw new ValidationException("User Id is Required to Search For Available Restaurants");
        }
        if(request.getReservationTime() == null){
            throw new ValidationException("Reservation Time is Required to Search For Available Restaurants");
        }
        if(request.getMinimumGuests() == null){
            throw new ValidationException("Minimum Guests is Required to Search For Available Restaurants");
        }
    }

    public static void isReservationCreateValid(Reservation reservation){
        if(CollectionUtils.isEmpty(reservation.getUserIds())){
            throw new ValidationException("User Id is Required to Search For Available Restaurants");
        }
        if(reservation.getStartTime() == null){
            throw new ValidationException("Start Reservation Time is Required to Search For Available Restaurants");
        }
        if(reservation.getRestaurantId() == null){
            throw new ValidationException("Restaurant Id is Required to Search For Available Restaurants");
        }
        if(reservation.getTableId() == null){
            throw new ValidationException("Table Id is Required to Search For Available Restaurants");
        }
    }
}
