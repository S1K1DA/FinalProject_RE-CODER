package com.heartlink.board.controller;

import com.heartlink.board.model.dto.NoticeDto;
import com.heartlink.board.model.service.NoticeService;
import com.heartlink.common.pagination.Pagination;
import com.heartlink.member.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;
    private final JwtUtil jwtUtil;
    private final Pagination pagination;

    @Autowired
    public NoticeController(NoticeService noticeService, JwtUtil jwtUtil, Pagination pagination) {
        this.noticeService = noticeService;
        this.jwtUtil = jwtUtil;
        this.pagination = pagination;
    }

    // 공지사항 리스트 페이지
    @GetMapping("/list")
    public String getNoticeList(Model model,
                                @RequestParam(value = "page", defaultValue = "1") int page) {

        int listCount = noticeService.getNoticeCount(); // 총 공지사항 수
        int pageSize = 8; // 페이지네이션의 최대 페이지 수
//        int boardLimit = 8; // 한 페이지에 보여줄 공지사항 수

        // 고정된 공지사항 가져오기
        List<NoticeDto> pinnedNotices = noticeService.getPinnedNotices();

        // 일반 공지사항 가져오기
        List<NoticeDto> list = noticeService.getNoticeList();

        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, list);

        if (pinnedNotices != null) {
            model.addAttribute("pinnedNotices", pinnedNotices);  // 고정된 공지사항 리스트 추가
        }
        if (list != null) {
          
            model.addAttribute("list", paginationData.get("items"));  // 일반 공지사항 리스트 추가
            model.addAttribute("startPage", paginationData.get("startPage"));
            model.addAttribute("endPage", paginationData.get("endPage"));
            model.addAttribute("currentPage", paginationData.get("currentPage"));
            model.addAttribute("totalPages", paginationData.get("totalPages"));
            model.addAttribute("paginationUrl", "/notices/list");
        }

        return "board/notice/notice-list";
    }

    @GetMapping("/detail")
    public String getNoticeDetail(@RequestParam("id") Long noticeNo, Model model) {
        NoticeDto notice = noticeService.getNoticeById(noticeNo);
        if (notice != null) {
            model.addAttribute("notice", notice);
            return "board/notice/notice-detail";
        } else {
            return "error/404";  // 공지사항이 없을 경우 404 페이지로 리다이렉트
        }
    }

    // 공지사항 글쓰기 페이지
    @GetMapping("/new")
    public String createNoticeForm() {
        return "board/notice/notice-write";
    }

    // 공지사항 수정 페이지
    @GetMapping("/edit")
    public String editNoticeForm(@RequestParam("id") Long noticeNo, Model model) {
        NoticeDto notice = noticeService.getNoticeById(noticeNo);  // 공지사항 정보 불러오기
        model.addAttribute("notice", notice);
        return "board/notice/notice-edit";
    }

    // 공지사항 글쓰기 처리
    @PostMapping("/new")
    public String createNotice(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String noticePriority,
            @CookieValue(name = "adminToken", required = false) String adminToken,
            Model model) {

        if (adminToken != null) {
            int adminUserNo = jwtUtil.getAdminNumberFromToken(adminToken);

            if (adminUserNo > 0) {  // adminUserNo가 0보다 크다면 유효한 admin
                NoticeDto noticeDto = new NoticeDto();
                noticeDto.setNoticeTitle(title); // 필드 이름 일치
                noticeDto.setNoticeContent(content); // 필드 이름 일치
                noticeDto.setNoticePriority(noticePriority); // 필드 이름 일치
                noticeDto.setAdminUserNo((long) adminUserNo); // 필드 이름 일치

                try {
                    noticeService.createNotice(noticeDto);
                    return "redirect:/notices/list";  // 성공 시 공지사항 목록으로 리다이렉트
                } catch (IllegalStateException e) {
                    model.addAttribute("errorMessage", e.getMessage());
                    return "board/notice/notice-list";  // 리스트로 이동
                }
            } else {
                model.addAttribute("error", "권한이 없습니다.");
                return "error/403";
            }
        } else {
            return "redirect:/login";  // 로그인 페이지로 리다이렉트
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteNotice(@CookieValue(name = "adminToken", required = false) String adminToken,
                                          @RequestParam("id") Long noticeNo) {
        if (adminToken != null) {
            int adminUserNo = jwtUtil.getAdminNumberFromToken(adminToken);

            if (adminUserNo > 0) {
                try {
                    noticeService.deleteNotice(noticeNo);
                    return ResponseEntity.ok("공지사항이 삭제되었습니다.");  // 200 OK 응답
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 삭제 중 문제가 발생했습니다.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updateNotice(@RequestParam("id") Long noticeNo,
                                          @RequestParam("title") String title,
                                          @RequestParam("content") String content,
                                          @RequestParam("noticePriority") String noticePriority,
                                          @CookieValue(name = "adminToken", required = false) String adminToken) {
        if (adminToken != null) {
            int adminUserNo = jwtUtil.getAdminNumberFromToken(adminToken);

            if (adminUserNo > 0) {
                NoticeDto noticeDto = new NoticeDto();
                noticeDto.setNoticeNo(noticeNo);
                noticeDto.setNoticeTitle(title);
                noticeDto.setNoticeContent(content);
                noticeDto.setNoticePriority(noticePriority);
                noticeDto.setAdminUserNo((long) adminUserNo);
                noticeDto.setNoticeUpdate(new Date());  // 업데이트 날짜 추가

                noticeService.updateNotice(noticeDto);
                return ResponseEntity.ok("공지사항이 수정되었습니다.");  // 200 OK 응답
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
    }
}
