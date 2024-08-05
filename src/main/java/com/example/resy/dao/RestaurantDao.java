package com.example.resy.dao;

import com.example.resy.data.Restaurant;
import com.example.resy.data.request.SearchRequest;

import java.util.List;

public interface RestaurantDao {

    public List<Restaurant> findOpenRestaurantsAtTime(SearchRequest request);
}
