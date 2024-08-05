package com.example.resy.data;

import java.util.Set;

public class User {
    private String name;
    private long id;
    private double latitude;
    private double longitude;
    private Set<DietaryRestriction> dietaryRestrictions;

    public User(String name, long id, double latitude, double longitude, Set<DietaryRestriction> restrictions){
        this.name = name;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dietaryRestrictions = restrictions;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<DietaryRestriction> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(Set<DietaryRestriction> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
