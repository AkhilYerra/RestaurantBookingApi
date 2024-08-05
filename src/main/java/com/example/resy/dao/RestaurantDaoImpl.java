package com.example.resy.dao;

import com.example.resy.data.Restaurant;
import com.example.resy.data.request.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class RestaurantDaoImpl implements RestaurantDao{

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Restaurant> findOpenRestaurantsAtTime(SearchRequest request) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String restaurantQuery =
                "SELECT r.id as id, r.name as name, r." +
                        "FROM restaurants r " +
                        "JOIN restaurant_endorsements re ON r.id = re.restaurant_id " +
                        "JOIN tables t ON r.id = t.restaurant_id " +
                        "WHERE re.endorsement IN (:restrictions) " +
                        "  AND t.capacity >= :minimumGuests " +
                        "  AND t.id NOT IN ( " +
                        "      SELECT reservation.table_id " +
                        "      FROM reservations reservation " +
                        "      WHERE reservation_time BETWEEN :startTime AND :endTime " +
                        "  ) " +
                        "GROUP BY r.id, r.name";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(request.getReservationTime());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        Date endTime = calendar.getTime();

        params.addValue("restrictions", request.getDietaryRestrictions());
        params.addValue("minimumGuests", request.getMinimumGuests());
        params.addValue("startTime", request.getReservationTime());
        params.addValue("endTime", endTime);

        List<Map<String, Object>> rawSQLResults = jdbcTemplate.queryForList(restaurantQuery, params);

        List<Restaurant> restaurantList = new ArrayList<>();
        for (Map<String, Object> row : rawSQLResults) {
            Restaurant restaurant = new Restaurant();
            restaurant.setId(((Number) row.get("id")).longValue());
            restaurant.setName(((String) row.get("name")));

            restaurantList.add(restaurant);
        }
        return restaurantList;
    }
}
