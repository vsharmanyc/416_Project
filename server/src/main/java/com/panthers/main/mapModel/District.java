package com.panthers.main.mapModel;

import java.util.List;

/**
 * District is the main unique area we interact with in our map, and in our congressional districtings.
 * Districts have some precincts, neighboring districts, election data and a number&name.
 */
public class District {
    private String state;
    private int districtNum;
    private List<District> neighbors;
    private List<Precinct> precincts;

    public District(String state, int districtNum, List<District> neighbors, List<Precinct> precincts) {
        this.state = state;
        this.districtNum = districtNum;
        this.neighbors = neighbors;
        this.precincts = precincts;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getDistrictNum() {
        return districtNum;
    }

    public void setDistrictNum(int districtNum) {
        this.districtNum = districtNum;
    }

    public List<District> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<District> neighbors) {
        this.neighbors = neighbors;
    }

    public List<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(List<Precinct> precincts) {
        this.precincts = precincts;
    }

    public String toString(){
        return "District ID: " + districtNum + ",\nPrecincts: " + precincts + "\n";
    }
}
