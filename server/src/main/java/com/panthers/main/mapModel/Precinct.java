package com.panthers.main.mapModel;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * Precint is the smallest unique area we take into account in our congressional districtings.
 * Precincts contain neighbors, election data, names, and a number.
 */
public class Precinct {
    private String name;
    private List<Precinct> neighbors;
    private String precinctNum;
    private Population electionData;
    private List<List<Double>> boundaryData;

    private int population;
    private int votingAgePopulation;
    private HashMap<Demographic, Integer> minorityPopulations;
    private HashMap<Demographic, Integer> minorityVAPopulations;
    private int districtID;

    private String county;

    public Precinct(String name, List<Precinct> neighbors, String precinctNum, Population electionData, int population,
                    int votingAgePopulation, HashMap<Demographic, Integer> minorityPopulations,
                    HashMap<Demographic, Integer> minorityVAPopulations, List<List<Double>> boundaryData, int districtID, String county) {
        this.name = name;
        this.neighbors = neighbors;
        this.precinctNum = precinctNum;
        this.electionData = electionData;
        this.population = population;
        this.votingAgePopulation = votingAgePopulation;
        this.minorityPopulations = minorityPopulations;
        this.minorityVAPopulations = minorityVAPopulations;
        this.boundaryData = boundaryData;
        this.districtID = districtID;
        this.county = county;
    }

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

    public String getPrecinctNum() {
        return precinctNum;
    }

    public void setPrecinctNum(String precinctNum) {
        this.precinctNum = precinctNum;
    }

    public Population getElectionData() {
        return electionData;
    }

    public void setElectionData(Population electionData) {
        this.electionData = electionData;
    }

    public String toString(){
        return "{\nPopulation: " + population + ",\nVotingAgePopulation: " + votingAgePopulation +
                ",\nMinorityPopulationData: " + minorityPopulations + ",\nMinorityVAPPopulations: " + minorityVAPopulations
                 + "\nPrecinctID: " + precinctNum;
    }

}
