package api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import services.PrecinctService;

/**
 * Class functions as controller for API/REST calls to get district/precint data, election data, etc etc (things
 * needed by the map)
 */
@RestController
public class MapController {
    private PrecinctService precinctService;

    @Autowired
    public MapController(PrecinctService precinctService) {
        this.precinctService = precinctService;
    }

    @PostMapping
    public void addPrecinct(){

    }
    @PostMapping
    public void addElectionData(){

    }

    public void getDistrictings(){

    }
}
