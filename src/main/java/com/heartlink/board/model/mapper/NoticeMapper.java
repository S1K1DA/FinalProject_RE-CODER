package com.heartlink.board.model.mapper;

import com.heartlink.board.model.dto.NoticeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {

    void insertNotice(NoticeDto noticeDto);

    // 고정된 공지사항 개수 조회
    int countPinnedNotices();

    // 공지사항 총 개수 조회
    int selectNoticeCount();

    // 공지사항 목록 조회 (페이징 처리)
    List<NoticeDto> selectNoticeList();

    // 고정된 공지사항 항목 조회
    List<NoticeDto> selectPinnedNotices();

    // 공지사항 디테일
    NoticeDto selectNoticeById(Long noticeNo);

    // 공지사항 삭제
    void deleteNoticeById(Long noticeNo);

    // 공지사항 수정
    void updateNotice(NoticeDto noticeDto);
}
