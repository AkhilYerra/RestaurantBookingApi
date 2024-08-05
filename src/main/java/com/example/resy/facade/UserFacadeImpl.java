package com.example.resy.facade;

import com.example.resy.dao.UserDaoImpl;
import com.example.resy.data.User;
import com.example.resy.data.filter.UserFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserFacadeImpl implements UserFacade{

    @Autowired
    UserDaoImpl userDao;
    public List<User> filter(UserFilter filter){
        if(filter == null){
            //TODO: Throw an error if filter is null as we dont want to return everything
        }
        return userDao.stubFilter(filter);
//        return new ArrayList<>();
    }
}
