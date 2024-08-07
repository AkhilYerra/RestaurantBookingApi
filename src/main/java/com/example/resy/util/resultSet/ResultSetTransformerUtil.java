package com.example.resy.util.resultSet;

import com.example.resy.data.Reservation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class ResultSetTransformerUtil {

    public static List<Reservation> transformToReservations(List<Map<String, Object>> results){
        List<Reservation> reservationList = new ArrayList<>();
        for (Map<String, Object> row : results) {
            Reservation reservation = new Reservation();
            reservation.setId(((Number) row.get("reservationId")).longValue());
            reservation.setRestaurantId(((Number) row.get("restaurantId")).longValue());
            reservation.setTableId(((Number) row.get("tableId")).longValue());
            LocalDateTime localDateTime = (LocalDateTime) row.get("timeStart");
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            Date startTime = Date.from(instant);
            reservation.setStartTime(startTime);

            // Fetch and add the user IDs associated with this reservation
            String userIdsString = (String) row.get("userIds");
            List<Long> userIdsList = userIdsString != null ?
                    Arrays.stream(userIdsString.split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList())
                    : new ArrayList<>();
            reservation.setUserIds(userIdsList);

            reservationList.add(reservation);
        }
        return reservationList;
    }
}
