package com.example.resy.data.request;

import com.example.resy.data.DietaryRestriction;

import java.util.Date;
import java.util.Set;

public class SearchRequest {
    Set<Long> userIds;
    private int minimumGuests;
    private Date reservationTime;
    Set<DietaryRestriction> dietaryRestrictions;

    public Set<Long> getUserIds(){
        return userIds;
    }

    public void setUserIds(Set<Long> userIds){
        this.userIds = userIds;
    }

    public int getMinimumGuests(){
        return minimumGuests;
    }

    public void setMinimumGuests(int minimumGuests){
        this.minimumGuests = minimumGuests;
    }

    public Date getReservationTime(){
        return reservationTime;
    }

    public void setReservationTime(Date reservationTime){
        this.reservationTime = reservationTime;
    }

    public Set<DietaryRestriction> getDietaryRestrictions(){
        return this.dietaryRestrictions;
    }

    public void setDietaryRestrictions(Set<DietaryRestriction> restrictions){
        this.dietaryRestrictions = restrictions;
    }

}
