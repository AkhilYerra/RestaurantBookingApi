package com.example.resy.transformers;

import com.example.resy.data.filter.ReservationFilter;
import com.example.resy.data.request.SearchRequest;
import com.example.resy.util.date.DateUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class SearchRequestToReservationFilterTransformer implements Transformer<SearchRequest, ReservationFilter> {

    @Override
    public ReservationFilter transformToB(SearchRequest searchRequest) {
        if(searchRequest == null){
            return null;
        }
        ReservationFilter filter = new ReservationFilter();
        if (searchRequest.getPageNumber() != null) {
            filter.setPageNumber(searchRequest.getPageNumber());
        } else {
            filter.setPageNumber(0);
        }

        if (searchRequest.getPageSize() != null) {
            filter.setPageSize(searchRequest.getPageSize());
        } else {
            filter.setPageSize(20);
        }

        if(!CollectionUtils.isEmpty(searchRequest.getUserIds())){
            filter.setUserIds(searchRequest.getUserIds());
        }

        if(searchRequest.getReservationTime() != null){
            filter.setStartTime(searchRequest.getReservationTime());
            LocalDateTime endTime = DateUtil.addTwoHours(searchRequest.getReservationTime());
            filter.setEndTime(endTime);
        }

        return filter;
    }

    @Override
    public SearchRequest transformToA(ReservationFilter reservationFilter) {
        return null;
    }
}
