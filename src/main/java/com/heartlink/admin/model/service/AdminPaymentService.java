package com.heartlink.admin.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.client.HttpClientErrorException;
import com.heartlink.admin.model.dto.PaymentHistoryDto;
import com.heartlink.admin.model.mapper.AdminPaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AdminPaymentService {

    private final AdminPaymentMapper adminPaymentMapper;
    private final RestTemplate restTemplate;

    @Value("${portone.api.secret}")
    private String portoneApiSecret;

    @Autowired
    public AdminPaymentService(AdminPaymentMapper adminPaymentMapper,
                               RestTemplate restTemplate){
        this.adminPaymentMapper = adminPaymentMapper;
        this.restTemplate = restTemplate;
    }

    public List<PaymentHistoryDto> getRefundHistory(){
        return adminPaymentMapper.getRefundHistory();
    }

    // 결제 취소
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

    public int updateCanceledPaymentHistory(String paymentNo, String state){

        int cancelTable = adminPaymentMapper.updateCanceledPaymentHistory(paymentNo, state);
        int paymentTable = adminPaymentMapper.updatePaymentHistory(paymentNo, state);

        if(cancelTable != 1 || paymentTable != 1){
            return 0;
        }

        return 1;
    }
}
