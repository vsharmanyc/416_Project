package com.panthers.main.mapmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * District is the main unique area we interact with in our map, and in our congressional districtings.
 * Districts have some precincts, neighboring districts, election data and a number&name.
 */
public class District implements Comparable<District>{
    @JsonIgnore
    private String state;
    private int districtNum;
    private List<Integer> neighbors;
    private List<Precinct> precincts;
    @JsonIgnore
    private Set<String> countyNames;
    @JsonIgnore
    private List<Polygon> boundaryData;
    private int counties;
    private double percentVap;

    public District(String state, int districtNum, List<Integer> neighbors, List<Precinct> precincts,
                    List<Polygon> boundaryData) {
        this.state = state;
        this.districtNum = districtNum;
        this.neighbors = neighbors;
        this.precincts = precincts;
        this.countyNames = new HashSet<>();
        this.boundaryData = boundaryData;
        this.counties = 0;
        this.percentVap = 0.0;
    }

    /*GETTERS/SETTERS*/
    public int getCounties() {
        return counties;
    }

    public void setCounties(int counties) {
        this.counties = counties;
    }

    public double getPercentVap() {
        return percentVap;
    }

    public void setPercentVap(double percentVap) {
        this.percentVap = percentVap;
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

    public List<Integer> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Integer> neighbors) {
        this.neighbors = neighbors;
    }

    public List<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(List<Precinct> precincts) {
        this.precincts = precincts;
    }

    public String toString(){
        return "District ID: " + districtNum +  ",\nCounties: " + counties + ",\nPercentVAP: " + percentVap;
    }

    /* FUNCTIONS */

    /**
     * Function is used when we sort the districts in a districting plan by their percentVAP
     * @param d district we compare this to
     * @return usual compareTo return values
     */
    @Override
    public int compareTo(District d) {
        return Double.compare(percentVap, d.getPercentVap());
    }

    /**
     * function checks if the given county was already accounted for in district
     * @return yes/no if county counted
     */
    public boolean checkIfCountedCounty(String county){
        return countyNames.contains(county);
    }

    /**
     * adds county to list of counties in district
     * @param county county that is in district.
     */
    public void addCounty(String county){
        countyNames.add(county);
    }

    /**
     * simply increments counties count.
     */
    public void incrementCountyCount(){
        this.counties = this.counties + 1;
    }

    /**
     * Calculates the percent mvap for this district. Utilizes
     * @param minorities list of minorities we calculate the percent mvap for.
     */
    public void calculatePercentMVAP(List<Demographic> minorities){
        double totalVap = 0.0;
        double selectedMvap = 0.0;
        for (Precinct p: precincts){
            for (Demographic d: minorities){
                totalVap += p.getPopulationData().getVotingAgePopulation();
                //Adding minority voting age populations.
                switch(d){
                    case ASIAN:
                        selectedMvap += p.getPopulationData().getMinorityVAPopulations().get(Demographic.ASIAN);
                        break;
                    case AFRICAN_AMERICAN:
                        selectedMvap += p.getPopulationData().getMinorityVAPopulations().get(Demographic.AFRICAN_AMERICAN);
                        break;
                    case AM_INDIAN_AK_NATIVE:
                        selectedMvap += p.getPopulationData().getMinorityVAPopulations().get(Demographic.AM_INDIAN_AK_NATIVE);
                        break;
                    case HISPANIC_LATINO:
                        selectedMvap += p.getPopulationData().getMinorityVAPopulations().get(Demographic.HISPANIC_LATINO);
                        break;
                    case WHITE:
                        selectedMvap += p.getPopulationData().getMinorityVAPopulations().get(Demographic.WHITE);
                        break;
                    case NH_OR_OPI:
                        selectedMvap += p.getPopulationData().getMinorityVAPopulations().get(Demographic.NH_OR_OPI);
                        break;
                    default:
                        break;
                }
            }
        }
        // Calculates the percent val by selected minorty vap by overall minority val.
        this.percentVap = selectedMvap / totalVap;
    }

    /**
     * Function calculates counties present in this specific district.
     * Loops through precincts, adding counties to list and incrementing counter when a new county is discovered
     */
    public void calculateCounties(){
        List<Precinct> ps = this.precincts;
        int countyCount = 0;
        for (Precinct precinct : ps) {
            //If county isnt accounted for, add it to the district's list + increment the count
            if (!checkIfCountedCounty(precinct.getCounty())) {
                addCounty(precinct.getCounty());
                countyCount++;
                System.out.println(countyCount);
                setCounties(countyCount);
            }
        }
    }
}
