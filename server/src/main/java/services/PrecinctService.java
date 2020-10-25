package services;

import dataAccess.PrecinctData;
import mapModel.Demographic;
import mapModel.ElectionData;
import mapModel.Precinct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PrecinctService {

    private PrecinctData precinctData;

    public PrecinctService(PrecinctData precintData) {
        this.precinctData = precintData;
    }

    public int addPrecinct(String name, int id, int population, int votingAgePopulation,
                           int minorityPopulation, int minorityVAPopulation){
//            HashMap<Demographic, Integer> demographics = new HashMap<>();
//            HashMap<Demographic, Integer> demographicVAP = new HashMap<>();
//            demographics.put(Demographic.AFRICAN_AMERICAN, minorityPopulation);
//            demographicVAP.put(Demographic.AFRICAN_AMERICAN, minorityVAPopulation);
//
//            ElectionData ed = new ElectionDatas(population, votingAgePopulation, demographics, demographicVAP);
//            List<Precinct> conn = new ArrayList<>();
//            Precinct p = new Precinct(name, conn, id, ed);
//            precinctData.addPrecinct(p);
            return 1;
    }

}
