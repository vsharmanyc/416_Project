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
//
//    /**
//     * in charge of returning precints for the state we are looking at.
//     * @return returns the JSON of the current districting
//     */
//    @GetMapping
//    public String handleGetDistricts(){
//        return null;
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

        if (state.equals("NY")){
            mapHandler.changeState(States.NY);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        else if (state.equals("PA")){
            mapHandler.changeState(States.PA);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        else if (state.equals("MD")){
            mapHandler.changeState(States.MD);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
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
