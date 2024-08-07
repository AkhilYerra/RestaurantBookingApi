package com.example.resy.facade;

import com.example.resy.dao.ReservationDao;
import com.example.resy.dao.RestaurantDao;
import com.example.resy.data.Reservation;
import com.example.resy.data.Restaurant;
import com.example.resy.data.error.ReservationAlreadyExistsException;
import com.example.resy.data.error.ReservationLockException;
import com.example.resy.data.filter.ReservationFilter;
import com.example.resy.data.request.CreateRequest;
import com.example.resy.data.request.SearchRequest;
import com.example.resy.transformers.SearchRequestToReservationFilterTransformer;
import com.example.resy.transformers.Transformer;
import com.example.resy.util.date.DateUtil;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class ResyFacadeImpl implements ResyFacade{

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    RedissonClient redissonClient;


    private final Transformer<SearchRequest, ReservationFilter> reservationFilterTransformer = new SearchRequestToReservationFilterTransformer();

    @Override
    public List<Restaurant> searchForReservation(@RequestParam SearchRequest searchRequest) {

        if(!CollectionUtils.isEmpty(filterForReservations(reservationFilterTransformer.transformToB(searchRequest)))){
            throw new ReservationAlreadyExistsException("A reservation already exists at the specified time for the given users.");
        }
        //If All Users do not have an existing reservation at that time continue to find.
       return restaurantDao.findOpenRestaurantsAtTime(searchRequest);
    }

    @Override
    @Transactional
    public void createReservation(Reservation reservation) throws InterruptedException {
        String lockKey = "reservationLock:" + reservation.getRestaurantId() + ":" + reservation.getTableId();

        if (acquireLock(lockKey, 25, 5)) {
            try {
                boolean available = isTimeRangeAvailable(reservation.getRestaurantId(), reservation.getTableId(), reservation.getStartTime(), reservation.getEndTime());
                if (!available) {
                    throw new ReservationAlreadyExistsException("The time range is not available.");
                }

                if (reservation.getEndTime() == null) {
                    // TODO: Defaulting behavior Remove this
                    Date date = DateUtil.createDate("2024-07-24 12:15:00");
                    reservation.setStartTime(date);
                    Date endTime = DateUtil.addTwoHours(reservation.getStartTime());
                    reservation.setEndTime(endTime);
                }

                Long createdId = reservationDao.createReservation(reservation);
                reservationDao.updateReservationUpdateTable(reservation.getUserIds(), createdId);
            } catch (Exception e) {
                throw e; // Rethrow the exception to trigger rollback
            } finally {
                redissonClient.getLock(lockKey).unlock();
            }
        } else {
            throw new ReservationLockException("Unable to acquire lock for reservation.");
        }
    }

    public List<Reservation> filterForReservations(ReservationFilter filter){
        return reservationDao.findReservations(filter);
    }

    private boolean acquireLock(String lockKey, long waitTime, long leaseTime) throws InterruptedException {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
    }

    private boolean isTimeRangeAvailable(Long restaurantId, Long tableId, Date startTime, Date endTime) {
        // TODO: Implement the logic to check if the time range is available
        return true; // Placeholder
    }
}
