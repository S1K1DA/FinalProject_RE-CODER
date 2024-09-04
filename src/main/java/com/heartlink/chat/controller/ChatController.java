package com.heartlink.chat.controller;

import com.heartlink.chat.model.dto.ChatDto;
import com.heartlink.chat.model.dto.ChatMessageDto;
import com.heartlink.chat.model.service.ChatService;
import com.heartlink.member.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final JwtUtil jwtUtil;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(ChatService chatService, JwtUtil jwtUtil, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
        this.jwtUtil = jwtUtil;
    }

    // 채팅 페이지로 이동하며, 매칭된 사용자 목록을 가져와서 화면에 전달
    @GetMapping("/chatting")
    public String getChatPage(@RequestParam(value = "matchingNo", required = false) Long matchingNo, @CookieValue("token") String jwtToken, Model model) {
        if (matchingNo == null) {
            // 기본값 설정
            System.out.println("matching num : X");
            matchingNo = 1L;  // DB에 존재하는 임의의 매칭 넘버를 설정
        }

        System.out.println(matchingNo + " : matchingNo");

        int userNumber = jwtUtil.getUserNumberFromToken(jwtToken);
        List<ChatDto> activeChats = chatService.getActiveChatList(userNumber);

        model.addAttribute("activeChats", activeChats);

        // 채팅 로그 가져오기
        List<ChatDto> chatLogs = chatService.getChatLogs(matchingNo);

        model.addAttribute("chatLogs", chatLogs);

        System.out.println(activeChats + " : activeChats");
        System.out.println(chatLogs + " : chatLogs");

        // 사용자 번호를 템플릿에 전달
        System.out.println(userNumber + ": userNumber");
        model.addAttribute("basicUserNo", userNumber);
        model.addAttribute("matchingNo", matchingNo);
        model.addAttribute("profiles", chatLogs);



        return "chat/chat-page";
    }




    // 클라이언트에서 메시지를 전송하면 처리하는 메서드
    @MessageMapping("/message")
    public void sendMessage(ChatMessageDto message) {
        chatService.saveChatMessage(message);
        // 메시지 전송
        messagingTemplate.convertAndSend("/topic/messages/" + message.getMatchingNo(), message);

        // 해당 유저의 채팅 목록을 새로 가져옴
        List<ChatDto> updatedChats = chatService.getActiveChatList(message.getBasicUserNo().intValue());

        // lastMessage 업데이트를 전송
        messagingTemplate.convertAndSend("/topic/lastMessage", updatedChats);
    }

    // 채팅방 나가기 요청 처리
    @PostMapping("/exit")
    public ResponseEntity<Void> exitChat(@RequestBody Map<String, Object> request, @CookieValue("token") String jwtToken) {
        Long matchingNo = Long.valueOf(request.get("matchingNo").toString());
        int userNo = jwtUtil.getUserNumberFromToken(jwtToken);

        chatService.updateChatStatus(matchingNo, (long) userNo);

        return ResponseEntity.ok().build();
    }
}
