package com.example.resy.dao;

import com.example.resy.data.DietaryRestriction;
import com.example.resy.data.Restaurant;
import com.example.resy.data.Table;
import com.example.resy.data.request.SearchRequest;
import com.example.resy.util.date.DateUtil;
import com.example.resy.util.resultSet.ResultSetTransformerUtil;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RestaurantDaoImpl implements RestaurantDao{

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Restaurant> findOpenRestaurantsAtTime(SearchRequest request) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        int offset = (request.getPageNumber() -1) * request.getPageSize();

        //We could expand functionality later to order restaurants by popularity
        // or some form of popularity and rating so the better ones show up first

        //In the future we can build out some more dynamic query builder so we dont have to do this.
        String restaurantQuery = "SELECT r.id as id, r.name as name, t.id as tableId, t.capacity AS tableCapacity , GROUP_CONCAT(DISTINCT re.endorsement) AS endorsements ";
        restaurantQuery += " FROM restaurant r ";
        restaurantQuery += " LEFT JOIN restaurant_endorsement re ON r.id = re.restaurant_id ";
        restaurantQuery += " JOIN restaurant_table t ON r.id = t.restaurant_id WHERE 1 = 1";
        restaurantQuery +=     "  AND t.capacity >= :minimumGuests " +
                "  AND t.id NOT IN ( " +
                "      SELECT reservation.table_id " +
                "      FROM reservation reservation " +
                "     WHERE (reservation.start_time < :endTime AND reservation.end_time > :startTime)  " +
                "  ) " +
                "GROUP BY t.id " +
                "ORDER BY r.name ASC " +
                "LIMIT :limit OFFSET :offset";

        //We are hard coding 2 hour reservation time, we can update in the future to allow the Table object to
        // include time for table reservation as larger tables require more time while smaller tables done
        LocalDateTime endTime = DateUtil.addTwoHours(request.getReservationTime());
        //Considering we will be calling this a lot it makes sense to add a combined index startTime, endtime
        // For example : CREATE INDEX openResyTimeIdx ON restaurant (start_time, end_time);
        params.addValue("minimumGuests", request.getMinimumGuests());
        params.addValue("startTime", request.getReservationTime());
        params.addValue("endTime", endTime);
        params.addValue("limit", request.getPageSize());
        params.addValue("offset", offset);

        List<Map<String, Object>> rawSQLResults = jdbcTemplate.queryForList(restaurantQuery, params);


        List<Restaurant> restaurantList = ResultSetTransformerUtil.transformToRestaurant(rawSQLResults);

        if(CollectionUtils.isEmpty(restaurantList)){
            return new ArrayList<>();
        }
        List<Restaurant> filteredRestaurants = restaurantList.stream()
                .filter(restaurant -> {
                    if(CollectionUtils.isEmpty(request.getDietaryRestrictions())){
                        return true;
                    }else{
                        return restaurant.getEndorsements().containsAll(request.getDietaryRestrictions());
                    }
                })
                .collect(Collectors.toList());
        return filteredRestaurants;

    }
}
