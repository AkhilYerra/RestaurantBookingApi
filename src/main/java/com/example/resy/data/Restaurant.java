package com.example.resy.data;

import java.util.List;

public class Restaurant {
    public String name;
    public Long id;
    private List<Table> tableList;
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

    public List<Table> getTableList() {
        return tableList;
    }

    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }
}
