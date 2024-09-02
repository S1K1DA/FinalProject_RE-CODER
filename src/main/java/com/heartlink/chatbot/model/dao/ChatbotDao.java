package com.heartlink.chatbot.model.dao;

import com.heartlink.chatbot.model.dto.ChatbotDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class ChatbotDao {

    private final SqlSession sqlSession;

    public ChatbotDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    // Chatbot Inquiry 저장
    public void insertChatbotInquiry(ChatbotDto inquiryDto) {
        sqlSession.insert("chatbotMapper.insertChatbotInquiry", inquiryDto);
    }
}
