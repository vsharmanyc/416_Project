package com.panthers.main.mapModel;

import java.util.List;

/**
 * District is the main unique area we interact with in our map, and in our congressional districtings.
 * Districts have some precincts, neighboring districts, election data and a number&name.
 */
public class District {
    private String name;
    private int districtNum;
    private List<District> neighbors;
    private List<Precinct> precincts;

    public District(String name, int districtNum, List<District> neighbors, List<Precinct> precincts) {
        this.name = name;
        this.districtNum = districtNum;
        this.neighbors = neighbors;
        this.precincts = precincts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
