package com.panthers.main.jobModel;

import com.panthers.main.mapModel.District;

import java.util.List;

/**
 * Class represnts one box plot for the collapsed nth district of the districts in a set of ordered districting
 * Class calculates required values on the data, such as percentiles, average, and extremes.
 */
public class BoxPlot {
    private List<District> districts;
    private double maxMVAP;
    private double thirdQuarMVAP;
    private double firstQuarMVAP;
    private double medianMVAP;
    private double minMVAP;
    private double resultingMVAP;

    public BoxPlot(List<District> districts) {
        this.districts = districts;
        this.maxMVAP = 0.0;
        this.minMVAP = 0.0;
        this.thirdQuarMVAP = 0.0;
        this.firstQuarMVAP = 0.0;
        this.medianMVAP = 0.0;
        this.resultingMVAP = 0.0;
    }

    /* GETTERS/SETTERS */

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public double getMaxMVAP() {
        return maxMVAP;
    }

    public void setMaxMVAP(double maxMVAP) {
        this.maxMVAP = maxMVAP;
    }

    public double getThirdQuarMVAP() {
        return thirdQuarMVAP;
    }

    public void setThirdQuarMVAP(double thirdQuarMVAP) {
        this.thirdQuarMVAP = thirdQuarMVAP;
    }

    public double getFirstQuarMVAP() {
        return firstQuarMVAP;
    }

    public void setFirstQuarMVAP(double firstQuarMVAP) {
        this.firstQuarMVAP = firstQuarMVAP;
    }

    public double getMedianMVAP() {
        return medianMVAP;
    }

    public void setMedianMVAP(double medianMVAP) {
        this.medianMVAP = medianMVAP;
    }

    public double getMinMVAP() {
        return minMVAP;
    }

    public void setMinMVAP(double minMVAP) {
        this.minMVAP = minMVAP;
    }

    public double getResultingMVAP() {
        return resultingMVAP;
    }

    public void setResultingMVAP(double resultingMVAP) {
        this.resultingMVAP = resultingMVAP;
    }

    /* FUNCTIONS */

    /**
     * function determines the max MVAP from list of districts
     */
    public void findMaxMVAP(){

    }

    /**
     * function determines the min MVAP from list of districts
     */
    public void findMinMVAP(){

    }

    /**
     * Function determines first quartile of MVAP's from districts
     */
    public void findFirstQuart(){

    }

    /**
     * Function determines third quartile of MVAP's from districts
     */
    public void findThirdQuart(){

    }

    /**
     * Function determines mean of MVAP's from districts
     */
    public void findMeanMVAP(){

    }

    /**
     * function compiles boxplot data into a canvasjs friendly format
     * ~~~~deprecated
     * @return returns the box plot data in canvas.js friendly format
     */
    public String compileToCanvasJSFormat(){
        return "";
    }


}
