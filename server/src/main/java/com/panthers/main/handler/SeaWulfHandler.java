package com.panthers.main.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panthers.main.dataaccess.SeaWulfProperties;
import com.panthers.main.jobmodel.Job;
import com.panthers.main.mapmodel.District;

import java.io.*;
import java.util.List;

/**
 * Main branch of program that will run/extract results from algorithm run on SEAWULF.
 */
public class SeaWulfHandler {
    private Job job;
    private SeaWulfProperties properties;

    public SeaWulfHandler(Job job) {
        this.job = job;
        this.properties = new SeaWulfProperties();
        this.properties.getProperties();
    }

    /*GETTERS/SETTERS*/
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    /*FUNCTIONS*/
    /**
     * function is the main, overarching function for the congressional districting generation/analysis on seawulf.
     * will run algorithm and compile results.
     * @return returns districting plans resulting from the job.
     */
    public void executeJob(){
        aggregateSeawulfData();
    }

    /**
     * method should aggregate necessary data to run the algorithm on the server
     */
    public void aggregateSeawulfData(){
        System.out.println("Aggregating Data for SeaWulf");
        String path = System.getProperty("java.class.path").split("server")[0] + properties.getServerStaticWd();

        buildBashScript(path);
        ProcessBuilder pb = new ProcessBuilder("expect", path + properties.getTransferDataBash());

        buildDataFiles(path);

        System.out.println("Sending files to SeaWulf. Expect a DUO Push...");
        pb.directory(new File(path));
        pb.redirectErrorStream(true);
        try{
            pb.start();
        }
        catch (IOException io){
            io.printStackTrace();
        }
    }

    /**
     * Method should update seawulf progress to the client side...somehow
     */
    public void updateSeaWulfProgress(){

    }

    /**
     * method would send the request to the seawulf, along with the necessary files
     */
    public void sendRequestToSeaWulf(){

    }

    /**
     * prepares seawulf for performing the requested job/algorithm running
     */
    public void prepareSeaWulf(){

    }

    private void buildDataFiles(String path){
        File swData = new File(path + properties.getSwDataPrefix() + job.getJobId() + ".json");
        FileWriter swDataOutput;
        try {
            swDataOutput = new FileWriter(swData);
            ObjectMapper objmp = new ObjectMapper();
            swDataOutput.write("{\"data\": [" + objmp.writeValueAsString(job) +",");

            FileReader fr = new FileReader(path + "/" + job.getState().name() + properties.getPrecinctDataSuffix());
            int c = fr.read();

            while (c != -1){
                swDataOutput.write(c);
                c = fr.read();
            }

            swDataOutput.close();
            fr.close();
//            swData.delete();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void buildBashScript(String path){
        File bash = new File(path + properties.getTransferDataBash());
        FileWriter bashOut;
        try {
            bashOut = new FileWriter(bash);
            ObjectMapper objmp = new ObjectMapper();
            //grabbing script from system properties file.
            String script = String.format(properties.getBashScript(), job.getJobId());
            bashOut.write(script);

            bashOut.close();
//            swData.delete();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void cancelJob(){

    }
}
