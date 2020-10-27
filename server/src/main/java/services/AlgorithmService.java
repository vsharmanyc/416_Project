package services;

import jdk.internal.util.xml.impl.Pair;
import jobModel.CompactnessMeasure;
import jobModel.DistrictingPlan;
import jobModel.DistrictingPlans;
import mapModel.Demographic;
import mapModel.District;

import java.util.List;

/**
 * The methods behind the main algorithm constructing the desired districtings. Returns a DistrictingPlans Object,
 * which holds all information resulting from the algorithm run.
 */
public class AlgorithmService {
    private List<Demographic> demographicGroups;
    private CompactnessMeasure compactnessMeasure;
    private double determinedCompactness;
    private double popEqThreshold;
    private String compactnessRequested;
    private DistrictingPlans runResults;
    private DistrictingPlan seedDistricting;
    private List<Pair> feasibleEdges;

    public AlgorithmService(List<Demographic> demographicGroups, CompactnessMeasure compactnessMeasure,
                            double determinedCompactness, double popEqThreshold, String compactnessRequested) {
        this.demographicGroups = demographicGroups;
        this.compactnessMeasure = compactnessMeasure;
        this.determinedCompactness = determinedCompactness;
        this.popEqThreshold = popEqThreshold;
        this.compactnessRequested = compactnessRequested;
        this.runResults = null;
        this.seedDistricting = null;
        this.feasibleEdges = null;
    }

    /*GETTERS/SETTERS*/
    public List<Demographic> getDemographicGroups() {
        return demographicGroups;
    }

    public void setDemographicGroups(List<Demographic> demographicGroups) {
        this.demographicGroups = demographicGroups;
    }

    public CompactnessMeasure getCompactnessMeasure() {
        return compactnessMeasure;
    }

    public void setCompactnessMeasure(CompactnessMeasure compactnessMeasure) {
        this.compactnessMeasure = compactnessMeasure;
    }

    public double getDeterminedCompactness() {
        return determinedCompactness;
    }

    public void setDeterminedCompactness(double determinedCompactness) {
        this.determinedCompactness = determinedCompactness;
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

    /*FUNCTIONS*/
    /**
     * calculates compactness of given districting plan
     * @return returns the measure compactness
     */
    public double measureCompactness(DistrictingPlan plan){
        return 0.0;
    }

    /**
     * main entry point of algorithm; manages all algorithm processes, and returns the run results in the form of
     * DistrictingPlans
     * @return the run results from the algorithm
     */
    public DistrictingPlans startAlgorithm(){
        return null;
    }

    /**
     * generates seed districting to be used in this run of the algorithm.
     * @return determined seed districting
     */
    public DistrictingPlan generateSeed(){
        return null;
    }

    /**
     * returns a randomly generated districting
     * @return randomly generated districting
     */
    public DistrictingPlan generateRandomDistricting(){
        return null;
    }

    /**
     * performs recombination step of the algorithm.
     */
    public void recombineTreeNodes(){

    }

    /**
     * generates spanning tree of districting graph
     */
    public void generateSpanningTree(){

    }

    /**
     * calculates acceptability of given graph according to our acceptability standards
     * @return
     */
    public boolean calculateAcceptability(){
        return false;
    }

    /**
     * generates set of feasible edges we may cut in algorithm.
     */
    public void generateEdgeList(){

    }

    /**
     * actually cuts a random edge from our list of acceptable edges.
     */
    public void cutEdge(){

    }

    /**
     * generates and returns a truly random integer
     * @return a truly random integer.
     */
    public int randomGenerator(){
        return -1;
    }



}
