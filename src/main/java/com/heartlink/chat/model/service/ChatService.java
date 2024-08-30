package com.heartlink.chat.model.service;

import com.heartlink.chat.model.dto.ChatDto;
import com.heartlink.chat.model.dto.ChatMessageDto;
import com.heartlink.chat.model.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final ChatMapper chatMapper;

    @Autowired
    public ChatService(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    // 매칭 상태가 Y인 채팅 목록을 가져오는 메서드
    public List<ChatDto> getActiveChatList(int userNo) {
        return chatMapper.getActiveChatList(userNo);
    }

    // 특정 매칭의 채팅 로그를 가져오는 메서드
    public List<ChatDto> getChatLogs(Long matchingNo) {
        return chatMapper.getChatLogs(matchingNo);
    }

    // 새로운 채팅 메시지를 저장하는 메서드
    public void saveChatMessage(ChatMessageDto message) {
        chatMapper.insertChatLog(message);  // ChatMessageDto를 그대로 전달
    }

    // 채팅 상태를 업데이트하는 메서드
    public void updateChatStatus(Long matchingNo,Long userNo) {
        chatMapper.updateChatStatus(matchingNo,userNo);
    }
}
