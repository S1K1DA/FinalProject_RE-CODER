package com.heartlink.admin.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자 .
@ToString
public class PaymentHistoryDto {

    private String paymentNo;
    private String paymentUserEmail;
    private String paymentDate;
    private String paymentAmount;
    private String paymentMethod;
    private String paymentState;
    private String paymentProduct;

    private String cancelRequestIndate;
    private String cancelResponse;
    private String cancelResponseIndate;
}
