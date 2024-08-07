package com.example.resy.dao;

import com.example.resy.data.Reservation;
import com.example.resy.data.filter.ReservationFilter;
import com.example.resy.data.request.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ReservationDaoImpl implements ReservationDao{
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Reservation> findReservations(ReservationFilter filter){
        String query = "SELECT res.id AS reservationId, res.start_time AS timeStart, res.end_time AS timeEnd, res.table_id AS tableId, res.restaurant_id AS restaurantId, " +
                "GROUP_CONCAT(userRes.user_id) AS userIds " +
                "FROM reservation res " +
                "JOIN user_reservation userRes ON res.id = userRes.reservation_id " +
                "WHERE userRes.user_id IN (:userIds) " +
                "AND (res.start_time < :endTime " +
                "AND res.end_time > :startTime )" +
                "GROUP BY res.id " +
                "ORDER BY res.start_time ASC " +
                "LIMIT :limit OFFSET :offset";


        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userIds", filter.getUserIds())
                .addValue("startTime", filter.getStartTime())
                .addValue("endTime", filter.getEndTime())
                .addValue("limit", filter.getPageSize())
                .addValue("offset", (filter.getPageNumber() - 1) * filter.getPageSize());

        List<Map<String, Object>> results = jdbcTemplate.queryForList(query, params);

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

    @Override
    @Transactional
    public Long createReservation(Reservation reservation) {
        // Create the reservation
        String insertReservationQuery = "INSERT INTO Reservation (timeStart, timeEnd, tableId) VALUES (:startTime, :endTime, :tableId)";
        MapSqlParameterSource insertParams = new MapSqlParameterSource()
                .addValue("startTime", reservation.getStartTime())
                .addValue("endTime", reservation.getEndTime())
                .addValue("tableId", reservation.getTableId());

        //Get the created Id to update Reservation User Table.
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(insertReservationQuery, insertParams, keyHolder, new String[]{"id"});
        return Optional.of(keyHolder).map(KeyHolder::getKey).map(Number::longValue).orElse(null);
    }

    @Override
    public void updateReservationUpdateTable(List<Long> userIds, Long createdId){
        if(createdId != null){
            String insertUsersQuery = "INSERT INTO ReservationUsers (reservationId, userId) VALUES (:reservationId, :userId)";
            for (Long userId : userIds) {
                MapSqlParameterSource userParams = new MapSqlParameterSource()
                        .addValue("reservationId", createdId)
                        .addValue("userId", userId);
                jdbcTemplate.update(insertUsersQuery, userParams);
            }

        }
    }

    public List<Reservation> filterReservations(ReservationFilter filter){
        String checkQuery = "SELECT res.id as id, res.table_id as tableId, FROM Reservation res" +
                "WHERE tableId = :tableId " +
                "AND ((timeStart <= :startTime AND timeEnd > :startTime) " +
                "OR (timeStart < :endTime AND timeEnd >= :endTime) " +
                "OR (timeStart >= :startTime AND timeEnd <= :endTime)) " +
                "FOR UPDATE";

        //TODO: Change so filter has table Id and other fields
//        MapSqlParameterSource checkParams = new MapSqlParameterSource()
//                .addValue("tableId", tableId)
//                .addValue("startTime", startTime)
//                .addValue("endTime", endTime);

//        List<Reservation> count = jdbcTemplate.queryForList(checkQuery, checkParams, Reservation.class);
        return new ArrayList<>();

    }
}
