package com.example.resy.facade;

import com.example.resy.dao.ReservationDao;
import com.example.resy.dao.RestaurantDao;
import com.example.resy.data.Reservation;
import com.example.resy.data.Restaurant;
import com.example.resy.data.error.ReservationAlreadyExistsException;
import com.example.resy.data.error.ReservationLockException;
import com.example.resy.data.filter.ReservationFilter;
import com.example.resy.data.request.SearchRequest;
import com.example.resy.shared.SimpleCache;
import com.example.resy.transformers.SearchRequestToReservationFilterTransformer;
import com.example.resy.transformers.Transformer;
import com.example.resy.util.date.DateUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ResyFacadeImpl implements ResyFacade{

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    RedissonClient redissonClient;

    private final SimpleCache<Integer, List<Reservation>> reservationCache = new SimpleCache<>();


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

        if (acquireLock(lockKey)) {
            try {
                boolean available = isTimeRangeAvailable(reservation.getRestaurantId(), reservation.getTableId(), reservation.getStartTime(), reservation.getEndTime());
                if (!available) {
                    throw new ReservationAlreadyExistsException("The time range is not available.");
                }

                if (reservation.getEndTime() == null) {
                    reservation.setStartTime(reservation.getStartTime());
                    LocalDateTime endTime = DateUtil.addTwoHours(reservation.getStartTime());
                    reservation.setEndTime(endTime);
                }

                Long createdId = reservationDao.createReservation(reservation);
                reservationDao.updateReservationUpdateTable(reservation.getUserIds(), createdId);
                reservationCache.evictAll();
            } catch (Exception e) {
                throw e; // Rethrow the exception to trigger rollback
            } finally {
                redissonClient.getLock(lockKey).unlock();
            }
        } else {
            throw new ReservationLockException("Unable to acquire lock for reservation.");
        }
    }

    @Override
    @Transactional
    public void deleteReservation(Long reservationId) {
        try{
            reservationDao.deleteUserReservations(reservationId);
            reservationDao.deleteReservation(reservationId);
            reservationCache.evictAll();
        }catch (Exception e){
            throw e;
        }
    }

    public List<Reservation> filterForReservations(ReservationFilter filter){
        int cacheKey = buildCacheKey(filter);
        if(reservationCache.get(cacheKey) != null){
            return reservationCache.get(cacheKey);
        }
        List<Reservation> reservations = reservationDao.findReservations(filter);
        reservationCache.put(cacheKey, reservations);
        return reservations;
    }

    private int buildCacheKey(ReservationFilter filter){
      return filter.hashCode();
    }

    private boolean acquireLock(String lockKey) throws InterruptedException {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.tryLock(60, 10, TimeUnit.SECONDS);
    }

    private boolean isTimeRangeAvailable(Long restaurantId, Long tableId, LocalDateTime startTime, LocalDateTime endTime) {
        ReservationFilter filter = new ReservationFilter();
        filter.setPageSize(10);
        filter.setPageSize(1);
        filter.setStartTime(startTime);
        filter.setStartTime(endTime);
        filter.setRestaurantIds(Collections.singletonList(restaurantId));
        filter.setTableIds(Collections.singleton(tableId));

        int hashKey = filter.hashCode();

        if(reservationCache.get(hashKey) != null){
            return CollectionUtils.isEmpty(reservationCache.get(hashKey));
        }
        List<Reservation> existingReservations = reservationDao.filterReservations(filter);
        reservationCache.put(hashKey, existingReservations);
        return CollectionUtils.isEmpty(existingReservations);
    }
}
