package com.panthers.main.services;

import com.panthers.main.jobModel.DistrictingType;
import com.panthers.main.mapModel.District;
import com.panthers.main.mapModel.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<District> requestDistricts(){
        return null;
    }

    /**
     *
     * @return returns the states current precinct plan
     */
    public List<District> requestPrecincts(){
        return null;
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

}
