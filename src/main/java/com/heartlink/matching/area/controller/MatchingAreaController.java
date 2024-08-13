package com.heartlink.matching.area.controller;

import com.heartlink.matching.area.model.service.MatchingAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/matching-area")
public class MatchingAreaController {

    private final MatchingAreaService areaService;

    @Autowired
    public MatchingAreaController(MatchingAreaService areaService){
        this.areaService = areaService;
    }


    @GetMapping("/main")
    public String moveMain(){
        return "matching/area_based/matching_main";
    }

    @PostMapping("/location")
    @ResponseBody
    public String setLocation(@RequestParam int basicUserNo){

        String takeLocation = areaService.setUserLocation(basicUserNo);

        if(Objects.isNull(takeLocation)){
            return "failed";
        }

        return takeLocation;
    }

}
