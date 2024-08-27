package com.heartlink.chat.controller;

import com.heartlink.chat.model.dto.ChatDto;
import com.heartlink.chat.model.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // 채팅 페이지로 이동하는 메서드
    @GetMapping("/chatting")
    public String getChatPage() {
        return "chat/chat-page";
    }

    // 채팅 중인 사용자 목록을 반환하는 API 엔드포인트 추가
    @GetMapping("/activeChats")
    @ResponseBody
    public List<ChatDto> getActiveChats() {
        return chatService.getActiveChatList();
    }
}
