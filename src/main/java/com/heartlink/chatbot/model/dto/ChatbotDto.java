package com.heartlink.chatbot.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChatbotDto {
    private Long chatbotChattingNo;
    private int userId;
    private Date chatbotChattingIndate;
    private String chatbotChattingState;
    private String chatbotInquiryTag;


}
