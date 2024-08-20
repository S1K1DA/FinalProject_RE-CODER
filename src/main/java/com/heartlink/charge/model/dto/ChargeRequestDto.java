package com.heartlink.charge.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자 .
@ToString
public class ChargeRequestDto {

    private String paymentNo;

    private String paymentUserEmail;

    private String paymentDate;

    private int paymentAmount;

    private String paymentMethod;

    private String paymentState;

    private String paymentProduct;
    private String paymentReference;
}
