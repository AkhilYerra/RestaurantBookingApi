package com.example.resy.data.filter;

import com.example.resy.util.date.DateUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ReservationFilter {
    private List<Long> reservationIds;
    private Set<Long> userIds;
    private Set<Long> tableIds;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
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

    @Override
    public int hashCode() {
        return Objects.hash(reservationIds, userIds, tableIds, startTime, endTime, restaurantIds, pageNumber, pageSize);
    }

    @Override
    public String toString() {
        return "ReservationFilter{" +
                "reservationIds=" + reservationIds +
                ", userIds=" + userIds +
                ", tableIds=" + tableIds +
                ", startTime=" + (startTime != null ? DateUtil.getFormattedDateTime(startTime) : null) +
                ", endTime=" + (endTime != null ? DateUtil.getFormattedDateTime(endTime) : null) +
                ", restaurantIds=" + restaurantIds +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                '}';
    }
}
