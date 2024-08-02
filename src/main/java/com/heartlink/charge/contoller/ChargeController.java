package com.heartlink.charge.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/charge")
public class ChargeController {

    @GetMapping("/shop")
    public String moveMain(){
        return "coin_charge/charge-main";
    }

    @GetMapping("/history")
    public String cashpage() {
        return "coin_charge/charge-history";
    }
}
