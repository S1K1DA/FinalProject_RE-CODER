package com.heartlink.charge.contoller;

import com.heartlink.charge.model.dto.ChargeRequestDto;
import com.heartlink.charge.model.service.ChargeService;
import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.util.JwtUtil;
import com.heartlink.common.pagination.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/charge")
public class ChargeController {

    private final ChargeService chargeService;
    private final Pagination pagination;
    private final JwtUtil jwtUtil;

    @Autowired
    public ChargeController(ChargeService chargeService,
                            Pagination pagination,
                            JwtUtil jwtUtil){
        this.chargeService = chargeService;
        this.pagination = pagination;
        this.jwtUtil = jwtUtil;

    }


    // SecurityContext에서 userId 가져오기
    private String getCurrentUserEmail() {
        String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return jwtUtil.getEmailFromToken(jwt);
    }

    @GetMapping("/shop")
    public String moveMain(Model model, HttpServletRequest request){

        String userEmail = getCurrentUserEmail();

        MemberDto userInfo = chargeService.getUserIndfo(userEmail);

        model.addAttribute("userEmail",userEmail);
        model.addAttribute("userName",userInfo.getName());
        model.addAttribute("userTelnum",userInfo.getPhoneNumber());

        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        return "mypage/mypage_coin_charge/charge-main";
    }

    @GetMapping("/history")
    public String cashpage(@RequestParam(name="page", defaultValue = "1") int page,
                           Model model, HttpServletRequest request) {

        int pageSize = 5;

        String userEmail = getCurrentUserEmail();

        List<ChargeRequestDto> userPaymentHistory = chargeService.getUserPaymentHistory(userEmail);

        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, userPaymentHistory);

        model.addAttribute("userPaymentHistory", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("startPage", paginationData.get("startPage"));
        model.addAttribute("endPage", paginationData.get("endPage"));
        model.addAttribute("paginationUrl", "/charge/history");

        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);

        return "mypage/mypage_coin_charge/charge-history";
    }



    @PostMapping("/payment-order")
    @ResponseBody
    public ResponseEntity<String> getSequence(@RequestBody ChargeRequestDto chargeRequestDto, HttpServletRequest request){

        String referer = request.getHeader("Referer");
        System.out.println("referer: " + referer);

        String thisSequence = chargeService.getCurrentSequence();
        String userEmail = getCurrentUserEmail();
        chargeRequestDto.setPaymentUserEmail(userEmail);

        if(Objects.isNull(thisSequence)){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database 조회 실패");
        }

        chargeRequestDto.setPaymentState("Pending");
        chargeRequestDto.setPaymentMethod("NONE");
        chargeRequestDto.setPaymentNo(thisSequence);

        int setPaymentHistory = chargeService.setPaymentHistory(chargeRequestDto);

        if(setPaymentHistory == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database 데이터 삽입 실패");
        }

        return ResponseEntity.status(HttpStatus.OK).body(thisSequence);
    }

    @PostMapping("/complete")
    @ResponseBody
    public ResponseEntity<?> completePayment(@RequestBody ChargeRequestDto chargeRequestDto, HttpServletRequest request) {
        String paymentNo = chargeRequestDto.getPaymentNo();

        try {
            // API 결제 상세 정보 조회
            ChargeRequestDto apiResponse = chargeService.getPaymentDetails(paymentNo);

            // DB에 저장된 pending 상태의 결제정보 조회
            ChargeRequestDto dbResponse = chargeService.setPaymentDbDetails(paymentNo);


            // 결제 정보가 없는 경우
            if (Objects.isNull(apiResponse)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API 결제상세 정보 조회 실패, 결제번호:" + paymentNo);
            }else if(Objects.isNull(dbResponse)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DB 결제상세 정보 조회 실패, 결제번호:" + paymentNo);
            }

            int apiAmount = apiResponse.getPaymentAmount();
            int dbAmount = dbResponse.getPaymentAmount();

            String apiOrderName = apiResponse.getOrderName();
            String dbOrderName = "하트 코인 "+Integer.toString(dbResponse.getPaymentProduct())+ "개";

            String apiUserEmail = apiResponse.getPaymentUserEmail();
            String dbUsserEmail = dbResponse.getPaymentUserEmail();

            // api와 db의 응답 값 비교 (결제금액, 결제 상품명, 결제요청 고객)
            if(apiAmount == dbAmount && apiOrderName.equals(dbOrderName) && apiUserEmail.equals(dbUsserEmail)) {

               apiResponse.setPaymentState("Completed");
               apiResponse.setPaymentReference("Success");
               int updatePaymentState = chargeService.setPaymentState(apiResponse);

               int insertUserCoin = chargeService.setUserCoin(dbResponse);

               return ResponseEntity.ok(apiResponse);

            }else{
                dbResponse.setPaymentState("Failed");
                dbResponse.setPaymentReference("Failed : api와 db응답 값 비교 실패");
                int updatePaymentState = chargeService.setPaymentState(dbResponse);
            }


            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("API와 DB의 결제정보 불일치, 결제번호: " + paymentNo);

            // 정상 응답 반환
        } catch (Exception e) {
            System.out.println("예외 반환 : "+ e.getMessage());
            // 예외 발생 시 오류 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception, 예외 발생 : " + e.getMessage());
        }
    }


    @PostMapping("/payment-cancel")
    @ResponseBody
    public ResponseEntity<?> canceledPayment(@RequestBody ChargeRequestDto requestDto){


        String paymentNo = requestDto.getPaymentNo();
        String userEmail = getCurrentUserEmail();

        ChargeRequestDto requestInfo = chargeService.getRequestPaymentInfo(paymentNo);

        ChargeRequestDto cancelVerifit = chargeService.setCancelQualificationVerifit(userEmail, requestInfo);

        String result = cancelVerifit.getPaymentState();

        if(result.equals("취소 가능")){
            String cancelRequset = chargeService.setCancelRequest(paymentNo, cancelVerifit);

            if(cancelRequset.equals("update complete")){
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cancelRequset);

            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }


}
