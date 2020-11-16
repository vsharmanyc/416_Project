package com.panthers.main.mapModel;

import java.util.List;

public class State {
    private State name;
    private List<District> districts;
    private Population electionData;

    public State(State name, List<District> districts, Population electionData) {
        this.name = name;
        this.districts = districts;
        this.electionData = electionData;
    }

    public State getName() {
        return name;
    }

    public void setName(State name) {
        this.name = name;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public Population getElectionData() {
        return electionData;
    }

    public void setElectionData(Population electionData) {
        this.electionData = electionData;
    }
}
