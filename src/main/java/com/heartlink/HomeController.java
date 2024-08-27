package com.heartlink;

import com.heartlink.matching.model.dto.MatchingAlarmDto;
import com.heartlink.matching.model.service.MatchingService;
import com.heartlink.member.util.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final MatchingService matchingService;
    private final JwtUtil jwtUtil;

    public HomeController(MatchingService matchingService, JwtUtil jwtUtil){
        this.matchingService = matchingService;
        this.jwtUtil = jwtUtil;
    }

    // SecurityContext에서 userNo 가져오기
    private int getCurrentUserNo() {
        String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return jwtUtil.getUserNumberFromToken(jwt);
    }

    @GetMapping("/")
    public String home(Model model) {




        return "index";  // "index"는 src/main/resources/templates/admin-main.html
    }
}
