package com.heartlink.admin.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminInquiryDto {
    private int chatbotChattingNo;
    private int basicUserNo;
    private String chatbotChattingIndate;
    private String chatbotChattingState;
    private String chatbotInquiryTag;
}
