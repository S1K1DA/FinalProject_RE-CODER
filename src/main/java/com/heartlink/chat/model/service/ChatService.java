package com.heartlink.chat.model.service;

import com.heartlink.chat.model.dto.ChatDto;
import com.heartlink.chat.model.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMapper chatMapper;

    // 채팅 중인 사용자 목록을 가져오는 서비스 메서드
    public List<ChatDto> getActiveChatList() {
        return chatMapper.getActiveChatList();
    }
}
