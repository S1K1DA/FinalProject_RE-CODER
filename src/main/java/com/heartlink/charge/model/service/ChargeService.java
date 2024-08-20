package com.heartlink.charge.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heartlink.charge.model.dto.ChargeRequestDto;
import com.heartlink.charge.model.dto.ChargeResponseDto;
import com.heartlink.charge.model.mapper.ChargeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        return chargeMapper.setPaymentHistory(chargeRequestDto);
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
        return chargeMapper.setPaymentDbDetails(paymentNo);
    }

    public int setPaymentState(ChargeRequestDto apiResponse) {
        return chargeMapper.setPaymentState(apiResponse);
    }
}
