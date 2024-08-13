package com.heartlink.charge.contoller;

import com.heartlink.charge.model.dto.ChargeRequestDto;
import com.heartlink.charge.model.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/charge")
public class ChargeController {

    private final ChargeService chargeService;

    @Autowired
    public ChargeController(ChargeService chargeService){
        this.chargeService = chargeService;
    }

    @GetMapping("/shop")
    public String moveMain(){
        return "mypage/mypage_coin_charge/charge-main";
    }

    @GetMapping("/history")
    public String cashpage() {
        return "mypage/mypage_coin_charge/charge-history";
    }



    @PostMapping("/payment-order")
    @ResponseBody
    public ResponseEntity<String> getSequence(@RequestBody ChargeRequestDto chargeRequestDto){
        String thisSequence = chargeService.getCurrentSequence();

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
    public ResponseEntity<?> completePayment(@RequestBody ChargeRequestDto chargeRequestDto) {

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

            String apiProduct = apiResponse.getPaymentProduct();
            String dbProduct = dbResponse.getPaymentProduct();


            // api와 db의 응답 값 비교 (결제금액, 결제 상품명, 결제요청 고객)
            if(apiAmount == dbAmount && apiProduct.equals(dbProduct)) {

               apiResponse.setPaymentState("Completed");
               apiResponse.setPaymentReference("Success");
               int updatePaymentState = chargeService.setPaymentState(apiResponse);

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


}
