package com.heartlink.chat.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDto {

    private static final int MAX_LENGTH = 20;


    private Long matchingNo;   // 매칭 번호
    private String nickname;   // 유저 닉네임
    private String photoPath;  // 프로필 사진 경로
    private String photoName;
    private String content;    // 채팅 내용
    private String messageType; // 메시지 타입 (텍스트, 이미지 등)
    private String timestamp;  // 채팅 입력 시간
    private String lastMessage;
    private Long basicUserNo;
    private String status;

    public void setLastMessage(String lastMessage) {
        System.out.println("Setting lastMessage: " + lastMessage); // 디버깅 로그 추가
        if (lastMessage != null && lastMessage.length() > MAX_LENGTH) {
            this.lastMessage = lastMessage.substring(0, 6) + "...";
        } else {
            this.lastMessage = lastMessage != null ? lastMessage : ""; // null일 경우 빈 문자열로 설정
        }
        System.out.println("Final lastMessage: " + this.lastMessage); // 디버깅 로그 추가
    }

}
