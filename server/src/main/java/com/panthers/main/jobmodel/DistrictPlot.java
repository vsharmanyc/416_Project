package com.panthers.main.jobmodel;

import com.panthers.main.mapmodel.District;

public class DistrictPlot {
    /*
    * + district: District
+ maxMVAP: float
+ thirdQuadMVAP: float
+ medianMVAP: float
+ firstQuadMVAP: float
+ minMVAP: float
+ resultingMVAP: float*/
    private District district;
    private double maxMVAP;
    private double medianMVAP;
    private double firstQuadMVAP;
    private double thirdQuadMVAP;
    private double minMVAP;
    private double resultingMVAP;

    public DistrictPlot(District district, double maxMVAP, double medianMVAP, double firstQuadMVAP,
                        double thirdQuadMVAP, double minMVAP, double resultingMVAP) {
        this.district = district;
        this.maxMVAP = maxMVAP;
        this.medianMVAP = medianMVAP;
        this.firstQuadMVAP = firstQuadMVAP;
        this.thirdQuadMVAP = thirdQuadMVAP;
        this.minMVAP = minMVAP;
        this.resultingMVAP = resultingMVAP;
    }

    /*GETTERS/SETTERS*/
    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public double getMaxMVAP() {
        return maxMVAP;
    }

    public void setMaxMVAP(double maxMVAP) {
        this.maxMVAP = maxMVAP;
    }

    public double getMedianMVAP() {
        return medianMVAP;
    }

    public void setMedianMVAP(double medianMVAP) {
        this.medianMVAP = medianMVAP;
    }

    public double getFirstQuadMVAP() {
        return firstQuadMVAP;
    }

    public void setFirstQuadMVAP(double firstQuadMVAP) {
        this.firstQuadMVAP = firstQuadMVAP;
    }

    public double getThirdQuadMVAP() {
        return thirdQuadMVAP;
    }

    public void setThirdQuadMVAP(double thirdQuadMVAP) {
        this.thirdQuadMVAP = thirdQuadMVAP;
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

    /*FUNCTIONS*/

    /**
     * function will compile the box-plot information to a format readable by canvas.js, our graphic library
     * @return returns the canvas.js readable string of our districting plot.
     */
    public String compileToCanvasJSFormat(){
        return null;
    }
}
