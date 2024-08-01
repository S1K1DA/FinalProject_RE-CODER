package com.heartlink.feed.cotroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/feed")
public class FeedController {

    @GetMapping("")
    public String feed() {
        return "/feed/feedmain";  // "index"는 src/main/resources/templates/index.html
    }

    @GetMapping("/write")
    public String newFeed(){
        return "/feed/feedwrite";
    }
}
