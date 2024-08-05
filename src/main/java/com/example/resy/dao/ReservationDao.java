package com.example.resy.dao;

import com.example.resy.data.Reservation;
import com.example.resy.data.filter.ReservationFilter;
import com.example.resy.data.request.CreateRequest;
import com.example.resy.data.request.SearchRequest;
import java.util.List;

public interface ReservationDao {
    List<Reservation> findReservations(ReservationFilter filter);

    Long createReservation(Reservation reservation);

    void updateReservationUpdateTable(List<Long> userIds, Long createdId);
}
