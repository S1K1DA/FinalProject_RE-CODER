package com.heartlink.admin.controller;

import com.heartlink.admin.model.dto.AdminInfoDto;
import com.heartlink.admin.model.dto.AdminReportDto;
import com.heartlink.admin.model.dto.MemberListDto;
import com.heartlink.admin.model.dto.PaymentHistoryDto;
import com.heartlink.admin.model.service.AdminMemberService;
import com.heartlink.admin.model.service.AdminPaymentService;
import com.heartlink.admin.model.service.AdminReportService;
import com.heartlink.member.model.dto.AdminDto;
import com.heartlink.member.model.service.MemberService;
import com.heartlink.member.util.JwtUtil;
import com.heartlink.common.pagination.Pagination;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final Pagination pagination;
    private final AdminMemberService adminMemberService;
    private final MemberService memberService;
    private final AdminPaymentService adminPaymentService;
    private final JwtUtil jwtUtil;
    private final AdminReportService adminReportService;

    @Autowired
    public AdminController(Pagination pagination,
                           AdminMemberService adminMemberService,
                           MemberService memberService,
                           JwtUtil jwtUtil,
                           AdminPaymentService adminPaymentService,
                           AdminReportService adminReportService) {
        this.pagination = pagination;
        this.adminMemberService = adminMemberService;
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
        this.adminPaymentService = adminPaymentService;
        this.adminReportService = adminReportService;
    }

    private int getCurrentAdminNo() {
        String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return jwtUtil.getAdminNumberFromToken(jwt);
    }


    @GetMapping("/login")
    public String moveLoginPage(){
        return "/admin/admin_user/admin-login";
    }

    @PostMapping("/login/authentication")
    public ResponseEntity<?> loginAdminMember(AdminInfoDto adminInfoDto,
                                           HttpServletResponse response){

        // 2. 어드민 로그인 처리
        AdminInfoDto admin = adminMemberService.verifyAdminLogin(adminInfoDto);

        if (!Objects.isNull(admin)) {

            if(admin.getAdminConsent().equals("N")){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("권한이 없습니다.");
            }
            // 로그인 성공 시 JWT 토큰 생성 (어드민)
            String accessToken = jwtUtil.generateAdminToken(admin.getAdminUserEmail(), admin.getAdminUserNo());
            String refreshToken = jwtUtil.generateRefreshToken(admin.getAdminUserEmail(), admin.getAdminUserNo());

            // JWT를 쿠키에 저장
            Cookie jwtCookie = new Cookie("adminToken", accessToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(86400);  // 쿠키 만료 시간 (예: 1일)
            response.addCookie(jwtCookie);

            return ResponseEntity.status(HttpStatus.OK).build(); // 어드민 로그인 성공 메시지 반환
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 또는 비밀번호가 틀렸습니다."); // 로그인 실패 메시지 반환
    }

    @GetMapping("/register")
    public String moveRegisterPage(){
        return "admin/admin_user/admin-register";
    }

    @PostMapping("/register/submit")
    public String registerAdminMember(AdminInfoDto adminInfoDto,
                                      BindingResult bindingResult,
                                      Model model){

        System.out.println(adminInfoDto.toString());

        if(bindingResult.hasErrors()){
            return "redirect:/admin/register";
        }

        if (!adminInfoDto.getAdminUserPwd().equals(adminInfoDto.getAdminUserPwdConfirm())){
            model.addAttribute("error", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "redirect:/admin/register";
        }

        int inputAdminUser = adminMemberService.setAdminMember(adminInfoDto);

        if(inputAdminUser != 1){
            return null;
        }

        model.addAttribute("message", "회원가입이 성공적으로 완료되었습니다.");
        return "redirect:/admin/login"; // 회원가입 후 로그인 페이지로 리디렉션

    }

    @GetMapping("/register/verifit")
    @ResponseBody
    public String checkAdminVerifit(AdminInfoDto adminInfoDto){
        String adminEmail = adminInfoDto.getAdminUserEmail();

        if(adminMemberService.checkEmailVerifit(adminEmail)){
            return "이미 사용 중인 이메일입니다.";
        }
        return "사용 가능한 이메일입니다.";
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
    public String moveReportPage(Model model,
                                 @RequestParam(name="page", defaultValue = "1") int page,
                                 @RequestParam(value = "startDate", required = false) String startDate,
                                 @RequestParam(value = "endDate", required = false) String endDate){
        int pageSize = 9;

        LocalDate today = LocalDate.now();

        if (startDate == null || endDate == null) {
            if (startDate == null) {
                startDate = today.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            if (endDate == null) {
                endDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }

        List<AdminReportDto> resolutionReportList = adminReportService.setResolutionReportList(startDate, endDate);

        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, resolutionReportList);

        model.addAttribute("resolutionList", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("startPage", paginationData.get("startPage"));
        model.addAttribute("endPage", paginationData.get("endPage"));
        model.addAttribute("paginationUrl", "/admin/reports");

        model.addAttribute("startDate", startDate); // 사용자가 선택한 시작 날짜를 전달
        model.addAttribute("endDate", endDate);

        return "admin/pages/admin-reports";
    }

    @GetMapping("/report/action")
    public String moveReportActionPage(Model model,
                                       @RequestParam(name="page", defaultValue = "1") int page,
                                       @RequestParam(value = "startDate", required = false) String startDate,
                                       @RequestParam(value = "endDate", required = false) String endDate){

        int pageSize = 9;

        LocalDate today = LocalDate.now();

        if (startDate == null || endDate == null) {
            if (startDate == null) {
                startDate = today.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            if (endDate == null) {
                endDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }

        List<AdminReportDto> reportList = adminReportService.setReportList(startDate, endDate);

        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, reportList);

        model.addAttribute("reportList", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("startPage", paginationData.get("startPage"));
        model.addAttribute("endPage", paginationData.get("endPage"));
        model.addAttribute("paginationUrl", "/admin/report/action");

        model.addAttribute("startDate", startDate); // 사용자가 선택한 시작 날짜를 전달
        model.addAttribute("endDate", endDate);

        return "admin/pages/admin-report-action";
    }

    @PostMapping("/report/response")
    public ResponseEntity<?> isAdminReportResolution(AdminReportDto reportDto){

        int adminNo = getCurrentAdminNo();
        reportDto.setAdminUserNo(adminNo);

        String result = adminReportService.setAdminResolution(reportDto);

        if(result.equals("SUCCESS")){

            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @GetMapping("/user/search")
    public String moveUserSearchPage(Model model,
                                     @RequestParam(value = "filter", defaultValue = "ALL")String filter ,
                                     @RequestParam(name="page", defaultValue = "1") int page,
                                     @RequestParam(name="category", defaultValue = "") String category,
                                     @RequestParam(name="search", defaultValue = "") String search){

        int pageSize = 9;
        List<MemberListDto> userList = adminMemberService.getAllUser(filter,category,search);

        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, userList);

        model.addAttribute("userList", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("startPage", paginationData.get("startPage"));
        model.addAttribute("endPage", paginationData.get("endPage"));
        model.addAttribute("paginationUrl", "/admin/user/search");

        return "admin/pages/admin-user-search";
    }

    @GetMapping("/user/status")
    public String moveUserStatusPage(Model model,
                                     @RequestParam(value = "filter", defaultValue = "ALL")String filter ,
                                     @RequestParam(name="page", defaultValue = "1") int page,
                                     @RequestParam(name="category", defaultValue = "") String category,
                                     @RequestParam(name="search", defaultValue = "") String search){

        int pageSize = 9;

        List<MemberListDto> userStateList = adminMemberService.getAllUserState(filter,category,search);

        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, userStateList);

        model.addAttribute("stateList", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("startPage", paginationData.get("startPage"));
        model.addAttribute("endPage", paginationData.get("endPage"));
        model.addAttribute("paginationUrl", "/admin/user/search");

        return "admin/pages/admin-user-status";
    }

    @PostMapping("/user/status/change")
    public ResponseEntity<?> changeUserState(@RequestBody MemberListDto memberListDto){

        String userStateResult = adminMemberService.setChangeUserState(memberListDto);

        if(userStateResult.equals("SUCCESS")){
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userStateResult);
    }

    @GetMapping("/payments")
    public String movePaymentsPage(Model model,
                                   @RequestParam(name="page", defaultValue = "1") int page,
                                   @RequestParam(value = "startDate", required = false) String startDate,
                                   @RequestParam(value = "endDate", required = false) String endDate){

        int pageSize = 9;

        LocalDate today = LocalDate.now();

        if (startDate == null || endDate == null) {
            if (startDate == null) {
                startDate = today.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            if (endDate == null) {
                endDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }

        List<PaymentHistoryDto> paymentHistoryList = adminPaymentService.getAllPaymentHistory(startDate, endDate);

        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, paymentHistoryList);

        String pageUrl = "/admin/payments?startDate=" + startDate + "&endDate=" + endDate;

        model.addAttribute("paymentsHistory", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("startPage", paginationData.get("startPage"));
        model.addAttribute("endPage", paginationData.get("endPage"));
        model.addAttribute("paginationUrl", pageUrl);

        model.addAttribute("startDate", startDate); // 사용자가 선택한 시작 날짜를 전달
        model.addAttribute("endDate", endDate);

        return "admin/pages/admin-payments";
    }

    @GetMapping("/refunds")
    public String moveRefundsPage(Model model,
                                  @RequestParam(name="page", defaultValue = "1") int page,
                                  @RequestParam(value = "startDate", required = false) String startDate,
                                  @RequestParam(value = "endDate", required = false) String endDate){

        int pageSize = 9;

        LocalDate today = LocalDate.now();

        if (startDate == null || endDate == null) {
            if (startDate == null) {
                startDate = today.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            if (endDate == null) {
                endDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }

        List<PaymentHistoryDto> refundHistory = adminPaymentService.getRefundHistory(startDate, endDate);

        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, refundHistory);

        String pageUrl = "/admin/refunds?startDate=" + startDate + "&endDate=" + endDate;

        model.addAttribute("refundHistory", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("startPage", paginationData.get("startPage"));
        model.addAttribute("endPage", paginationData.get("endPage"));
        model.addAttribute("paginationUrl", pageUrl);

        model.addAttribute("startDate", startDate); // 사용자가 선택한 시작 날짜를 전달
        model.addAttribute("endDate", endDate);

        return "admin/pages/admin-refunds";
    }

    @PostMapping("/payment/cancel")
    public ResponseEntity<?> isPaymentResponseCancel(@RequestBody Map<String, String> data){
        String paymentNo = data.get("paymentNo");

        String portOneRequest = adminPaymentService.setPortOneRequestCancle(paymentNo);

        if(portOneRequest.equals("SUCCEEDED")){
            int userUpdate = adminPaymentService.updateCanceledPaymentHistory(paymentNo, "cancel");

            if(userUpdate == 1){
                return ResponseEntity.status(HttpStatus.OK).body("결제 취소 완료");
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/payment/denied")
    public ResponseEntity<?> isPaymentResponseDenied(@RequestBody Map<String, String> data){
        String paymentNo = data.get("paymentNo");

        int cancelDenied = adminPaymentService.updateCanceledPaymentHistory(paymentNo, "denied");

        if(cancelDenied != 1){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
