package com.example.resy.data.filter;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class ReservationFilter {
    private List<Long> reservationIds;
    private Set<Long> userIds;
    private Set<Long> tableIds;
    private Date startTime;
    private Date endTime;
    private List<Long> restaurantIds;
    private Integer pageNumber;
    private Integer pageSize;


    public List<Long> getReservationIds() {
        return reservationIds;
    }

    public void setReservationIds(List<Long> reservationIds) {
        this.reservationIds = reservationIds;
    }

    public Set<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Long> userIds) {
        this.userIds = userIds;
    }

    public Set<Long> getTableIds() {
        return tableIds;
    }

    public void setTableIds(Set<Long> tableIds) {
        this.tableIds = tableIds;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Long> getRestaurantIds() {
        return restaurantIds;
    }

    public void setRestaurantIds(List<Long> restaurantIds) {
        this.restaurantIds = restaurantIds;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
