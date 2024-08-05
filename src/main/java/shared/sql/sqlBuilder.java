package shared.sql;

import com.example.resy.data.User;
import com.example.resy.data.request.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class sqlBuilder {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> searchForReservation(SearchRequest request) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String restaurantQuery =
                "SELECT r.id as id, r.name as name" +
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

        return jdbcTemplate.queryForList(restaurantQuery, params);
    }
}
