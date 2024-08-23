package com.heartlink.board.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class NoticeDto {
    private Long noticeNo;          // 공지사항 번호
    private Long adminUserNo;       // 작성자 (어드민 번호)
    private Date noticeIndate;      // 공지사항 작성일
    private Date noticeUpdate;      // 공지사항 수정일
    private String noticeDelete;    // 공지사항 삭제 여부 (Y/N)
    private String noticeTitle;     // 공지사항 제목
    private String noticeContent;   // 공지사항 내용 (CLOB)
    private String noticePriority;  // 공지사항 상단 고정 여부 (Y/N)
}
