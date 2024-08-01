package com.heartlink.review.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @GetMapping("/photomain")
    public String photomain() { return "/review/photo-main"; }

    @GetMapping("/photoenroll")
    public String photoenroll(){ return "/review/photo-enroll"; }

    @GetMapping("/photoedit")
    public String photoedit(){ return "/review/photo-edit"; }

    @GetMapping("/photodetail")
    public String photodetail(){ return "/review/photo-detail"; }
}
