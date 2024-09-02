package com.heartlink.chat.model.service;

import com.amazonaws.services.s3.AmazonS3;
import com.heartlink.chat.model.dto.ChatDto;
import com.heartlink.chat.model.dto.ChatMessageDto;
import com.heartlink.chat.model.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

@Service
public class ChatService {

    private final ChatMapper chatMapper;
    private final AmazonS3 s3Client;

    @Autowired
    public ChatService(ChatMapper chatMapper, AmazonS3 s3Client) {
        this.chatMapper = chatMapper;
        this.s3Client = s3Client;
    }

    // 매칭 상태가 Y인 채팅 목록을 가져오는 메서드
    public List<ChatDto> getActiveChatList(int userNo) {

        List<ChatDto> result = chatMapper.getActiveChatList(userNo);

        for (ChatDto item : result) {
            String photoPath = item.getPhotoPath();
            String photoName = item.getPhotoName();

            // Null 체크 및 기본 이미지 설정
            if (photoPath == null || photoName == null) {
                item.setPhotoPath("/image/mypage/icon_users.png"); // 기본 이미지 URL 설정
            } else {
                String s3Url = photoPath + photoName;
                URL url = s3Client.getUrl("heart-link-bucket", s3Url);
                String txtUrl = url.toString();
                item.setPhotoPath(txtUrl);
            }
        }

        return result;
    }

    // 특정 매칭의 채팅 로그를 가져오는 메서드
    public List<ChatDto> getChatLogs(Long matchingNo) {
        List<ChatDto> result = chatMapper.getChatLogs(matchingNo);

        for (ChatDto item : result) {
            String photoPath = item.getPhotoPath();
            String photoName = item.getPhotoName();

            // Null 체크 및 기본 이미지 설정
            if (photoPath == null || photoName == null) {
                item.setPhotoPath("/image/mypage/icon_users.png"); // 기본 이미지 URL 설정
            } else {
                String s3Url = photoPath + photoName;
                URL url = s3Client.getUrl("heart-link-bucket", s3Url);
                String txtUrl = url.toString();
                item.setPhotoPath(txtUrl);
            }
        }

        return result;
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
