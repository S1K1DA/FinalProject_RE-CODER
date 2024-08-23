package com.heartlink.board.model.service;

import com.heartlink.board.model.dto.NoticeDto;
import com.heartlink.board.model.mapper.NoticeMapper;
import com.heartlink.common.paging.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    private final NoticeMapper noticeMapper;

    @Autowired
    public NoticeService(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }

    public void createNotice(NoticeDto noticeDto) {

        int pinnedCount = noticeMapper.countPinnedNotices();

        // 고정된 공지사항이 3개 이상이면 예외 발생
        if ("Y".equals(noticeDto.getNoticePriority()) && pinnedCount >= 3) {
            throw new IllegalStateException("고정된 공지사항은 최대 3개까지만 작성할 수 있습니다.");
        }

        noticeMapper.insertNotice(noticeDto);
    }

    public int getNoticeCount() {
        return noticeMapper.selectNoticeCount();
    }

    public List<NoticeDto> getNoticeList(PageInfo pi) {
        return noticeMapper.selectNoticeList(pi);
    }

    public List<NoticeDto> getPinnedNotices() {
        return noticeMapper.selectPinnedNotices();
    }
    // 게시판 디테일 조회
    public NoticeDto getNoticeById(Long noticeNo) {
        return noticeMapper.selectNoticeById(noticeNo);
    }
    
    // 게시판 삭제
    public void deleteNotice(Long noticeNo) {
        noticeMapper.deleteNoticeById(noticeNo);
    }

    // 공지사항 수정 메소드
    public void updateNotice(NoticeDto noticeDto) {

        int pinnedCount = noticeMapper.countPinnedNotices();

        // 고정된 공지사항이 3개 이상이면 예외 발생
        if ("Y".equals(noticeDto.getNoticePriority()) && pinnedCount >= 3) {
            throw new IllegalStateException("고정된 공지사항은 최대 3개까지만 작성할 수 있습니다.");
        }

        noticeMapper.updateNotice(noticeDto);
    }

}
