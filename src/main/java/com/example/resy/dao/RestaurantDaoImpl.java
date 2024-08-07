package com.example.resy.dao;

import com.example.resy.data.DietaryRestriction;
import com.example.resy.data.Restaurant;
import com.example.resy.data.Table;
import com.example.resy.data.request.SearchRequest;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(request.getReservationTime());
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        Date endTime = calendar.getTime();
        params.addValue("minimumGuests", request.getMinimumGuests());
        params.addValue("startTime", request.getReservationTime());
        params.addValue("endTime", endTime);
        params.addValue("limit", request.getPageSize());
        params.addValue("offset", offset);

        List<Map<String, Object>> rawSQLResults = jdbcTemplate.queryForList(restaurantQuery, params);

        Map<Long, Restaurant> restaurantMap = new HashMap<>();
        List<Restaurant> restaurantList = new ArrayList<>();
        for (Map<String, Object> row : rawSQLResults) {
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
        if(CollectionUtils.isEmpty(restaurantMap.values())){
            return new ArrayList<>();
        }
        List<Restaurant> filteredRestaurants = restaurantMap.values().stream()
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
