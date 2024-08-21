package com.heartlink.admin.controller;

import com.heartlink.admin.model.dto.AdminMemberDto;
import com.heartlink.admin.model.service.AdminMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminMemberService memberService;

    @Autowired
    public AdminController(AdminMemberService memberService){
        this.memberService = memberService;
    }


    @GetMapping("/login")
    public String moveLoginPage(){
        return "/admin/admin_user/admin-login";
    }

    @GetMapping("/register")
    public String moveRegisterPage(){
        return "admin/admin_user/admin-register";
    }

    @GetMapping("/main")
    public String moveMain(){
        return "admin/dashboard/admin-main";
    }

    @GetMapping("/inquiries")
    public String moveInquiriesPage(){
        return "admin/pages/admin-inquiries";
    }

    @GetMapping("/inquiry/response")
    public String moveInquiryResponsePage(){
        return "admin/pages/admin-inquiry-response";
    }

    @GetMapping("/reports")
    public String moveReportPage(){
        return "admin/pages/admin-reports";
    }

    @GetMapping("/report/action")
    public String moveReportActionPage(){
        return "admin/pages/admin-report-action";
    }

    @GetMapping("/user/search")
    public String moveUserSearchPage(Model model){

        List<AdminMemberDto> userList = memberService.getAllUser();

        model.addAttribute("userList", userList);

        return "admin/pages/admin-user-search";
    }

    @GetMapping("/user/status")
    public String moveUserStatusPage(){
        return "admin/pages/admin-user-status";
    }

    @GetMapping("/payments")
    public String movePaymentsPage(){
        return "admin/pages/admin-payments";
    }

    @GetMapping("/refunds")
    public String moveRefundsPage(){
        return "admin/pages/admin-refunds";
    }
}
