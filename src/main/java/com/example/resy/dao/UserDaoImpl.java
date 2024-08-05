package com.example.resy.dao;

import com.example.resy.data.User;
import com.example.resy.data.filter.UserFilter;
import com.example.resy.util.stub.UserStubUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDaoImpl {

    public List<User> stubFilter(UserFilter filter){
        return UserStubUtil.getUserFromFilter(filter);
    }
}
