package com.heartlink.chatbot.model.service;

import com.heartlink.chatbot.model.dao.ChatbotDao;
import com.heartlink.chatbot.model.dto.ChatbotDto;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    private final ChatbotDao chatbotDao;

    public ChatbotService(ChatbotDao chatbotDao) {
        this.chatbotDao = chatbotDao;
    }

    // Chatbot Inquiry 저장
    public void saveChatbotInquiry(ChatbotDto inquiryDto) {
        chatbotDao.insertChatbotInquiry(inquiryDto);
    }
}
