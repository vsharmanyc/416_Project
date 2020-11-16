package com.panthers.main.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panthers.main.jobModel.DistrictingType;
import com.panthers.main.mapModel.District;
import com.panthers.main.mapModel.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Second in line from the MapController, the MapHandler handles business logic of getting districts, precincts,
 * generating a heat map, changing states, etc.
 */
@Service
public class MapHandler {
    private States state;

    @Autowired
    public MapHandler() {
        this.state = null;
    }

    /* GETTERS/SETTERS */
    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    /* FUNCTIONS */
    /**
     *
     * @return returns the states current districting plan
     */
    public String requestDistricts(){
        // MODIFY PATH HERE TO FIT YOUR LOCAL MACHINE!!!
        String path = "/Users/james/Documents/Code/University/416_Project/server/src/main/resources/static/";
        States s = getState();

        // Determines which precinct json to load
        switch (s){
            case NY:
                path += "NY_Districts.json";
                break;
            case MD:
                path += "MD_Districts.json";
                break;
            case PA:
                path += "PA_Districts.json";
                break;
        }
        try{
            path = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return path;
    }

    /**
     *
     * @return returns the states current precinct plan
     */
    public String requestPrecincts(){
        // MODIFY PATH HERE TO FIT YOUR LOCAL MACHINE!!!
        String path = "/Users/james/Documents/Code/University/416_Project/server/src/main/resources/static/";
        States s = getState();

        // Determines which precinct json to load
        switch (s){
            case NY:
                path += "NY_Precincts.json";
                break;
            case MD:
                path += "MD_Precincts.json";
                break;
            case PA:
                path += "PA_Precincts.json";
                break;
        }
        try{
            path = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return path;
    }

    /**
     * changes the state our application is focused on
     * @param state state we are changing view to.
     */
    public void changeState(States state){
        this.setState(state);
    }

    /**
     * @return returns requested districting type
     */
    public DistrictingType requestDistricting(String districtingType){
        return null;
    }

    /**
     *
     * @return returns the districting type for the state the view is focused on
     */
    public Map<Integer, Integer> generateHeatMap(){
        return null;
    }

    public Object requestState(String state){
        switch(state){
            case "NY":
                changeState(States.NY);
                break;
            case "PA":
                changeState(States.PA);
                break;
            case "MD":
                changeState(States.MD);
                break;
            default:
                return new Object();
        }
        States s = getState();
        // Parsing state from JSON object
        ObjectMapper mapper = new ObjectMapper();

        return null;
    }




}
