package com.heartlink.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionController {

    @GetMapping("/error-page")
    public String errorPage(HttpServletRequest request, Model model) {
        String errorMessage = (String) request.getAttribute("errorMessage");
        String refererUrl = request.getHeader("Referer");

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("redirectUrl", refererUrl);
        return "common/error";
    }
}
