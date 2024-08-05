package com.example.resy.dao;

import com.example.resy.data.Reservation;
import com.example.resy.data.request.CreateRequest;
import com.example.resy.data.request.SearchRequest;
import java.util.List;

public interface ReservationDao {
    List<Reservation> findExistingReservationsForUsers(SearchRequest request);

    Long createReservation(Reservation reservation);

    void updateReservationUpdateTable(List<Long> userIds, Long createdId);
}
