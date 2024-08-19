package com.heartlink.mypage.controller;

import com.heartlink.mypage.model.dto.MypageDto;
import com.heartlink.mypage.model.service.MypageService;
import com.heartlink.review.common.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;
    private final Pagination pagination;

    public MypageController(MypageService mypageService, Pagination pagination) {
        this.mypageService = mypageService;
        this.pagination = pagination;
    }

    @ModelAttribute
    public void addUserToModel(Model model) {
        int userId = 9; // 임의로 설정한 userId, 실제로는 로그인 정보로 대체되어야 함
        model.addAttribute("userId", userId);
    }

    @GetMapping("/main")
    public String mainpage(HttpServletRequest request, Model model) {
        int userId = (Integer) model.getAttribute("userId");
        MypageDto user = mypageService.getUserInfo(userId);
        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        model.addAttribute("user", user);
        return "mypage/mypage_main/mypage-main";
    }

    @GetMapping("/infoedit")
    public String editPage(HttpServletRequest request, Model model) {
        Integer userId = (Integer) model.getAttribute("userId");
        MypageDto user = mypageService.getUserInfo(userId);
        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        model.addAttribute("user", user);
        return "mypage/mypage_main/mypage-infoedit";
    }

    @GetMapping("/feedlike")
    public String feedlikepage(HttpServletRequest request,
                               @RequestParam(name = "page", defaultValue = "1") int page,
                               Model model) {
        int userId = (Integer) model.getAttribute("userId");
        int pageSize = 12;  // 한 페이지에 표시할 항목 수

        // 전체 좋아요한 피드 목록 가져오기
        List<MypageDto> likedFeeds = mypageService.getLikedFeeds(userId);

        // 페이지네이션 처리
        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, likedFeeds);

        // 모델에 페이지네이션 관련 데이터 추가
        model.addAttribute("likedFeeds", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("paginationUrl", "/mypage/feedlike");

        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        return "mypage/mypage_feedlike/mypage-feedlike";
    }


    @GetMapping("/ptreview")
    public String ptreview(HttpServletRequest request, @RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 6;
        int userId = (Integer) model.getAttribute("userId");
        List<MypageDto> photoReviews = mypageService.getPhotoReviews(userId);

        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, photoReviews);

        model.addAttribute("photoReviews", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("paginationUrl", "/mypage/ptreview");

        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        return "mypage/mypage_review/mypage-ptreview";
    }

    @GetMapping("/lireview")
    public String lireview(HttpServletRequest request, @RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 5;
        int userId = (Integer) model.getAttribute("userId");
        List<MypageDto> liveReviews = mypageService.getLiveReviews(userId);

        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, liveReviews);

        model.addAttribute("liveReviews", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("paginationUrl", "/mypage/lireview");

        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        return "mypage/mypage_review/mypage-lireview";
    }

    // 프로필 좋아요 페이지
    @GetMapping("/proflike")
    public String profLikePage(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        int pageSize = 8; // 한 페이지에 보여줄 아이템 수
        int userId = (Integer) model.getAttribute("userId");

        List<MypageDto> likedProfiles = mypageService.getLikedProfiles(userId);

        // 페이지네이션 처리
        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, likedProfiles);

        model.addAttribute("likedProfiles", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("paginationUrl", "/mypage/proflike");

        return "mypage/mypage_proflike/mypage-proflike";
    }

    @GetMapping("/match")
    public String matchPage(HttpServletRequest request, Model model) {
        int userId = (Integer) model.getAttribute("userId");
        List<MypageDto> matchingHistory = mypageService.getUserMatchingHistory(userId);

        model.addAttribute("matchingHistory", matchingHistory);
        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);

        return "mypage/mypage_match/mypage-match";
    }

    @GetMapping("/delete")
    public String deletePage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        return "mypage/mypage_delete/mypage-delete";
    }

    @GetMapping("/hobby")
    public String hobbyPage(HttpServletRequest request, Model model) {
        int userId = (Integer) model.getAttribute("userId");

        List<MypageDto> likeCategories = mypageService.getPersonalCategoriesByType("L");
        List<MypageDto> dislikeCategories = mypageService.getPersonalCategoriesByType("H");
        List<Integer> userSelectedCategories = mypageService.getUserSelectedCategories(userId);

        model.addAttribute("likeCategories", likeCategories);
        model.addAttribute("dislikeCategories", dislikeCategories);
        model.addAttribute("userSelectedCategories", userSelectedCategories);

        List<MypageDto> userHobbies = mypageService.getUserHobbies(userId);
        model.addAttribute("userHobbies", userHobbies);

        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        return "mypage/mypage_hobby/mypage-hobby";
    }

    @GetMapping("/sentiedit")
    public String editSentiPage(Model model) {
        int userId = (Integer) model.getAttribute("userId");

        List<MypageDto> likeCategories = mypageService.getPersonalCategoriesByType("L");
        List<MypageDto> dislikeCategories = mypageService.getPersonalCategoriesByType("H");
        List<Integer> userSelectedCategories = mypageService.getUserSelectedCategories(userId);

        model.addAttribute("likeCategories", likeCategories);
        model.addAttribute("dislikeCategories", dislikeCategories);
        model.addAttribute("userSelectedCategories", userSelectedCategories);

        return "mypage/mypage_hobby/mypage-sentiedit";
    }

    @GetMapping("/hobbyedit")
    public String hobbyEditPage(HttpServletRequest request, Model model) {
        int userId = (Integer) model.getAttribute("userId");

        List<MypageDto> hobbyCategories = mypageService.getHobbyCategories();
        List<MypageDto> userHobbies = mypageService.getUserHobbies(userId);
        List<Integer> userHobbyIds = userHobbies.stream().map(MypageDto::getHobbyNo).collect(Collectors.toList());

        model.addAttribute("hobbyCategories", hobbyCategories);
        model.addAttribute("userHobbyIds", userHobbyIds);
        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);

        return "mypage/mypage_hobby/mypage-hobbyedit";
    }

    @PostMapping("/validatePassword")
    @ResponseBody
    public Map<String, Boolean> validatePassword(@RequestBody Map<String, String> payload, Model model) {
        Integer userId = (Integer) model.getAttribute("userId");
        String inputPassword = payload.get("password");
        String storedPassword = mypageService.getPasswordByUserId(userId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", storedPassword != null && storedPassword.equals(inputPassword));
        return response;
    }

    @PostMapping("/update")
    public String updateUserInfo(@ModelAttribute("user") MypageDto user, Model model) {
        Integer userId = (Integer) model.getAttribute("userId");
        user.setUserId(userId);

        int result = mypageService.updateUserInfo(user);
        return result > 0 ? "redirect:/mypage/main" : "mypage/mypage_main/mypage-infoedit";
    }

    @PostMapping("/sentiedit/submit")
    public String submitSentiEdit(
            @RequestParam(value = "likes", required = false) List<Integer> likeIds,
            @RequestParam(value = "dislikes", required = false) List<Integer> dislikeIds,
            @ModelAttribute("userId") int userId) {

        List<Integer> allCategoryIds = new ArrayList<>();
        if (likeIds != null) allCategoryIds.addAll(likeIds);
        if (dislikeIds != null) allCategoryIds.addAll(dislikeIds);

        mypageService.saveUserCategories(userId, allCategoryIds);
        return "redirect:/mypage/hobby";
    }

    @PostMapping("/hobbyedit/submit")
    public String submitHobbyEdit(
            @RequestParam(value = "hobbies", required = false) List<Integer> hobbyIds,
            @ModelAttribute("userId") int userId) {

        mypageService.saveUserHobbies(userId, hobbyIds);
        return "redirect:/mypage/hobby";
    }

    @PostMapping("/delete")
    public String deleteUser(
            @RequestParam("password") String password,
            @RequestParam("password-confirm") String passwordConfirm,
            HttpServletRequest request,
            Model model) {

        int userId = (Integer) model.getAttribute("userId");

        if (!password.equals(passwordConfirm)) {
            return "mypage/mypage_delete/mypage-delete";
        }

        Map<String, String> payload = new HashMap<>();
        payload.put("password", password);
        Map<String, Boolean> validationResult = validatePassword(payload, model);

        if (validationResult.get("valid")) {
            boolean isDeleted = mypageService.deleteUserById(userId);
            return isDeleted ? "redirect:/login?logout" : "mypage/mypage_delete/mypage-delete";
        } else {
            return "mypage/mypage_delete/mypage-delete";
        }
    }

    @PostMapping("/unlikeFeed")
    @ResponseBody
    public ResponseEntity<Void> unlikeFeed(@RequestBody Map<String, Integer> payload, Model model) {
        Integer userId = (Integer) model.getAttribute("userId");
        Integer feedNo = payload.get("feedNo");

        boolean success = mypageService.unlikeFeed(userId, feedNo);

        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/likeFeed")
    @ResponseBody
    public ResponseEntity<Void> likeFeed(@RequestBody Map<String, Integer> payload, Model model) {
        Integer userId = (Integer) model.getAttribute("userId");
        Integer feedNo = payload.get("feedNo");

        boolean success = mypageService.likeFeed(userId, feedNo);

        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 프로필 좋아요 추가
    @PostMapping("/likeProfile")
    @ResponseBody
    public Map<String, Boolean> likeProfile(@RequestBody Map<String, Integer> payload, Model model) {
        int userId = (Integer) model.getAttribute("userId");
        int likedUserNo = payload.get("likedUserNo");

        boolean isLiked = mypageService.likeProfile(userId, likedUserNo);

        return Map.of("success", isLiked);
    }

    // 프로필 좋아요 해제
    @PostMapping("/unlikeProfile")
    @ResponseBody
    public Map<String, Boolean> unlikeProfile(@RequestBody Map<String, Integer> payload, Model model) {
        int userId = (Integer) model.getAttribute("userId");
        int likedUserNo = payload.get("likedUserNo");

        boolean isUnliked = mypageService.unlikeProfile(userId, likedUserNo);

        return Map.of("success", isUnliked);
    }

}
