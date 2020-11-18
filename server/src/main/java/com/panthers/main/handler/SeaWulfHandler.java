package com.panthers.main.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panthers.main.jobModel.RunResults;
import com.panthers.main.jobModel.Job;
import com.panthers.main.mapModel.District;

import java.io.*;
import java.util.List;

/**
 * Main branch of program that will run/extract results from algorithm run on SEAWULF.
 */
public class SeaWulfHandler {
    private Job job;
    private List<District> currentDistricting;

    public SeaWulfHandler(Job job) {
        this.job = job;
        this.currentDistricting = null;
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
        String path = System.getProperty("java.class.path").split("server")[0] +
                "server/src/main/resources/static";
        ProcessBuilder pb = new ProcessBuilder("expect", path + "/transfer_MD_data.sh");

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

    public void buildDataFiles(String path){
        File swData = new File(path + "/swData.txt");
        FileWriter swDataOutput;
        try {
            swDataOutput = new FileWriter(swData);
            ObjectMapper objmp = new ObjectMapper();
            swDataOutput.write(objmp.writeValueAsString(job) +"\n\nDATA:\n");
            FileReader fr = new FileReader(path + "/MD_Precincts_data.json");
            int c = fr.read();

            while (c != -1){
                swDataOutput.write(c);
                c = fr.read();
            }

            swDataOutput.close();
            fr.close();
//            swData.delete();
        }
        catch (FileNotFoundException fnf){
            fnf.printStackTrace();
        }
        catch (IOException io){
            io.printStackTrace();
        }
    }
}
