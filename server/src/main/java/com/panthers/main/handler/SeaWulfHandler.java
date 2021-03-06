package com.panthers.main.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.panthers.main.dataaccess.SeaWulfProperties;
import com.panthers.main.jobmodel.DistrictingPlan;
import com.panthers.main.jobmodel.Job;
import com.panthers.main.jobmodel.JobStatus;
import com.panthers.main.jobmodel.RunResults;
import com.panthers.main.jpa.Dao;
import com.panthers.main.jpa.JpaJobDao;
import com.panthers.main.mapmodel.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Main branch of program that will run/extract results from algorithm run on SEAWULF.
 */
public class SeaWulfHandler {
    private Job job;
    private SeaWulfProperties properties;

    private static Dao<Job> jpaUserDao = new JpaJobDao();

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
        //aggregateSeawulfData();
        startJobOnSeaWulf();
        grabSeaWulfJobNumber();
    }

    public void grabSeaWulfJobNumber(){
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            String p = System.getProperty("java.class.path").split("server")[0] + properties.getServerStaticWd();
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
                        if (itemChanged.endsWith("id.txt")) {
                            System.out.println("Found file change. Writing to SW object");
                            //read the transferred progress.txt
                            BufferedReader reader;
                            try{
                                reader = new BufferedReader(new FileReader(path+"/id.txt"));
                                String line = reader.readLine();
                                int swJobId = Integer.parseInt(line.substring(20,line.length()));
                                this.job.setSwJobNum(swJobId);
                                //should probably update the job in DB
                                this.job.setJobStatus(JobStatus.RUNNING);
                                jpaUserDao.update(this.job);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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

    /**
     * method should aggregate necessary data to run the algorithm on the server
     */
    public void aggregateSeawulfData(){
        System.out.println("Aggregating Data for SeaWulf");
        String path = System.getProperty("java.class.path").split("server")[0] + properties.getServerStaticWd();

        //buildBashScript(path);
//        buildSlurmScript(path);
        System.out.println(path + properties.getTransferDataBash());
        ProcessBuilder pb = new ProcessBuilder("expect", path + properties.getTransferDataBash());

        buildDataFiles(path);

        System.out.println("Sending files to SeaWulf. Expect a DUO Push...");
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
        File swData = new File(path + properties.getSwDataPrefix() + ".json");
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

    private void buildSlurmScript(String path){
        File bash = new File(path + properties.getSlurmScriptPath());
        FileWriter bashOut;
        // Finding node count
        int nodeCount = 0;
        String time = "";
        String nodeName = "";
        if (job.getNumDistrictings() > 80){
            nodeCount = 3;
            time = "2-00:00:00";
            nodeName = "long-40core";
        }
        else if (job.getNumDistrictings() > 40){
            nodeCount = 2;
            time = "24:00;00";
            nodeName = "long-40core";
        }
        else{
            nodeCount = 1;
            time = "12:00:00";
            nodeName = "large-40core";
        }
        try {
            bashOut = new FileWriter(bash);
            ObjectMapper objmp = new ObjectMapper();
            //grabbing script from system properties file.
            String script = String.format(properties.getSlurmScript(), job.getState().toString(), job.getNumDistrictings(), job.getState().toString(), job.getNumDistrictings(),
                    nodeCount, time, nodeName, job.getNumDistrictings(), nodeCount);
            bashOut.write(script);

            bashOut.close();
//            swData.delete();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void buildBashScript(String path){
        File bash = new File(path + properties.getTransferDataBash());
        System.out.println(bash);
        FileWriter bashOut;
        try {
            bashOut = new FileWriter(bash);
            ObjectMapper objmp = new ObjectMapper();
            //grabbing script from system properties file.
            String script = String.format(properties.getBashScript(), properties.getNetID(), properties.getNetID(),
                    properties.getPassword(), job.getState().toString(), job.getJobId(), job.getState().toString(),
                    job.getJobId(), properties.getNetID(), properties.getPassword());
            bashOut.write(script);

            bashOut.close();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startJobOnSeaWulf(){
        System.out.println("Sending sbatch command to start job on seawulf");
        String path = System.getProperty("java.class.path").split("server")[0] + properties.getServerStaticWd();

        buildSlurmScript(path);
        buildStartScript(path);
        ProcessBuilder pb = new ProcessBuilder("expect", path + properties.getBashStartJobScriptPath());

        buildDataFiles(path);

        System.out.println("Logging in and starting job. Expect a DUO push...");
        pb.directory(new File(path));
        pb.redirectErrorStream(true);
        try{
            pb.start();
        }
        catch (Exception io){
            io.printStackTrace();
        }
    }

    public void buildStartScript(String path){
        File bash = new File(path + properties.getBashStartJobScriptPath());
        FileWriter bashOut;
        try {
            bashOut = new FileWriter(bash);
            ObjectMapper objmp = new ObjectMapper();
            //grabbing script from system properties file.
            String script = String.format(properties.getJobStartScript(), properties.getNetID(), properties.getNetID(),
                    properties.getPassword(), job.getState().toString(), job.getJobId(), job.getState().toString(),
                    job.getJobId(), properties.getNetID(), properties.getPassword(), properties.getNetID(),
                    properties.getNetID(), properties.getPassword(), job.getState().toString(), job.getJobId(),
                    properties.getNetID(), job.getState().toString(), job.getJobId(), properties.getNetID(),
                    properties.getPassword());
            bashOut.write(script);
            bashOut.close();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void cancelJob(){
        System.out.println("Cancelling job" + job.getJobId() + " on SeaWulf.");
        String path = System.getProperty("java.class.path").split("server")[0] + properties.getServerStaticWd();

        buildCancelScript(path);
        ProcessBuilder pb = new ProcessBuilder("expect", path + properties.getCancelSwJobFile());

        buildDataFiles(path);

        System.out.println("Logging in and cancelling Job. Expect a DUO push...");
        pb.directory(new File(path));
        pb.redirectErrorStream(true);
        try{
            pb.start();
        }
        catch (Exception io){
            io.printStackTrace();
        }
    }

    public void buildCancelScript(String path){
        File bash = new File(path + properties.getCancelSwJobFile());
        FileWriter bashOut;
        try {
            bashOut = new FileWriter(bash);
            ObjectMapper objmp = new ObjectMapper();
            //grabbing script from system properties file.
            String script = String.format(properties.getCancelSwJobBash(), properties.getNetID(), properties.getNetID(),
                    properties.getPassword(), job.getSwJobNum());
            bashOut.write(script);

            bashOut.close();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getJobFromSeaWulf(int swjobID){
        this.job.setJobId(15);
        transferResultFiles(job);
        this.job.setJobStatus(JobStatus.POST_PROCESSING);
        List<DistrictingPlan> plans = new ArrayList<>();
        for (int i = 2; i < 3; i++){
            parseDataIntoRunResult(this.job, plans, i);
        }
        System.out.println("Created Run Results Object");
        RunResults rr = new RunResults(this.job, plans);
        postProcessSeaWulfJob(rr);
    }

    public void postProcessSeaWulfJob(RunResults rr){
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

//    public RunResults parseDataIntoRunResult(Job job){
//        System.out.println("Aggregating data from SeaWulf run results.");
//        List<Precinct> districtPrecincts = new ArrayList<>();
//        List<Precinct> ps = new ArrayList<>();
//        HashMap<String, Precinct> precinctHash = new HashMap<>();
//        for (Precinct p: ps){
//            precinctHash.put(p.getgeoid10(), p);
//        }
//        String path = "/Users/james/Documents/Code/University/416/416_Project/server/src/main/resources/static/NY_result.json";
//        try {
//            path = new String(Files.readAllBytes(Paths.get(path)));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        List<DistrictingPlan> plans = new ArrayList<>();
//        JSONObject obj = new JSONObject(path);
//        JSONArray features = obj.getJSONArray("data");
//        JSONArray districtingPlansJSON = features.getJSONObject(1).getJSONArray("districtingPlans");
//        //for each such districting, parse the districts
//        for (int i = 0; i < districtingPlansJSON.length(); i++) {
//            System.out.println("Parsing Districting Plan #" + (i+1));
//            JSONArray districtsJSON = districtingPlansJSON.getJSONObject(i).getJSONArray("districting");
//            List<District> districts = new ArrayList<>();
//            for (int k = 0; k < districtsJSON.length(); k++){
//                JSONObject district = districtsJSON.getJSONObject(k);
//                int id = district.getInt("DISTRICTID");
//                String state = job.getState().toString();
//                JSONArray precincts = district.getJSONArray("precincts");
//                districtPrecincts = new ArrayList<>();
//                for (int j = 0; j < precincts.length(); j++){
//                    JSONObject precinct = precincts.getJSONObject(j);
//                    HashMap<Demographic, Integer> mpop = new HashMap<>();
//                    if (job.getDemographicGroups().contains(Demographic.AFRICAN_AMERICAN))
//                        mpop.put(Demographic.AFRICAN_AMERICAN, precinct.getInt("BTOT"));
//                    if (job.getDemographicGroups().contains(Demographic.ASIAN))
//                        mpop.put(Demographic.ASIAN, precinct.getInt("AIANTOT"));
//                    if (job.getDemographicGroups().contains(Demographic.HISPANIC_LATINO))
//                        mpop.put(Demographic.HISPANIC_LATINO, precinct.getInt("HTOT"));
//                    if (job.getDemographicGroups().contains(Demographic.AM_INDIAN_AK_NATIVE))
//                        mpop.put(Demographic.AM_INDIAN_AK_NATIVE, precinct.getInt("ATOT"));
//                    if (job.getDemographicGroups().contains(Demographic.NH_OR_OPI))
//                        mpop.put(Demographic.NH_OR_OPI, precinct.getInt("NHTOT"));
//
//                    HashMap<Demographic, Integer> mVapPop = new HashMap<>();
//                    if (job.getDemographicGroups().contains(Demographic.AFRICAN_AMERICAN))
//                        mVapPop.put(Demographic.AFRICAN_AMERICAN, precinct.getInt("BVAP"));
//                    if (job.getDemographicGroups().contains(Demographic.ASIAN))
//                        mVapPop.put(Demographic.ASIAN, precinct.getInt("AIANVAP"));
//                    if (job.getDemographicGroups().contains(Demographic.HISPANIC_LATINO))
//                        mVapPop.put(Demographic.HISPANIC_LATINO, precinct.getInt("HVAP"));
//                    if (job.getDemographicGroups().contains(Demographic.AM_INDIAN_AK_NATIVE))
//                        mVapPop.put(Demographic.AM_INDIAN_AK_NATIVE, precinct.getInt("AVAP"));
//                    if (job.getDemographicGroups().contains(Demographic.NH_OR_OPI))
//                        mVapPop.put(Demographic.NH_OR_OPI, precinct.getInt("NHVAP"));
//                    Population pop = new Population(precinct.getInt("TOTAL"), precinct.getInt("TOTVAP"), mpop, mVapPop,
//                            String.valueOf(precinct.getInt("PRECINCTID")));
//                    Precinct p = new Precinct("", null, String.valueOf(precinct.getInt("GEOID10")),
//                            pop, null, id,  precinct.getString("COUNTY"));
//                    districtPrecincts.add(p);
//                }
//                List<Integer> districtNeighbors = new ArrayList<>();
//                JSONArray dNeigh = district.getJSONArray("DISTRICTNEIGHBORS");
//                for (int l = 0; l < dNeigh.length(); l++){
//                    districtNeighbors.add(dNeigh.getInt(l));
//                }
//                District d = new District(state, id, districtNeighbors, districtPrecincts, null);
//                d.setPercentVap(district.getDouble("MVAP"));
//                districts.add(d);
//            }
//            //District parsed. Put into DP
//            plans.add(new DistrictingPlan(job.getState(), districts, job.getPopEqThreshold(), job.getCompactness()));
//        }
//        //Create the RunResults object here.
//        System.out.println("Created Run Results Object");
//        RunResults rr = new RunResults(job, plans);
//        return rr;
//    }


    public void parseDataIntoRunResult(Job j, List<DistrictingPlan> plans, int i){
        System.out.println("Aggregating data from SeaWulf run results.");
        JsonFactory jsonfactory = new JsonFactory();
        File source = new File("/Users/james/Documents/Code/University/416/416_Project/server/src/main/resources/static/results/"
                + this.job.getState() + "_result" + i + ".json");
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
            int k = 0;
            while (parser.getCurrentToken() != JsonToken.END_ARRAY) {
                if (i == 2 && k == 120)
                    break;
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
                k++;
            }
            parser.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void transferResultFiles(Job job){
        System.out.println("Transferring Summary from SeaWulf for Job#"+job.getJobId());
        String path = System.getProperty("java.class.path").split("server")[0] + properties.getServerStaticWd();
        ProcessBuilder pb = new ProcessBuilder("expect", path + properties.getTransferSummaryBash());

        buildTransferScript(path, job);
        System.out.println("Sending files to SeaWulf. Expect a DUO Push...unless your on VPN :)");
        pb.directory(new File(path));
        pb.redirectErrorStream(true);
        try{
            pb.start();
        }
        catch (Exception io){
            io.printStackTrace();
        }
    }

    private void buildTransferScript(String path, Job job){
        File bash = new File(path + properties.getTransferResultBash());
        FileWriter bashOut;
        try {
            bashOut = new FileWriter(bash);
            ObjectMapper objmp = new ObjectMapper();
            //grabbing script from system properties file.
            String script = String.format(properties.getTransferResultFile(), properties.getNetID(), job.getJobId(),
                    properties.getNetID(), job.getJobId(),properties.getNetID(), job.getJobId(),properties.getNetID(),
                    job.getJobId(),properties.getNetID(), job.getJobId(),properties.getNetID(), job.getJobId(),
                    properties.getNetID(), properties.getPassword(), properties.getNetID(), properties.getPassword(),
                    properties.getNetID(), properties.getPassword(), properties.getNetID(), properties.getPassword(),
                    properties.getNetID(), properties.getPassword(), properties.getNetID(), properties.getPassword());
            bashOut.write(script);

            bashOut.close();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}
