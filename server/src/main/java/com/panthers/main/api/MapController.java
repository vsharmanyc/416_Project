package com.panthers.main.api;

import com.panthers.main.jobModel.DistrictingType;
import com.panthers.main.mapModel.States;
import com.panthers.main.requestDto.StateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.panthers.main.services.MapHandler;

/**
 * Class functions as controller for API/REST calls to get district/precint data, election data, etc etc (things
 * needed by the map)
 */
@RestController
@RequestMapping("/api/map")
public class MapController {
    private MapHandler mapHandler;

    @Autowired
    public MapController(MapHandler mapHandler) {
        this.mapHandler = mapHandler;
    }

//    @GetMapping
//    public String handleGetPrecincts(){
//        return null;
//    }

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
    public ResponseEntity handleChangeState(@RequestBody StateDto dto){
        String state = dto.getState();
        switch (state) {
            case "NY":
                mapHandler.changeState(States.NY);
                return new ResponseEntity(HttpStatus.ACCEPTED);
            case "PA":
                mapHandler.changeState(States.PA);
                return new ResponseEntity(HttpStatus.ACCEPTED);
            case "MD":
                mapHandler.changeState(States.MD);
                return new ResponseEntity(HttpStatus.ACCEPTED);
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
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
