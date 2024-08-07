package com.example.resy.data;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {
    private Long id;
    private Long tableId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<Long> userIds;
    private Long restaurantId;

    public void setId(long id) {
        this.id = id;
    }
    public Long getId(){
        return this.id;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
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
}
