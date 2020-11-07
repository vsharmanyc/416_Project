package com.panthers.main.mapModel;

import java.util.HashMap;

public class ElectionData {
    private int precinctNum;
    private int population;
    private int votingAgePopulation;
    private HashMap<Demographic, Integer> minorityPopulations;
    private HashMap<Demographic, Integer> minorityVAPopulations;

    public ElectionData(int population, int votingAgePopulation, HashMap<Demographic, Integer> minorityPopulations,
                        HashMap<Demographic, Integer> minorityVAPopulations, int precinctNum) {
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

    public int getPrecinctNum() {
        return precinctNum;
    }

    public void setPrecinctNum(int precinctNum) {
        this.precinctNum = precinctNum;
    }
}
