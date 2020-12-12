package com.panthers.main.mapmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

/**
 * Object wraps population data metrics in an easily manageable/modular class
 * Contains populations, vap, and minority vaps for each minority
 */
public class Population {
    @JsonIgnore
    private String precinctNum;
    private int population;
    private int votingAgePopulation;
    private HashMap<Demographic, Integer> minorityPopulations;
    private HashMap<Demographic, Integer> minorityVAPopulations;

    public Population(int population, int votingAgePopulation, HashMap<Demographic, Integer> minorityPopulations,
                      HashMap<Demographic, Integer> minorityVAPopulations, String precinctNum) {
        this.population = population;
        this.votingAgePopulation = votingAgePopulation;
        this.minorityPopulations = minorityPopulations;
        this.minorityVAPopulations = minorityVAPopulations;
        this.precinctNum = precinctNum;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getVotingAgePopulation() {
        return votingAgePopulation;
    }

    public void setVotingAgePopulation(int votingAgePopulation) {
        this.votingAgePopulation = votingAgePopulation;
    }

    public HashMap<Demographic, Integer> getMinorityPopulations() {
        return minorityPopulations;
    }

    public void setMinorityPopulations(HashMap<Demographic, Integer> minorityPopulations) {
        this.minorityPopulations = minorityPopulations;
    }

    public HashMap<Demographic, Integer> getMinorityVAPopulations() {
        return minorityVAPopulations;
    }

    public void setMinorityVAPopulations(HashMap<Demographic, Integer> minorityVAPopulations) {
        this.minorityVAPopulations = minorityVAPopulations;
    }

    public String getPrecinctNum() {
        return precinctNum;
    }

    public void setPrecinctNum(String precinctNum) {
        this.precinctNum = precinctNum;
    }
}
