package com.example.resy.util.resultSet;

import com.example.resy.data.DietaryRestriction;
import com.example.resy.data.Reservation;
import com.example.resy.data.Restaurant;
import com.example.resy.data.Table;

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

    public static List<Restaurant> transformToRestaurant(List<Map<String, Object>> resultSet){
        Map<Long, Restaurant> restaurantMap = new HashMap<>();
        List<Restaurant> restaurantList = new ArrayList<>();
        for (Map<String, Object> row : resultSet) {
            Long restaurantId = ((Number) row.get("id")).longValue();
            if(restaurantMap.get(restaurantId) == null){
                Restaurant restaurant = new Restaurant();
                restaurant.setId(((Number) row.get("id")).longValue());
                restaurant.setName((String) row.get("name"));
                String endorsementString = (String) row.get("endorsements");
                Set<DietaryRestriction> endorsementList = endorsementString != null ?
                        Arrays.stream(endorsementString.split(","))
                                .map(DietaryRestriction::valueOf)
                                .collect(Collectors.toSet())
                        : new HashSet<>();
                restaurant.setEndorsements(endorsementList);
                List<Table> tables = new ArrayList<>();
                Table table = new Table();
                table.setRestaurant_id(restaurantId);
                table.setId(((Number) row.get("tableId")).longValue());
                table.setCapacity(((Number) row.get("tableCapacity")).intValue());
                tables.add(table);
                restaurant.setTableList(tables);
                restaurantMap.put(restaurantId, restaurant);
            }else{
                Table table = new Table();
                table.setRestaurant_id(restaurantId);
                table.setId(((Number) row.get("tableId")).longValue());
                table.setCapacity(((Number) row.get("tableCapacity")).intValue());
                restaurantMap.get(restaurantId).getTableList().add(table);
            }
        }
        return restaurantMap.values().stream().collect(Collectors.toList());
    }
}
