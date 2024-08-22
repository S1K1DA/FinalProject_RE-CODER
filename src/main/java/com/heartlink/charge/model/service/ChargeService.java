package com.heartlink.charge.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heartlink.charge.model.dto.ChargeRequestDto;
import com.heartlink.charge.model.dto.ChargeResponseDto;
import com.heartlink.charge.model.mapper.ChargeMapper;
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
//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(fixedRate = 600000)  // 10분마다 실행 (밀리초 단위)
    @Transactional
    public void markOldPendingPaymentsAsFailed(){
        LocalDateTime limitMinute = LocalDateTime.now().minusMinutes(30);

        List<ChargeRequestDto> oldPendingPayments = chargeMapper.getOldPendingPayments(limitMinute);

        for (ChargeRequestDto payment : oldPendingPayments) {
            chargeMapper.failPayment(payment.getPaymentNo());
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
            responseChargeRequestDto.setPaymentProduct(paymentResponse.getOrderName());

            return responseChargeRequestDto;
        }

        // paymentResult의 상태가 "PAID"가 아닐 경우 null을 반환
        return null;
    }

    public ChargeRequestDto setPaymentDbDetails(String paymentNo) {

        ChargeRequestDto resultDto = chargeMapper.setPaymentDbDetails(paymentNo);
        int coinCnt = resultDto.getPaymentAmount() / 100;
        resultDto.setPaymentCoin(coinCnt);

        return resultDto;
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

            }else if(item.getPaymentState().equals("Canceld")){
                item.setPaymentState("취소됨");
            }

        }

        return paymentHistoryDto;
    }

    public ChargeRequestDto getRequestPaymentInfo(String paymentNo){
        return chargeMapper.getRequestPaymentInfo(paymentNo);
    }



    public String setPortOneRequestCancle(String paymentNo) {
        // 요청 보낼 API URL
        String url = "https://api.portone.io/payments/" + paymentNo + "/cancel";

        // 요청의 Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "PortOne " + portoneApiSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);  // JSON 형식으로 지정

        // 요청 본문
        String body = "{\"reason\": \"고객의 요청에 따라 취소됨\"}";

        // 본문이 있는 요청
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // 포트원 API 호출
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,  // POST 메서드를 사용
                    entity,
                    String.class
            );

            String responseBody = responseEntity.getBody();

            // JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode cancellationNode = rootNode.path("cancellation");

            // "status" 필드 추출
            String status = cancellationNode.path("status").asText();

            System.out.println(status);

            return status;

        } catch (HttpClientErrorException e) {
            // HTTP 오류 처리 및 로그 출력
            System.err.println("HTTP Error: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
        } catch (JsonProcessingException e) {
            // JSON 파싱 오류 처리
            System.err.println("JSON Processing Error: " + e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            System.err.println("Error: " + e.getMessage());
        }

        return null;
    }

    public int setPaymentWithCoinUpdate(String paymentNo, String userEmail, int userProduct){

        // canceled update
        int cancelState = chargeMapper.canceledState(paymentNo);

        if(cancelState == 1){
            int coindeduction = chargeMapper.setCoindeduction(userEmail, userProduct);

            if(coindeduction == 1){
                return coindeduction;
            }
        }

        return 0;
    }

}
