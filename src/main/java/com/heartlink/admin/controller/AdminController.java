package com.heartlink.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/login")
    public String moveLoginPage(){
        return "admin/admin_user/admin_login";
    }

    @GetMapping("/register")
    public String moveRegisterPage(){
        return "admin/admin_user/admin_register";
    }

    @GetMapping("/main")
    public String moveMain(){
        return "admin/dashboard/admin_main";
    }

    @GetMapping("/inquiries")
    public String moveInquiriesPage(){
        return "admin/pages/admin_inquiries";
    }

    @GetMapping("/inquiry/response")
    public String moveInquiryResponsePage(){
        return "admin/pages/admin_inquiry_response";
    }

    @GetMapping("/reports")
    public String moveReportPage(){
        return "admin/pages/admin_reports";
    }

    @GetMapping("/report/action")
    public String moveReportActionPage(){
        return "admin/pages/admin_report_action";
    }

    @GetMapping("/user/search")
    public String moveUserSearchPage(){
        return "admin/pages/admin_user_search";
    }

    @GetMapping("/user/status")
    public String moveUserStatusPage(){
        return "admin/pages/admin_user_status";
    }

    @GetMapping("/payments")
    public String movePaymentsPage(){
        return "admin/pages/admin_payments";
    }

    @GetMapping("/refunds")
    public String moveRefundsPage(){
        return "admin/pages/admin_refunds";
    }
}
