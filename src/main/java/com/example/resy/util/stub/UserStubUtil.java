package com.example.resy.util.stub;

import com.example.resy.data.DietaryRestriction;
import com.example.resy.data.User;
import com.example.resy.data.filter.UserFilter;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class UserStubUtil {

    private static List<User> stubUsers = new ArrayList<>(Arrays.asList(
            new User("John Doe", 1, 10.10, 11.11,Set.of(DietaryRestriction.LACTOSE_INTOLERANT)),
            new User("Jane Smith", 2, 10.10, 11.11, new HashSet<>()),
            new User("Jack Sparrow", 3, 10.10, 11.11,Set.of(DietaryRestriction.PALEO))));

    public static List<User> getUserFromFilter(UserFilter userFilter){
        return stubUsers;
    }
}
