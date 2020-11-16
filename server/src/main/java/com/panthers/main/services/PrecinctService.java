package com.panthers.main.services;

import com.panthers.main.dataAccess.PrecinctData;
import org.springframework.stereotype.Service;

@Service
/**
 * Services are in charge of business logic (algorithms, getting data, etc etc)
 * Precinct service would then get the available precincts from the database/storage.
 *
 */
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
//            Population ed = new Population(population, votingAgePopulation, demographics, demographicVAP);
//            List<Precinct> conn = new ArrayList<>();
//            Precinct p = new Precinct(name, conn, id, ed);
//            precinctData.addPrecinct(p);
            return 1;
    }

}
