package com.example.resy.facade;

import com.example.resy.data.User;
import com.example.resy.data.filter.UserFilter;

import java.util.List;

public interface UserFacade {
    List<User> filter(UserFilter filter);
}
