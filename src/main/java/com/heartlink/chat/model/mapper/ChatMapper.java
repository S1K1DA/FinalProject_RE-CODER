package com.heartlink.chat.model.mapper;

import com.heartlink.chat.model.dto.ChatDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatMapper {

    // 채팅 중인 사용자 목록 조회
    List<ChatDto> getActiveChatList();
}
