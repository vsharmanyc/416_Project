package com.panthers.main.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panthers.main.jobmodel.Job;
import com.panthers.main.mapmodel.District;

import java.io.*;
import java.util.List;

/**
 * Main branch of program that will run/extract results from algorithm run on SEAWULF.
 */
public class SeaWulfHandler {
    private Job job;

    public SeaWulfHandler(Job job) {
        this.job = job;
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

        buildBashScript(path);
        ProcessBuilder pb = new ProcessBuilder("expect", path + "/transferDataToSeaWulf.sh");

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
        File swData = new File(path + "/swData_job" + job.getJobId() + ".txt");
        FileWriter swDataOutput;
        try {
            swDataOutput = new FileWriter(swData);
            ObjectMapper objmp = new ObjectMapper();
            swDataOutput.write(objmp.writeValueAsString(job) +"\n\nDATA:\n");

            FileReader fr = new FileReader(path + "/" + job.getState().name() + "_Precinct_data.json");
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
        File bash = new File(path + "/transferDataToSeaWulf.sh");
        FileWriter bashOut;
        try {
            bashOut = new FileWriter(bash);
            ObjectMapper objmp = new ObjectMapper();
            //grabbing script from system properties file.
            String bashScript = "#!/usr/bin/expect\n\nspawn scp swData_job%d.txt jlungu@login.seawulf.stonybrook.edu:/gpfs/scratch/jlungu\nexpect \"jlungu@login.seawulf.stonybrook.edu's password:\"\nsend \"Uranium12*\\r\"\ninteract\n";
            String script = String.format(bashScript, job.getJobId());
            bashOut.write(script);

            bashOut.close();
//            swData.delete();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}
