package com.example.resy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.resy.dao.ReservationDao;
import com.example.resy.dao.RestaurantDao;
import com.example.resy.data.Reservation;
import com.example.resy.data.Restaurant;
import com.example.resy.data.error.ReservationAlreadyExistsException;
import com.example.resy.data.error.ReservationLockException;
import com.example.resy.data.filter.ReservationFilter;
import com.example.resy.data.request.SearchRequest;
import com.example.resy.facade.ResyFacadeImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ResyFacadeImplTests {

    @InjectMocks
    private ResyFacadeImpl resyFacade;

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private RestaurantDao restaurantDao;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redissonClient.getLock(anyString())).thenReturn(rLock);
    }

    @Test
    public void testSearchForReservation_NoExistingReservations() {
        SearchRequest searchRequest = new SearchRequest();
        // Set up searchRequest properties

        when(reservationDao.findReservations(any(ReservationFilter.class)))
                .thenReturn(Collections.emptyList());
        when(restaurantDao.findOpenRestaurantsAtTime(any(SearchRequest.class)))
                .thenReturn(Collections.emptyList());

        List<Restaurant> restaurants = resyFacade.searchForReservation(searchRequest);

        assertNotNull(restaurants);
        assertTrue(restaurants.isEmpty());
    }

    @Test
    public void testCreateReservation_LockNotAcquired() throws InterruptedException {
        Reservation reservation = new Reservation();
        // Set up reservation properties

        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(false);

        assertThrows(ReservationLockException.class, () -> resyFacade.createReservation(reservation));
    }

    @Test
    public void testDeleteReservation_Success() {
        Long reservationId = 1L;

        resyFacade.deleteReservation(reservationId);

        verify(reservationDao).deleteUserReservations(reservationId);
        verify(reservationDao).deleteReservation(reservationId);
    }


    @Test
    public void testFilterForReservations_WithoutCache() {
        ReservationFilter filter = new ReservationFilter();
        // Set up filter properties

        List<Reservation> reservations = Collections.singletonList(new Reservation());
        when(reservationDao.findReservations(filter)).thenReturn(reservations);

        List<Reservation> result = resyFacade.filterForReservations(filter);

        assertNotNull(result);
        assertEquals(reservations, result);
    }
}
