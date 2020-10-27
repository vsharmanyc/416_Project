package jobModel;

import mapModel.District;
import mapModel.State;

import java.util.List;

public class DistrictingPlan {
    private State state;
    private List<District> districts;
    private CompactnessMeasure compactnessMeasure;
    private double popEqThreshold;
    private String compactnessRequested;
    private DistrictingType type;
    private int counties;

    public DistrictingPlan(State state, List<District> districts, CompactnessMeasure compactnessMeasure,
                           double popEqThreshold, String compactnessRequested, DistrictingType type, int counties) {
        this.state = state;
        this.districts = districts;
        this.compactnessMeasure = compactnessMeasure;
        this.popEqThreshold = popEqThreshold;
        this.compactnessRequested = compactnessRequested;
        this.type = type;
        this.counties = counties;
    }
    /*GETTERS/SETTERS*/
    public State getState() {
        return state;
    }

    public void setState(State state) {
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
}
