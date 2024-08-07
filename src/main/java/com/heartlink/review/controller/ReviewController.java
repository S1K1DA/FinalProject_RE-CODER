package com.heartlink.review.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @GetMapping("/photomain")
    public String photoMain() { return "/review/review_photo/photo-main"; }

    @GetMapping("/photoenroll")
    public String photoEnroll(){ return "/review/review_photo/photo-enroll"; }

    @GetMapping("/photoedit")
    public String photoEdit(){ return "/review/review_photo/photo-edit"; }

    @GetMapping("/photodetail")
    public String photoDetail(){ return "/review/review_photo/photo-detail"; }

    @GetMapping("/livemain")
    public String liveMain(){ return "/review/live-main"; }

}
