package com.heartlink.charge.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heartlink.charge.model.dto.ChargeRequestDto;
import com.heartlink.charge.model.dto.ChargeResponseDto;
import com.heartlink.charge.model.mapper.ChargeMapper;
import com.heartlink.common.exception.CustomException;
import com.heartlink.member.model.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class ChargeService {

    private final ChargeMapper chargeMapper;
    private final RestTemplate restTemplate;

    @Value("${portone.api.secret}")
    private String portoneApiSecret;

    @Autowired
    public ChargeService(ChargeMapper chargeMapper, RestTemplate restTemplate) {
        this.chargeMapper = chargeMapper;
        this.restTemplate = restTemplate;
    }

// 자정마다 panding 상태의 결제 데이터 failed 로 변경
//    @Scheduled(fixedRate = 600000)  // 10분마다 실행 (밀리초 단위) 600.000
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void markOldPendingPaymentsAsFailed(){

        System.out.println("스케줄러 발생!");

        LocalDateTime limitMinute = LocalDateTime.now().minusMinutes(30);

        List<ChargeRequestDto> oldPendingPayments = chargeMapper.getOldPendingPayments(limitMinute);

        for (ChargeRequestDto payment : oldPendingPayments) {
            chargeMapper.failedPayment(payment.getPaymentNo());
        }

    }

    public MemberDto getUserIndfo(String userEmail){
        return chargeMapper.getUserIfo(userEmail);
    }

    public String getCurrentSequence() {

        try{
            int currentSequence =  chargeMapper.getCurrentSequence();

            String stringSequence = Integer.toString(currentSequence);

            // 현재시간 생성
            LocalDate SequenceNow = LocalDate.now();
            // SequenceNow포매팅
            String formatedNow = SequenceNow.format(DateTimeFormatter.ofPattern("yyMMdd"));

            // 최종 결제 번호 MID
            String resultMID = "";
            if(currentSequence < 10){
                resultMID = formatedNow + "-00" + stringSequence;
            }else if (10 <= currentSequence && currentSequence < 100){
                resultMID = formatedNow + "-0" + stringSequence;
            }else {
                resultMID = formatedNow + "-" + stringSequence;
            }

            return resultMID;
        }catch (DataAccessException e){
            // MyBatis는 SQLException을 DataAccessException 같은 런타임 예외로 변환
            // 로그 남기기
            return null;
        }

    }

    public int setPaymentHistory(ChargeRequestDto chargeRequestDto) {

        int insertPaymentHistory =  chargeMapper.setPaymentHistory(chargeRequestDto);

        return insertPaymentHistory;
    }

    public ChargeRequestDto getPaymentDetails(String paymentNo) {
        // 요청 보낼 API URL
        String url = "https://api.portone.io/payments/" + paymentNo;

        // 요청의 Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne " + portoneApiSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 포트원 API 호출
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        ChargeResponseDto paymentResponse;
        try {
            paymentResponse = objectMapper.readValue(responseEntity.getBody(), ChargeResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON 응답 구문 분석 실패: " + e.getMessage(), e);
        }

        String paymentResult = paymentResponse.getStatus();
        ChargeRequestDto responseChargeRequestDto = new ChargeRequestDto();

        if ("PAID".equals(paymentResult)) {
            responseChargeRequestDto.setPaymentNo(paymentResponse.getId());
            responseChargeRequestDto.setPaymentAmount(paymentResponse.getAmount().getTotalAmount());
            responseChargeRequestDto.setPaymentMethod(paymentResponse.getMethod().getProvider());
            responseChargeRequestDto.setOrderName(paymentResponse.getOrderName());
            responseChargeRequestDto.setPaymentUserEmail(paymentResponse.getCustomer().getEmail());

            return responseChargeRequestDto;
        }

        // paymentResult의 상태가 "PAID"가 아닐 경우 null을 반환
        return null;
    }

    public ChargeRequestDto setPaymentDbDetails(String paymentNo) {
        return chargeMapper.setPaymentDbDetails(paymentNo);
    }

    public int setPaymentState(ChargeRequestDto apiResponse) {
        return chargeMapper.setPaymentState(apiResponse);
    }

    public int setUserCoin(ChargeRequestDto dbResponse){
        return chargeMapper.setUserCoin(dbResponse);
    }

    public int getUserCoin(String userEmail){
        return chargeMapper.selectUserCoin(userEmail);
    }


    public List<ChargeRequestDto> getUserPaymentHistory(String userEmail){

        List<ChargeRequestDto> paymentHistoryDto = chargeMapper.getUserPaymentHistory(userEmail);

        int userCoin = chargeMapper.selectUserCoin(userEmail);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for(ChargeRequestDto item : paymentHistoryDto){
            int paymentCoin = item.getPaymentAmount() / 100 ;

            if(item.getPaymentState().equals("Completed")){
                if(userCoin >= paymentCoin) {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime paymentDate = LocalDateTime.parse(item.getPaymentDate(), formatter);
                    Duration duration = Duration.between(paymentDate, now);

                    if (Math.abs(duration.toDays()) < 7) {
                        item.setPaymentState("취소가능");
                    }else{
                        item.setPaymentState("취소불가");
                    }

                }else{
                    item.setPaymentState("취소불가");
                }

            }

        }

        return paymentHistoryDto;
    }

    public ChargeRequestDto getRequestPaymentInfo(String paymentNo){
        return chargeMapper.getRequestPaymentInfo(paymentNo);
    }

    public ChargeRequestDto setCancelQualificationVerifit(String userEmail, ChargeRequestDto requestInfo){
        ChargeRequestDto result = new ChargeRequestDto();

        // 해당 정보가 있는지
        if(Objects.isNull(requestInfo) || !requestInfo.getPaymentUserEmail().equals(userEmail)){
            result.setPaymentState("해당 결제 정보의 오류.");
            return result;
        }

        // 코인은 충분한지
        int userCoincnt = getUserCoin(userEmail);
        int userProductCoin = requestInfo.getPaymentProduct();

        if(userCoincnt < userProductCoin){
            result.setPaymentState("보유 코인이 부족합니다.");
            return result;
        }

        // 날짜는 유효한지
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime paymentDate = LocalDateTime.parse(requestInfo.getPaymentDate(), formatter);
        Duration duration = Duration.between(paymentDate, now);

        if (Math.abs(duration.toDays()) >= 7) {
            result.setPaymentState("취소 가능 날짜 만료");
            return result;
        }


        result.setPaymentUserEmail(userEmail);
        result.setPaymentProduct(userProductCoin);
        result.setPaymentState("취소 가능");

        return result;
    }

    @Transactional(rollbackFor = DataAccessException.class)
    public String setCancelRequest(String paymentNo, ChargeRequestDto requestDto){

        try {
            // 상태 업데이트
            int cancelRequestState = chargeMapper.paymentHistoryStateUpdate(paymentNo, "Cancel Requested");

            // 코인 갯수 업데이트
            int coindeduction = chargeMapper.setCoindeduction(requestDto.getPaymentUserEmail(), requestDto.getPaymentProduct());

            // 결제 취소 테이블 insert
            int cancelHistoryTable = chargeMapper.setCanceledHistory(paymentNo);

            if(cancelRequestState != 1 || coindeduction != 1){
                return "업데이트 실패";
            }

            return "update complete";

        }catch (Exception e){
            throw new CustomException("오류 발생 : " + e, e);
        }

    }

}
