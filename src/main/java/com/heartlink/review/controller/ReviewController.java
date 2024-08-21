package com.heartlink.review.controller;

import com.heartlink.member.util.JwtUtil;
import com.heartlink.review.common.Pagination;
import com.heartlink.review.model.dto.ReviewDto;
import com.heartlink.review.model.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final Pagination pagination;
    private final JwtUtil jwtUtil;

    public ReviewController(ReviewService reviewService, Pagination pagination, JwtUtil jwtUtil) {
        this.reviewService = reviewService;
        this.pagination = pagination;
        this.jwtUtil = jwtUtil;
    }

    // SecurityContext에서 userId 가져오기
    private int getCurrentUserId() {
        String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return jwtUtil.getUserNumberFromToken(jwt);
    }

    @GetMapping("/photomain")
    public String photoMain(@RequestParam(name="page",defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        List<ReviewDto> reviews = reviewService.getAllReviews(); // 모든 리뷰를 가져옴

        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, reviews);

        model.addAttribute("reviews", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("paginationUrl", "/review/photomain");

        return "/review/review_photo/photo-main";
    }

    @GetMapping("/photoenroll")
    public String photoEnroll(Model model) {
        int userId = getCurrentUserId();
        String reviewerNickname = reviewService.getNicknameByUserId(userId);

        // ReviewDto 객체 생성 및 설정
        ReviewDto review = new ReviewDto();
        review.setReviewerUserId(userId);
        review.setReviewerNickname(reviewerNickname);

        // 모델에 review 객체 추가
        model.addAttribute("review", review);

        return "review/review_photo/photo-enroll";
    }

    @GetMapping("/photoedit")
    public String photoEdit(@RequestParam("reviewNo") int reviewNo, Model model) {
        ReviewDto review = reviewService.getReviewDetail(reviewNo); // 조회수 증가 없음
        int currentUserId = getCurrentUserId();

        if (review.getReviewerUserId() == currentUserId) {
            model.addAttribute("review", review);
            return "review/review_photo/photo-edit";
        } else {
            model.addAttribute("message", "작성자만 글을 수정할 수 있습니다.");
            return "redirect:/review/photodetail?reviewNo=" + reviewNo;
        }
    }

    @GetMapping("/photodetail")
    public String photoDetail(@RequestParam("reviewNo") int reviewNo, Model model) {
        ReviewDto review = reviewService.getReviewDetailWithViews(reviewNo); // 조회수 증가
        model.addAttribute("review", review);
        return "/review/review_photo/photo-detail";
    }

    @GetMapping("/livemain")
    public String liveMain(@RequestParam(name="page",defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        List<ReviewDto> liveReviews = reviewService.getLiveReviews();

        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, liveReviews);

        model.addAttribute("liveReviews", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("paginationUrl", "/review/livemain");

        return "/review/live-main";
    }

    @PostMapping("/submit")
    public String submitPhotoEnroll(@RequestParam("title") String title,
                                    @RequestParam("content") String content,
                                    Model model) {
        try {
            int userId = getCurrentUserId();
            ReviewDto review = new ReviewDto();
            review.setReviewTitle(title);
            review.setReviewContent(content);
            review.setReviewerUserId(userId);

            boolean isSaved = reviewService.savePhotoReview(review, null);

            if (isSaved) {
                model.addAttribute("message", "글이 성공적으로 작성되었습니다.");
                return "redirect:/review/photomain";
            } else {
                model.addAttribute("message", "글 작성에 실패했습니다.");
                return "review/review_photo/photo-enroll";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "오류가 발생했습니다: " + e.getMessage());
            return "review/review_photo/photo-enroll";
        }
    }

    @PostMapping("/photoedit")
    public String updatePhotoReview(@RequestParam("reviewNo") int reviewNo,
                                    @RequestParam("reviewTitle") String title,
                                    @RequestParam("reviewContent") String content,
                                    Model model) {
        try {
            int currentUserId = getCurrentUserId();
            ReviewDto review = reviewService.getReviewDetail(reviewNo);

            // 작성자와 현재 userId가 동일할 때만 수정 가능
            if (review.getReviewerUserId() == currentUserId) {
                review.setReviewTitle(title);
                review.setReviewContent(content);

                boolean isUpdated = reviewService.updatePhotoReview(review);

                if (isUpdated) {
                    model.addAttribute("message", "글이 성공적으로 수정되었습니다.");
                    return "redirect:/review/photodetail?reviewNo=" + reviewNo;
                } else {
                    model.addAttribute("message", "글 수정에 실패했습니다.");
                    return "review/review_photo/photo-edit";
                }
            } else {
                model.addAttribute("message", "작성자만 글을 수정할 수 있습니다.");
                return "redirect:/review/photodetail?reviewNo=" + reviewNo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "오류가 발생했습니다: " + e.getMessage());
            return "review/review_photo/photo-edit";
        }
    }

    @PostMapping("/delete")
    public String deleteReview(@RequestParam("reviewNo") int reviewNo, RedirectAttributes redirectAttributes) {
        try {
            int currentUserId = getCurrentUserId();
            ReviewDto review = reviewService.getReviewDetail(reviewNo);

            // 작성자와 현재 userId가 동일할 때만 삭제 가능
            if (review.getReviewerUserId() == currentUserId) {
                boolean isDeleted = reviewService.deleteReview(reviewNo);

                if (isDeleted) {
                    redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 삭제되었습니다.");
                } else {
                    redirectAttributes.addFlashAttribute("message", "리뷰 삭제에 실패했습니다.");
                }
            } else {
                redirectAttributes.addFlashAttribute("message", "작성자만 글을 삭제할 수 있습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/review/photomain";
    }

    @PostMapping("/submitLiveReview")
    public String submitLiveReview(@RequestParam("review_content") String content,
                                   @RequestParam("rating") int rating,
                                   Model model) {
        try {
            int userId = getCurrentUserId();
            ReviewDto review = new ReviewDto();
            review.setReviewContent(content);
            review.setReviewRating(rating);
            review.setReviewerUserId(userId);

            boolean isSaved = reviewService.saveLiveReview(review);

            if (isSaved) {
                return "redirect:/review/livemain";
            } else {
                model.addAttribute("message", "리뷰 작성에 실패했습니다.");
                return "review/live-main";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "오류가 발생했습니다: " + e.getMessage());
            return "review/live-main";
        }
    }

    @PostMapping("/deleteLiveReview")
    public String deleteLiveReview(@RequestParam("reviewNo") int reviewNo,
                                   RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) {
        try {
            int currentUserId = getCurrentUserId();
            ReviewDto review = reviewService.getReviewDetail(reviewNo);

            // 작성자와 현재 userId가 동일할 때만 삭제 가능
            if (review.getReviewerUserId() == currentUserId) {
                boolean isDeleted = reviewService.deleteReview(reviewNo);
                if (isDeleted) {
                    redirectAttributes.addFlashAttribute("message", "리뷰가 삭제되었습니다.");
                } else {
                    redirectAttributes.addFlashAttribute("message", "리뷰 삭제에 실패했습니다.");
                }
            } else {
                redirectAttributes.addFlashAttribute("message", "작성자만 글을 삭제할 수 있습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다: " + e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
