package com.example.resy.data;

public class Restaurant {
    public String name;
    public Long id;
    //Todo: Add location data later


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
