package com.panthers.main.jobModel;

import com.panthers.main.mapModel.District;
import com.panthers.main.mapModel.State;
import com.panthers.main.mapModel.States;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DistrictingPlan {
    private States state;
    private List<District> districts;
    private CompactnessMeasure compactnessMeasure;
    private double popEqThreshold;
    private String compactnessRequested;
    private DistrictingType type;
    private int counties;

    public DistrictingPlan(States state, List<District> districts,
                           double popEqThreshold, String compactnessRequested, DistrictingType type, int counties) {
        this.state = state;
        this.districts = districts;
        this.popEqThreshold = popEqThreshold;
        this.compactnessRequested = compactnessRequested;
        this.type = type;
        this.counties = counties;
    }
    /*GETTERS/SETTERS*/
    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public CompactnessMeasure getCompactnessMeasure() {
        return compactnessMeasure;
    }

    public void setCompactnessMeasure(CompactnessMeasure compactnessMeasure) {
        this.compactnessMeasure = compactnessMeasure;
    }

    public double getPopEqThreshold() {
        return popEqThreshold;
    }

    public void setPopEqThreshold(double popEqThreshold) {
        this.popEqThreshold = popEqThreshold;
    }

    public String getCompactnessRequested() {
        return compactnessRequested;
    }

    public void setCompactnessRequested(String compactnessRequested) {
        this.compactnessRequested = compactnessRequested;
    }

    public DistrictingType getType() {
        return type;
    }

    public void setType(DistrictingType type) {
        this.type = type;
    }

    public int getCounties() {
        return counties;
    }

    public void setCounties(int counties) {
        this.counties = counties;
    }

    /**
     * Sorts the districts in this districting plan by the mvapPercentage. Utilizes the collections.sort functionality,
     * since District implements Comparable
     */
    public void sortDistrictsByMVAP(){
        Collections.sort(districts);
    }

    public String toString(){
        return "Districts:" + districts + ",\nPopEqThreshold: " + popEqThreshold + ",\nCompactness: "  + compactnessRequested
         + ",\nType: " + type + "\n\n";
    }
}
