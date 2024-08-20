package com.heartlink.charge.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 변수가 있는 생성자
@JsonIgnoreProperties(ignoreUnknown = true)  // DTO에 정의되지 않은 JSON 응답의 필드가 들어올 경우 무시
@JsonInclude(JsonInclude.Include.NON_NULL) // Non-null 필드만 JSON에 포함
public class ChargeResponseDto {

    // 기본 필드
    private String status;
    private String id;
    private String transactionId;
    private String merchantId;
    private String storeId;

    // 결제 방법
    @JsonProperty("method")
    private Method method;

    // 결제 채널
    @JsonProperty("channel")
    private Channel channel;

    // 시간 관련 필드
    private String version;
    private String requestedAt;
    private String updatedAt;
    private String statusChangedAt;
    private String orderName;

    // 금액 정보
    @JsonProperty("amount")
    private Amount amount;

    // 통화 정보
    private String currency;

    // 고객 정보
    @JsonProperty("customer")
    private Customer customer;

    // 추가 필드
    private String promotionId;
    private boolean isCulturalExpense;
    private String paidAt;
    private String pgTxId;

    private String receiptUrl;

    // Method 내부 클래스
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Method {
        @JsonProperty("type")
        private String type;

        @JsonProperty("provider")
        private String provider;

        @JsonProperty("easyPayMethod")
        private EasyPayMethod easyPayMethod;
    }

    // EasyPayMethod 내부 클래스
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class EasyPayMethod {
        @JsonProperty("type")
        private String type;
    }

    // Channel 내부 클래스
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Channel {
        @JsonProperty("type")
        private String type;

        @JsonProperty("id")
        private String id;

        @JsonProperty("key")
        private String key;

        @JsonProperty("name")
        private String name;

        @JsonProperty("pgProvider")
        private String pgProvider;

        @JsonProperty("pgMerchantId")
        private String pgMerchantId;
    }

    // Amount 내부 클래스
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Amount {
        @JsonProperty("total")
        private int totalAmount;

        @JsonProperty("taxFree")
        private int taxFreeAmount;

        @JsonProperty("vat")
        private int vatAmount;

        @JsonProperty("supply")
        private int supplyAmount;

        @JsonProperty("discount")
        private int discountAmount;

        @JsonProperty("paid")
        private int paidAmount;

        @JsonProperty("cancelled")
        private int cancelledAmount;

        @JsonProperty("cancelledTaxFree")
        private int cancelledTaxFreeAmount;
    }

    // Customer 내부 클래스
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Customer {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        private String email;

        @JsonProperty("phoneNumber")
        private String phoneNumber;
    }
}
