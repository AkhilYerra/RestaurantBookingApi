package com.example.resy.data.request;

import com.example.resy.data.DietaryRestriction;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

public class SearchRequest {
    Set<Long> userIds;
    private int minimumGuests;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private Date reservationTime;
    Set<DietaryRestriction> dietaryRestrictions;
    private Integer pageNumber;
    private Integer pageSize;


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
        return this.reservationTime;
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

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
