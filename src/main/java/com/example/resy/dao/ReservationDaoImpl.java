package com.example.resy.dao;

import com.example.resy.data.Reservation;
import com.example.resy.data.filter.ReservationFilter;
import com.example.resy.data.request.SearchRequest;
import com.example.resy.util.resultSet.ResultSetTransformerUtil;
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

    @Override
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
        return ResultSetTransformerUtil.transformToReservations(results);
        }

    @Override
    public List<Reservation> filterReservations(ReservationFilter filter){
        String query = "SELECT res.id AS reservationId, res.start_time AS timeStart, res.end_time AS timeEnd, res.table_id AS tableId, res.restaurant_id AS restaurantId " +
                "FROM reservation res " +
                "WHERE res.table_id IN (:tableIds) " +
                "AND restaurant_id IN (:restaurantIds) " +
                "AND (res.start_time < :endTime " +
                "AND res.end_time > :startTime )" +
                "ORDER BY res.start_time ASC " +
                "LIMIT :limit OFFSET :offset";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("restaurantIds", filter.getRestaurantIds().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")))
                .addValue("tableIds", filter.getTableIds().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")))
                .addValue("startTime", filter.getStartTime())
                .addValue("endTime", filter.getEndTime())
                .addValue("limit", filter.getPageSize())
                .addValue("offset", (filter.getPageNumber() - 1) * filter.getPageSize());

        List<Map<String, Object>> results = jdbcTemplate.queryForList(query, params);
        return ResultSetTransformerUtil.transformToReservations(results);
    }

    @Override
    @Transactional
    public Long createReservation(Reservation reservation) {
        // Create the reservation
        String insertReservationQuery = "INSERT INTO reservation (start_time, end_time, table_id, restaurant_id) VALUES (:startTime, :endTime, :tableId, :restaurantId)";
        MapSqlParameterSource insertParams = new MapSqlParameterSource()
                .addValue("startTime", reservation.getStartTime())
                .addValue("endTime", reservation.getEndTime())
                .addValue("tableId", reservation.getTableId())
                .addValue("restaurantId", reservation.getRestaurantId());

        //Get the created Id to update Reservation User Table.
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(insertReservationQuery, insertParams, keyHolder, new String[]{"id"});
        return Optional.of(keyHolder).map(KeyHolder::getKey).map(Number::longValue).orElse(null);
    }

    @Override
    public void updateReservationUpdateTable(List<Long> userIds, Long createdId){
        if(createdId != null){
            String insertUsersQuery = "INSERT INTO user_reservation (reservation_id, user_id) VALUES (:reservationId, :userId)";

            List<MapSqlParameterSource> batchArgs = userIds.stream()
                    .map(userId -> new MapSqlParameterSource()
                            .addValue("reservationId", createdId)
                            .addValue("userId", userId))
                    .collect(Collectors.toList());

            jdbcTemplate.batchUpdate(insertUsersQuery, batchArgs.toArray(new MapSqlParameterSource[0]));

        }
    }

    @Override
    public void deleteReservation(Long id) {
        String DELETE_RESERVATION_QUERY = "DELETE FROM reservation WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(DELETE_RESERVATION_QUERY, params);
    }

    @Override
    public void deleteUserReservations(Long id) {
        String DELETE_RESERVATION_QUERY = "DELETE FROM user_reservation WHERE reservation_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(DELETE_RESERVATION_QUERY, params);
    }
}
