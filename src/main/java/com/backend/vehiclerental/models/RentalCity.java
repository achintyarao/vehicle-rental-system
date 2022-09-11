package com.backend.vehiclerental.models;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RentalCity {
    private static RentalCity instance = null;
    public Map<String, Branch> branches;

    private RentalCity(){
        branches = new HashMap<>();
    }

    public Map<String, Branch> getBranches() {
        return branches;
    }

    public void setBranches(Map<String, Branch> branches) {
        this.branches = branches;
    }

    public static RentalCity getInstance(){
        if (instance == null) {
            instance = new RentalCity();
        }

        return instance;
    }

}
