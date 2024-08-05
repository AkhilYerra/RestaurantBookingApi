package com.example.resy.data.filter;

import com.example.resy.data.DietaryRestriction;

import java.util.List;
import java.util.Set;

public class UserFilter {
    Set<Long> ids;
    Set<String> names;
    Set<DietaryRestriction> restrictions;

    public void setIds(Set<Long> ids) {
        this.ids = ids;
    }

    public Set<Long> getIds(){
        return ids;
    }

    public void setNames(Set<String> names) {
        this.names = names;
    }

    public Set<String> getNames(){
        return names;
    }

    public void setRestrictions(Set<DietaryRestriction> restrictions) {
        this.restrictions = restrictions;
    }

    public Set<DietaryRestriction> getRestrictions(){
        return restrictions;
    }
}
