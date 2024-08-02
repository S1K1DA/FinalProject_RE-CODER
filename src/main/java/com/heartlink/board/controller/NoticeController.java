package com.heartlink.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notices")
public class NoticeController {

    // 공지사항 리스트 페이지
    @GetMapping("/list")
    public String getNoticeList() {
        return "board/notice/notice-list";
    }

    // 공지사항 상세보기 페이지
    @GetMapping("/detail")
    public String getNoticeDetail() {
        return "board/notice/notice-detail";
    }

    // 공지사항 글쓰기 페이지
    @GetMapping("/new")
    public String createNoticeForm() {
        return "board/notice/notice-write";
    }

    // 공지사항 수정 페이지
    @GetMapping("/edit")
    public String editNoticeForm() {
        return "board/notice/notice-edit";
    }
}