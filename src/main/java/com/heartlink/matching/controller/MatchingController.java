package com.heartlink.matching.controller;

import com.heartlink.matching.model.dto.MatchingDto;
import com.heartlink.matching.model.service.MatchingService;
import com.heartlink.member.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/matching")
public class MatchingController {

    private final JwtUtil jwtUtil;
    private final MatchingService matchingService;

    @Autowired
    public MatchingController(JwtUtil jwtUtil, MatchingService matchingService){
        this.jwtUtil = jwtUtil;
        this.matchingService = matchingService;
    }

    // SecurityContext에서 userNo 가져오기
    private int getCurrentUserNo() {
        String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return jwtUtil.getUserNumberFromToken(jwt);
    }

    @PostMapping("/request")
    public ResponseEntity<?> setMatchingRequest(@RequestBody MatchingDto matchingDto){

        int matchingUserNo = getCurrentUserNo();
        int matchedUserNo = matchingDto.getMatchedUserNo();

        String matchingRequest = matchingService.setMatchingRequest(matchingUserNo, matchedUserNo);

        if(matchingRequest.equals("success")){
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(matchingRequest);
    }
}
