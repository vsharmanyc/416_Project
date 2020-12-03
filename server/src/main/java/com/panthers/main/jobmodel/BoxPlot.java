package com.panthers.main.jobmodel;

import com.panthers.main.mapmodel.District;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represnts one box plot for the collapsed nth district of the districts in a set of ordered districting
 * Class calculates required values on the data, such as percentiles, average, and extremes.
 */
public class BoxPlot {
    private List<Double> mvaps;
    private double maxMVAP;
    private double thirdQuarMVAP;
    private double firstQuarMVAP;
    private double medianMVAP;
    private double minMVAP;
    private double resultingMVAP;

    public BoxPlot(List<Double> mvaps) {
        this.mvaps = mvaps;
        this.maxMVAP = 0.0;
        this.minMVAP = 0.0;
        this.thirdQuarMVAP = 0.0;
        this.firstQuarMVAP = 0.0;
        this.medianMVAP = 0.0;
        this.resultingMVAP = 0.0;
    }

    /* GETTERS/SETTERS */

    public List<Double> getMVaps() {
        return mvaps;
    }

    public void setMvaps(List<Double> mvaps) {
        this.mvaps = mvaps;
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
        double maxMVAP = 0.0;
        for (Double d: mvaps){
            if (d > maxMVAP)
                maxMVAP = d;
        }
        setMaxMVAP(maxMVAP);
    }

    /**
     * function determines the min MVAP from list of districts
     */
    public void findMinMVAP(){
        double minMVAP = 1.0;
        for (Double d: mvaps){
            if (d < minMVAP)
                minMVAP = d;
        }
        setMinMVAP(minMVAP);
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
        setMinMVAP(mvaps.get(mvaps.size()/2));
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
