package com.panthers.main.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.panthers.main.dataaccess.SeaWulfProperties;
import com.panthers.main.jobmodel.DistrictingPlan;
import com.panthers.main.jobmodel.JobStatus;
import com.panthers.main.jobmodel.RunResults;
import com.panthers.main.jobmodel.Job;
import com.panthers.main.mapmodel.*;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Main branch of program that will run/extract results from algorithm run on SERVER.
 */
public class ServerHandler {
    private Job job;

    private SeaWulfProperties properties;

    public ServerHandler(Job job) {
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

    public void executeJob(){
        startJobOnServer();
        getJobFromServer();//Waits until its done, basically busy waiting.
    }

    public void cancelJob(){

    }

    public void startJobOnServer(){
        System.out.println("Sending sbatch command to start job on seawulf");
        String path = System.getProperty("java.class.path").split("server")[0] + properties.getAlgorithmStaticWd();

        buildDataFiles(path);
        ProcessBuilder pb = new ProcessBuilder("python3", path + "algorithm.py " + job.getJobId());
        pb.inheritIO();

        System.out.println("Starting job on Server");
        pb.directory(new File(path));
        pb.redirectErrorStream(true);
        try{
            pb.start();
        }
        catch (Exception io){
            io.printStackTrace();
        }
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

    public void getJobFromServer(){
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            String p = System.getProperty("java.class.path").split("server")[0] + properties.getAlgorithmStaticWd();
            Path path = Paths.get(p);
            path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
            while (1==1) {
                WatchKey key;
                key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> eventType = event.kind();
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    if (eventType == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    } else if (eventType == StandardWatchEventKinds.ENTRY_CREATE ||
                            eventType == StandardWatchEventKinds.ENTRY_MODIFY) {
                        final Path itemChanged = (Path) event.context();
                        if (itemChanged.endsWith("result" + job.getJobId() + ".json")) {
                            System.out.println("Server algorithm has finished. ");
                            transferResultFiles(job);
                            this.job.setJobStatus(JobStatus.POST_PROCESSING);
                            RunResults rr = parseDataIntoRunResult(this.job);
                            postProcessServerJob(rr);
                        }
                    }
                }
                boolean changed = key.reset();
                if (!changed) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transferResultFiles(Job job){
        System.out.println("Transferring Summary from SeaWulf for Job#"+job.getJobId());
        String path = System.getProperty("java.class.path").split("server")[0] + properties.getServerStaticWd();
        ProcessBuilder pb = new ProcessBuilder("bash", path + properties.getTransferSummaryBash());

        buildTransferScript(path);
        System.out.println("Grabbing files from algorithm. Expect a DUO push...");
        pb.directory(new File(path));
        pb.redirectErrorStream(true);
        try{
            pb.start();
        }
        catch (Exception io){
            io.printStackTrace();
        }
    }


    private void buildTransferScript(String path){
        File bash = new File(path + properties.getTransferFileFromAlg());
        FileWriter bashOut;
        try {
            bashOut = new FileWriter(bash);
            ObjectMapper objmp = new ObjectMapper();
            //grabbing script from system properties file.
            String script = String.format(properties.getTransferFileFromAlgFile(), job.getJobId(), job.getJobId());
            bashOut.write(script);

            bashOut.close();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    public RunResults parseDataIntoRunResult(Job j){
        System.out.println("Aggregating data from SeaWulf run results.");
        JsonFactory jsonfactory = new JsonFactory();
        File source = new File("/Users/james/Documents/Code/University/416/416_Project/server/src/main/resources/static/NY_result.json");
        List<DistrictingPlan> plans = new ArrayList<>();
        try {
            JsonParser parser = jsonfactory.createJsonParser(source);
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String token = parser.getCurrentName();
            }
            String token = parser.getCurrentName();

            while (token != null && !token.equals("districtingPlans")){
                token = parser.getCurrentName();
            }

            while (parser.getCurrentToken() != JsonToken.START_OBJECT){
                parser.nextToken();
            }
            parser.nextToken();
            while (parser.getCurrentToken() != JsonToken.END_ARRAY) {
                while (parser.getCurrentToken() != JsonToken.START_OBJECT && parser.getCurrentToken() != null) {
                    parser.nextToken();
                }
                if (parser.getCurrentToken() == null)
                    break;
                //'districting'
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    token = parser.getCurrentName();
                    List<District> districts = new ArrayList<>();
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        if (parser.nextToken() == JsonToken.END_ARRAY)
                            break;
                        parser.nextToken();
                        parser.nextToken();

                        //District ID
                        int districtID = parser.getIntValue();
                        parser.nextToken();
                        parser.nextToken();
                        List<Integer> districtNeighbors = new ArrayList<>();
                        while (parser.nextToken() != JsonToken.END_ARRAY)
                            districtNeighbors.add(parser.getIntValue());
                        parser.nextToken();
                        parser.nextToken();
                        double mvap = parser.getDoubleValue();
                        parser.nextToken();
                        List<Precinct> precincts = new ArrayList<>();
                        //PRECINCTS
                        parser.nextToken();
                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                            HashMap<Demographic, Integer> mtot = new HashMap<>();
                            HashMap<Demographic, Integer> mvaps = new HashMap<>();
                            parser.nextToken();//AIANTOT
                            parser.nextToken();
                            //For each minority, do this.
                            if (job.getDemographicGroups().contains(Demographic.ASIAN)) {
                                mtot.put(Demographic.ASIAN, parser.getIntValue());
                                parser.nextToken();//AIANTVAP
                                parser.nextToken();
                                mvaps.put(Demographic.ASIAN, parser.getIntValue());
                            } else {
                                parser.nextToken();//AIANTVAP
                                parser.nextToken();
                            }
                            parser.nextToken();//ATOT
                            parser.nextToken();
                            if (job.getDemographicGroups().contains(Demographic.AM_INDIAN_AK_NATIVE)) {
                                mtot.put(Demographic.AM_INDIAN_AK_NATIVE, parser.getIntValue());
                                parser.nextToken();//AVAP
                                parser.nextToken();
                                mvaps.put(Demographic.AM_INDIAN_AK_NATIVE, parser.getIntValue());
                            } else {
                                parser.nextToken();//ATVAP
                                parser.nextToken();
                            }
                            parser.nextToken();//BTOT
                            parser.nextToken();
                            if (job.getDemographicGroups().contains(Demographic.AFRICAN_AMERICAN)) {
                                mtot.put(Demographic.AFRICAN_AMERICAN, parser.getIntValue());
                                parser.nextToken();//BVAP
                                parser.nextToken();
                                mvaps.put(Demographic.AFRICAN_AMERICAN, parser.getIntValue());
                            } else {
                                parser.nextToken();//BVAP
                                parser.nextToken();
                            }
                            //COUNTY, COUNTYID, ID
                            parser.nextToken();
                            parser.nextToken();
                            String county = parser.getText();
                            parser.nextToken();
                            parser.nextToken();
                            parser.nextToken();
                            parser.nextToken();
                            int geoid10 = parser.getIntValue();
                            parser.nextToken();
                            parser.nextToken();
                            if (job.getDemographicGroups().contains(Demographic.HISPANIC_LATINO)) {
                                mtot.put(Demographic.HISPANIC_LATINO, parser.getIntValue());
                                parser.nextToken();//hVAP
                                parser.nextToken();
                                mvaps.put(Demographic.HISPANIC_LATINO, parser.getIntValue());
                            } else {
                                parser.nextToken();//hVAP
                                parser.nextToken();
                            }
                            parser.nextToken();//NHOPTOT
                            parser.nextToken();
                            if (job.getDemographicGroups().contains(Demographic.NH_OR_OPI)) {
                                mtot.put(Demographic.NH_OR_OPI, parser.getIntValue());
                                parser.nextToken();//NHOPVAP
                                parser.nextToken();
                                mvaps.put(Demographic.NH_OR_OPI, parser.getIntValue());
                            } else {
                                parser.nextToken();//NHOPVAP
                                parser.nextToken();
                            }
                            parser.nextToken();
                            parser.nextToken();
                            int precinctID = parser.getIntValue();
                            parser.nextToken();
                            parser.nextToken();
                            int totPop = parser.getIntValue();
                            parser.nextToken();
                            parser.nextToken();
                            int totVap = parser.getIntValue();
                            Population pop = new Population(totPop, totVap, mtot, mvaps,
                                    String.valueOf(precinctID));
                            Precinct p = new Precinct("", null, String.valueOf(geoid10),
                                    pop, null, precinctID, county);
                            precincts.add(p);
                            parser.nextToken();
                        }
                        District d = new District(job.getState().toString(), districtID, districtNeighbors, precincts, null);
                        d.setPercentVap(mvap);
                        districts.add(d);
                    }
                    plans.add(new DistrictingPlan(job.getState(), districts, job.getPopEqThreshold(), job.getCompactness()));
                }
            }
            parser.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Create the RunResults object here.
        System.out.println("Created Run Results Object");
        return new RunResults(job, plans);
    }

    public void postProcessServerJob(RunResults rr){
        System.out.println("Calculating counties for job#" + 1);
        rr.calculateCounties();
        System.out.println("Sorting Districtings");
        rr.sortResultDistrictings();
        rr.determineStatisticalAverage();
        rr.scoreDistrictingPlans();
        rr.findLowestScoringDistricting();
        rr.findHighestScoringDistricting();
        rr.findRandomDistricting();
        writeResultToFile(rr);
        rr.addDistrictingsBack();
        rr.generateBoxPlot();
        System.out.println("Storing Box Plot Data in Database.");
        this.job.setJobStatus(JobStatus.COMPLETED);
        rr.storeBoxPlotInJob();
        rr.generateSummary();
        rr.writeSummaryToSeaWulf();
        System.out.println("Run Results Processing Complete.");
    }

    public void writeResultToFile(RunResults rr) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(rr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String path = System.getProperty("java.class.path").split("server")[0] + properties.getServerStaticWd();
        File bash = new File(path + "/job" + job.getJobId() + "_processed.json");
        FileWriter bashOut;
        System.out.println("Writing result to file...");
        try {
            bashOut = new FileWriter(bash);
            ObjectMapper objmp = new ObjectMapper();
            //grabbing script from system properties file.
            bashOut.write(jsonString);

            bashOut.close();
//            swData.delete();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
