package com.example.resy.data.filter;

import com.example.resy.data.Endorsement;
import com.example.resy.data.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class SearchFilter {
    private Set<Long> ids;
    private int minimumGuests;
    private Date reservationTime;
    private List<User> users;
    private Set<Endorsement> endorsements;

    public Set<Long> getIds() {
        return ids;
    }

    public void setIds(Set<Long> ids) {
        this.ids = ids;
    }

    public int getMinimumGuests() {
        return minimumGuests;
    }

    public void setMinimumGuests(int minimumGuests) {
        this.minimumGuests = minimumGuests;
    }

    public Date getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Date reservationTime) {
        this.reservationTime = reservationTime;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Set<Endorsement> getEndorsements() {
        return endorsements;
    }

    public void setEndorsements(Set<Endorsement> endorsements) {
        this.endorsements = endorsements;
    }
}
