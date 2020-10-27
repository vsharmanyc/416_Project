package mapModel;

import java.util.HashMap;
import java.util.List;

/**
 * Precint is the smallest unique area we take into account in our congressional districtings.
 * Precincts contain neighbors, election data, names, and a number.
 */
public class Precinct {
    private String name;
    private List<Precinct> neighbors;
    private int precinctNum;
    private ElectionData electionData;

    private int population;
    private int votingAgePopulation;
    private HashMap<Demographic, Integer> minorityPopulations;
    private HashMap<Demographic, Integer> minorityVAPopulations;

    public Precinct(String name, List<Precinct> neighbors, int precinctNum, ElectionData electionData, int population,
                    int votingAgePopulation, HashMap<Demographic, Integer> minorityPopulations,
                    HashMap<Demographic, Integer> minorityVAPopulations) {
        this.name = name;
        this.neighbors = neighbors;
        this.precinctNum = precinctNum;
        this.electionData = electionData;
        this.population = population;
        this.votingAgePopulation = votingAgePopulation;
        this.minorityPopulations = minorityPopulations;
        this.minorityVAPopulations = minorityVAPopulations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Precinct> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Precinct> neighbors) {
        this.neighbors = neighbors;
    }

    public int getPrecinctNum() {
        return precinctNum;
    }

    public void setPrecinctNum(int precinctNum) {
        this.precinctNum = precinctNum;
    }

    public ElectionData getElectionData() {
        return electionData;
    }

    public void setElectionData(ElectionData electionData) {
        this.electionData = electionData;
    }

}
