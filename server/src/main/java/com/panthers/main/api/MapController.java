package com.panthers.main.api;

import com.panthers.main.mapmodel.States;
import com.panthers.main.requestdto.StateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.panthers.main.handler.MapHandler;

/**
 * Class functions as controller for API/REST calls to get district/precint data, election data, etc etc (things
 * needed by the map)
 */
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/map")
public class MapController {
    private MapHandler mapHandler;

    @Autowired
    public MapController(MapHandler mapHandler) {
        this.mapHandler = mapHandler;
    }

    @GetMapping("/getPrecincts")
    @ResponseBody
    public String handleGetPrecincts(){
        return mapHandler.requestPrecincts();
    }

    /**
     * in charge of returning precints for the state we are looking at.
     * @return returns the JSON of the current districting
     */
    @GetMapping("/getDistricts")
    @ResponseBody
    public String handleGetDistricts(){
        return mapHandler.requestDistricts();
    }

//    /**
//     * pulls state from storage, and returns it. Returns state object, complete with districts and precincts.
//     * @return returns the JSON of the state representation
//     */
//    @GetMapping("/getState")
//    public Object handleGetState(@RequestParam String state){
//        return mapHandler.requestState(state);
//    }

    /**
     * Changes the state to reflect clients view change
     * @param dto dto for state to change the view from
     * @return returns a HTTP success code if successful, failure code if not.
     */
    @PostMapping("/changeState")
    @ResponseBody
    public String handleChangeState(@RequestBody StateDto dto){
        String state = dto.getState();
        switch (state) {
            case "NY":
                mapHandler.changeState(States.NY);
                return "{}";
            case "PA":
                mapHandler.changeState(States.PA);
                return "{}";
            case "MD":
                mapHandler.changeState(States.MD);
                return "{}";
            default:
                return "{}";
        }
    }

    @GetMapping("/getState")
    public String handleGetState(){
        return mapHandler.getState().name();
    }
//
//    public String handleGetNewDistrictings(DistrictingType type){
//        return null;
//    }

}
