package com.panthers.main.mapmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.List;

/**
 * Precint is the smallest unique area we take into account in our congressional districtings.
 * Precincts contain neighbors, election data, names, and a number.
 */
public class Precinct {
    @JsonIgnore
    private String name;
    @JsonIgnore
    private List<Precinct> neighbors;
    private String geoid10;
    private Population populationData;
    @JsonIgnore
    private List<List<Double>> boundaryData;

    private int population;
    private int votingAgePopulation;
    @JsonIgnore
    private HashMap<Demographic, Integer> minorityPopulations;
    @JsonIgnore
    private HashMap<Demographic, Integer> minorityVAPopulations;
    @JsonIgnore
    private int districtID;
    @JsonIgnore
    private String county;
    private int bvap;
    private int btot;
    private int hvap;
    private int htot;
    private int aianvap;
    private int aiaintot;
    private int avap;
    private int atot;
    private int nhopvap;
    private int nhoptot;

    public Precinct(String name, List<Precinct> neighbors, String geoid10, Population populationData, List<List<Double>> boundaryData, int districtID, String county) {
        this.name = name;
        this.neighbors = neighbors;
        this.geoid10 = geoid10;
        this.populationData = populationData;
        this.population = population;
        this.votingAgePopulation = votingAgePopulation;
        this.minorityPopulations = minorityPopulations;
        this.minorityVAPopulations = minorityVAPopulations;
        this.boundaryData = boundaryData;
        this.districtID = districtID;
        this.county = county;
        this.setDataForJson();
    }

    /* GETTERS/SETTERS */
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<Double>> getBoundaryData() {
        return boundaryData;
    }

    public void setBoundaryData(List<List<Double>> boundaryData) {
        this.boundaryData = boundaryData;
    }

    public List<Precinct> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Precinct> neighbors) {
        this.neighbors = neighbors;
    }

    public String getgeoid10() {
        return geoid10;
    }

    public void setgeoid10(String geoid10) {
        this.geoid10 = geoid10;
    }

    public Population getPopulationData() {
        return populationData;
    }

    public void setPopulationData(Population populationData) {
        this.populationData = populationData;
    }

    private void setDataForJson(){
        return;
    }
    public String toString(){
        return "{\nPopulation: " + population + ",\nVotingAgePopulation: " + votingAgePopulation +
                ",\nMinorityPopulationData: " + minorityPopulations + ",\nMinorityVAPPopulations: " + minorityVAPopulations
                 + "\nPrecinctID: " + geoid10 + "\nCounty: " + county;
    }

}
