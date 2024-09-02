package com.heartlink.chat.model.mapper;

import com.heartlink.chat.model.dto.ChatDto;
import com.heartlink.chat.model.dto.ChatMessageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMapper {

    // 매칭 상태가 Y인 사용자 목록 조회
    List<ChatDto> getActiveChatList(int userNo);

    // 특정 매칭의 채팅 로그 조회
    List<ChatDto> getChatLogs(@Param("matchingNo") Long matchingNo);

    // 채팅 로그 삽입
    void insertChatLog(ChatMessageDto chatMessage);

    // 매칭 상태 업데이트 (채팅 종료)
    void updateChatStatus(@Param("matchingNo") Long matchingNo, @Param("userNo") Long userNo);
}
