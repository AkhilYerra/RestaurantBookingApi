package com.example.resy.dao;

import com.example.resy.data.Reservation;
import com.example.resy.data.filter.ReservationFilter;
import com.example.resy.data.request.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationDaoImpl implements ReservationDao{
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Reservation> findExistingReservationsForUsers(SearchRequest request){
            String query = "SELECT r.id AS reservationId, r.timeStart as timeStart, r.timeEnd as timeEnd, r.tableId as tableId, r.restaurantId as restaurantId" +
                "GROUP_CONCAT(ru.userId) AS userIds " +
                "FROM Reservation r " +
                "JOIN ReservationUsers ru ON r.id = ru.reservationId " +
                "WHERE ru.userId IN (:userIds) " +
                "AND r.timeStart >= :startTime " +
                "AND r.timeEnd <= :endTime ";

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("userIds", request.getUserIds())
                    .addValue("startTime", request.getReservationTime())
                    .addValue("endTime", request.getReservationTime());

            List<Map<String, Object>> results = jdbcTemplate.queryForList(query, params);

            List<Reservation> reservationList = new ArrayList<>();
            for (Map<String, Object> row : results) {
                Reservation reservation = new Reservation();
                reservation.setId(((Number) row.get("reservationId")).longValue());
                reservation.setRestaurantId(((Number) row.get("restaurantId")).longValue());
                reservation.setTableId(((Number) row.get("tableId")).longValue());
                reservation.setStartTime((Date) row.get("timeStart"));

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
