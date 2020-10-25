package dataAccess;

import mapModel.Demographic;
import mapModel.ElectionData;
import mapModel.Precinct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class PrecinctData {
    private static List<Precinct> precincts;

    public PrecinctData() {
        this.precincts = new ArrayList<Precinct>();
    }

    public int addPrecinct(Precinct p){
       precincts.add(p);
       return 1;
    }
}
