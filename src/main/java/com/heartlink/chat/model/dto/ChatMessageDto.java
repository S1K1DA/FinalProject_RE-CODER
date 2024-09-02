package com.heartlink.chat.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    private String sender;     // 메시지 발신자
    private String content;    // 메시지 내용
    private String messageType; // 메시지 타입 (CHAT, IMAGE 등)
    private Long matchingNo;   // 매칭 번호
    private Long basicUserNo;  // 유저 번호
}
