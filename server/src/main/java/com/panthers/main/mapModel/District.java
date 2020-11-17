package com.panthers.main.mapModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * District is the main unique area we interact with in our map, and in our congressional districtings.
 * Districts have some precincts, neighboring districts, election data and a number&name.
 */
public class District implements Comparable<District>{
    private String state;
    private int districtNum;
    private List<District> neighbors;
    private List<Precinct> precincts;
    private Set<String> counties;
    private List<Polygon> boundaryData;
    private int countCounties;
    private double percentVap;

    public District(String state, int districtNum, List<District> neighbors, List<Precinct> precincts,
                    List<Polygon> boundaryData) {
        this.state = state;
        this.districtNum = districtNum;
        this.neighbors = neighbors;
        this.precincts = precincts;
        this.counties = new HashSet<>();
        this.boundaryData = boundaryData;
        this.countCounties = 0;
        this.percentVap = 0.0;
    }

    /*GETTERS/SETTERS*/
    public int getCountCounties() {
        return countCounties;
    }

    public void setCountCounties(int countCounties) {
        this.countCounties = countCounties;
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
        return "District ID: " + districtNum +  ",\nCounties: " + countCounties + ",\nPercentVAP: " + percentVap;
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
        return counties.contains(county);
    }

    /**
     * adds county to list of counties in district
     * @param county county that is in district.
     */
    public void addCounty(String county){
        counties.add(county);
    }

    /**
     * simply increments counties count.
     */
    public void incrementCountyCount(){
        this.countCounties++;
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
                totalVap += p.getElectionData().getVotingAgePopulation();
                //Adding minority voting age populations.
                switch(d){
                    case ASIAN:
                        selectedMvap += p.getElectionData().getMinorityVAPopulations().get(Demographic.ASIAN);
                        break;
                    case AFRICAN_AMERICAN:
                        selectedMvap += p.getElectionData().getMinorityVAPopulations().get(Demographic.AFRICAN_AMERICAN);
                        break;
                    case AM_INDIAN_AK_NATIVE:
                        selectedMvap += p.getElectionData().getMinorityVAPopulations().get(Demographic.AM_INDIAN_AK_NATIVE);
                        break;
                    case HISPANIC_LATINO:
                        selectedMvap += p.getElectionData().getMinorityVAPopulations().get(Demographic.HISPANIC_LATINO);
                        break;
                    case WHITE:
                        selectedMvap += p.getElectionData().getMinorityVAPopulations().get(Demographic.WHITE);
                        break;
                    case NH_OR_OPI:
                        selectedMvap += p.getElectionData().getMinorityVAPopulations().get(Demographic.NH_OR_OPI);
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
        for (Precinct precinct : ps) {
            //If county isnt accounted for, add it to the district's list + increment the count
            if (!checkIfCountedCounty(precinct.getCounty())) {
                addCounty(precinct.getCounty());
                incrementCountyCount();
            }
        }
    }
}
