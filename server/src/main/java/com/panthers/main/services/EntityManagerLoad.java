package com.panthers.main.services;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.panthers.main.mapModel.Demographic;
import com.panthers.main.mapModel.Population;
import com.panthers.main.mapModel.Precinct;
import com.panthers.main.mapModel.States;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.AutoPopulatingList;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class EntityManagerLoad {
    private List<Precinct> precincts;

    public EntityManagerLoad(List<Precinct> precincts) {
        this.precincts = precincts;
    }

    public EntityManagerLoad() {
        this.precincts = new ArrayList<>();
    }

    public void loadEM(){
        String path = "/Users/james/Documents/Code/University/416_Project/server/src/main/resources/static/MD_Precincts_data.json";
        try{
            path = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException ex){
            ex.printStackTrace();
        }
        JSONObject obj = new JSONObject(path);
        JSONArray features = obj.getJSONArray("precincts");
        for(int i = 0; i < features.length(); i++){
            JSONObject precinct = features.getJSONObject(i);
            String name = precinct.getString("PRECINCT");
            int population = precinct.getInt("TOTAL");
            int vap = precinct.getInt("TOTVAP");
            HashMap<Demographic, Integer> mpop = new HashMap<>();
            mpop.put(Demographic.WHITE, precinct.getInt("WTOT"));
            mpop.put(Demographic.AFRICAN_AMERICAN, precinct.getInt("BTOT"));
            mpop.put(Demographic.ASIAN, precinct.getInt("ATOT"));
            mpop.put(Demographic.AM_INDIAN_AK_NATIVE, precinct.getInt("AIANTOT"));
            mpop.put(Demographic.HISPANIC_LATINO, precinct.getInt("HLTOT"));
            mpop.put(Demographic.NH_OR_OPI, precinct.getInt("NHOPTOT"));

            HashMap<Demographic, Integer> mvappop = new HashMap<>();
            mvappop.put(Demographic.WHITE, precinct.getInt("WVAP"));
            mvappop.put(Demographic.AFRICAN_AMERICAN, precinct.getInt("BVAP"));
            mvappop.put(Demographic.ASIAN, precinct.getInt("AVAP"));
            mvappop.put(Demographic.AM_INDIAN_AK_NATIVE, precinct.getInt("AIANVAP"));
            mvappop.put(Demographic.HISPANIC_LATINO, precinct.getInt("HLVAP"));
            mvappop.put(Demographic.NH_OR_OPI, precinct.getInt("NHOPVAP"));

            String precinctID = precinct.getString("PRECINCTID");


            Population pop = new Population(population, vap, mpop, mvappop, precinctID);
            Precinct p = new Precinct(precinct.getString("PRECINCTNAME"), new ArrayList<Precinct>(), precinctID,
                     pop, population, vap, mpop, mvappop, null, precinct.getInt("DISTRICTID"));
            precincts.add(p);
        }

//        System.out.println(precincts.get(120).toString());
    }
}
