package com.heartlink.mypage.controller;

import com.heartlink.member.util.JwtUtil;
import com.heartlink.mypage.model.dto.MypageDto;
import com.heartlink.mypage.model.service.MypageService;
import com.heartlink.common.pagination.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public MypageController(MypageService mypageService, Pagination pagination, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.mypageService = mypageService;
        this.pagination = pagination;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // SecurityContext에서 userId 가져오기
    private int getCurrentUserId() {
        String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return jwtUtil.getUserNumberFromToken(jwt);
    }

    @GetMapping("/main")
    public String mainpage(HttpServletRequest request, Model model) {
        int userId = getCurrentUserId();
        MypageDto user = mypageService.getUserInfo(userId);
        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        model.addAttribute("user", user);
        return "mypage/mypage_main/mypage-main";
    }

    @GetMapping("/infoedit")
    public String editPage(HttpServletRequest request, Model model) {
        int userId = getCurrentUserId();
        MypageDto user = mypageService.getUserInfo(userId);
        MypageDto userLocation = mypageService.getUserLocation(userId);  // 위도/경도 정보를 가져옴

        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        model.addAttribute("user", user);
        model.addAttribute("userLocation", userLocation);  // 모델에 위도/경도 정보를 추가

        return "mypage/mypage_main/mypage-infoedit";
    }


    @GetMapping("/feedlike")
    public String feedlikepage(HttpServletRequest request,
                               @RequestParam(name = "page", defaultValue = "1") int page,
                               Model model) {
        int userId = getCurrentUserId();
        int pageSize = 12;  // 한 페이지에 표시할 항목 수

        List<MypageDto> likedFeeds = mypageService.getLikedFeeds(userId);
        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, likedFeeds);

        model.addAttribute("likedFeeds", paginationData.get("items"));
        model.addAttribute("startPage", paginationData.get("startPage"));
        model.addAttribute("endPage", paginationData.get("endPage"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("paginationUrl", "/mypage/feedlike");

        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        return "mypage/mypage_feedlike/mypage-feedlike";
    }

    @GetMapping("/ptreview")
    public String ptreview(HttpServletRequest request, @RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        int userId = getCurrentUserId();
        int pageSize = 6;

        List<MypageDto> photoReviews = mypageService.getPhotoReviews(userId);
        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, photoReviews);

        model.addAttribute("photoReviews", paginationData.get("items"));
        model.addAttribute("startPage", paginationData.get("startPage"));
        model.addAttribute("endPage", paginationData.get("endPage"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("paginationUrl", "/mypage/ptreview");

        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        return "mypage/mypage_review/mypage-ptreview";
    }

    @GetMapping("/lireview")
    public String lireview(HttpServletRequest request, @RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        int userId = getCurrentUserId();
        int pageSize = 5;

        List<MypageDto> liveReviews = mypageService.getLiveReviews(userId);
        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, liveReviews);

        model.addAttribute("liveReviews", paginationData.get("items"));
        model.addAttribute("startPage", paginationData.get("startPage"));
        model.addAttribute("endPage", paginationData.get("endPage"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("paginationUrl", "/mypage/lireview");

        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        return "mypage/mypage_review/mypage-lireview";
    }

    @GetMapping("/proflike")
    public String profLikePage(@RequestParam(name = "page", defaultValue = "1") int page,HttpServletRequest request, Model model) {
        int userId = getCurrentUserId();
        int pageSize = 8;

        List<MypageDto> likedProfiles = mypageService.getLikedProfiles(userId);
        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, likedProfiles);

        model.addAttribute("likedProfiles", paginationData.get("items"));
        model.addAttribute("startPage", paginationData.get("startPage"));
        model.addAttribute("endPage", paginationData.get("endPage"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("paginationUrl", "/mypage/proflike");

        model.addAttribute("currentUrl", request.getRequestURI().split("\\?")[0]);
        return "mypage/mypage_proflike/mypage-proflike";
    }

    @GetMapping("/match")
    public String matchPage(HttpServletRequest request, Model model) {
        int userId = getCurrentUserId();
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
        int userId = getCurrentUserId();

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
        int userId = getCurrentUserId();

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
        int userId = getCurrentUserId();

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
    public Map<String, Boolean> validatePassword(@RequestBody Map<String, String> payload) {
        int userId = getCurrentUserId();
        String inputPassword = payload.get("password");
        String storedPassword = mypageService.getPasswordByUserId(userId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", storedPassword != null && passwordEncoder.matches(inputPassword, storedPassword));
        return response;
    }

    @PostMapping("/update")
    public String updateUserInfo(@ModelAttribute("user") MypageDto user) {
        int userId = getCurrentUserId();
        user.setUserId(userId);

        // 체크박스가 체크되지 않았을 경우, consentLocationInfo가 null이므로 "N"으로 설정
        if (user.getConsentLocationInfo() == null) {
            user.setConsentLocationInfo("N");
        }

        int result = mypageService.updateUserInfo(user);
        return result > 0 ? "redirect:/mypage/main" : "mypage/mypage_main/mypage-infoedit";
    }

    @PostMapping("/updateMbti")
    @ResponseBody
    public ResponseEntity<String> updateUserMbti(@RequestParam("mbti") String mbti) {
        int userId = getCurrentUserId();
        MypageDto user = mypageService.getUserInfo(userId);
        user.setMbti(mbti);

        int result = mypageService.updateUserInfo(user);
        if(result > 0) {
            return ResponseEntity.ok("MBTI 업데이트 성공");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("MBTI 업데이트 실패");
        }
    }

    @PostMapping("/sentiedit/submit")
    public String submitSentiEdit(
            @RequestParam(value = "likes", required = false) List<Integer> likeIds,
            @RequestParam(value = "dislikes", required = false) List<Integer> dislikeIds) {

        int userId = getCurrentUserId();  // 여기서 userId를 가져옵니다.

        List<Integer> allCategoryIds = new ArrayList<>();
        if (likeIds != null) allCategoryIds.addAll(likeIds);
        if (dislikeIds != null) allCategoryIds.addAll(dislikeIds);

        mypageService.saveUserCategories(userId, allCategoryIds);
        return "redirect:/mypage/hobby";
    }

    @PostMapping("/hobbyedit/submit")
    public String submitHobbyEdit(
            @RequestParam(value = "hobbies", required = false) List<Integer> hobbyIds) {

        int userId = getCurrentUserId();  // 여기서 userId를 가져옵니다.

        mypageService.saveUserHobbies(userId, hobbyIds);
        return "redirect:/mypage/hobby";
    }

    @PostMapping("/delete")
    public String deleteUser(
            @RequestParam("password") String password,
            @RequestParam("password-confirm") String passwordConfirm) {

        int userId = getCurrentUserId();

        if (!password.equals(passwordConfirm)) {
            return "mypage/mypage_delete/mypage-delete";
        }

        String storedPassword = mypageService.getPasswordByUserId(userId);
        if (storedPassword != null && passwordEncoder.matches(password, storedPassword)) {
            boolean isDeleted = mypageService.deleteUserById(userId);
            return isDeleted ? "redirect:/member/logout" : "mypage/mypage_delete/mypage-delete";
        } else {
            return "mypage/mypage_delete/mypage-delete";
        }
    }

    @PostMapping("/unlikeFeed")
    @ResponseBody
    public ResponseEntity<Void> unlikeFeed(@RequestBody Map<String, Integer> payload) {
        int userId = getCurrentUserId();
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
    public ResponseEntity<Void> likeFeed(@RequestBody Map<String, Integer> payload) {
        int userId = getCurrentUserId();
        Integer feedNo = payload.get("feedNo");

        boolean success = mypageService.likeFeed(userId, feedNo);

        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/likeProfile")
    @ResponseBody
    public Map<String, Boolean> likeProfile(@RequestBody Map<String, Integer> payload) {
        int userId = getCurrentUserId();
        int likedUserNo = payload.get("likedUserNo");

        boolean isLiked = mypageService.likeProfile(userId, likedUserNo);

        return Map.of("success", isLiked);
    }

    @PostMapping("/unlikeProfile")
    @ResponseBody
    public Map<String, Boolean> unlikeProfile(@RequestBody Map<String, Integer> payload) {
        int userId = getCurrentUserId();
        int likedUserNo = payload.get("likedUserNo");

        boolean isUnliked = mypageService.unlikeProfile(userId, likedUserNo);

        return Map.of("success", isUnliked);
    }

    @PostMapping("/checkNickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestBody Map<String, String> payload) {
        String nickname = payload.get("nickname");
        boolean isUnique = mypageService.isNicknameUnique(nickname);
        return ResponseEntity.ok(Map.of("isUnique", isUnique));
    }

    @GetMapping("/getFeedContent")
    @ResponseBody
    public Map<String, Object> getFeedContent(@RequestParam("feedNo") int feedNo) {
        MypageDto feed = mypageService.getFeedByNo(feedNo);
        Map<String, Object> response = new HashMap<>();

        if (feed == null) {
            response.put("status", "error");
            response.put("message", "Feed not found");
            return response;
        }

        response.put("status", "success");
        response.put("feedTitle", feed.getFeedTitle());
        response.put("author", feed.getAuthor());
        response.put("likeCount", feed.getLikeCount());
        response.put("feedTag", feed.getFeedTag());
        response.put("feedIndate", feed.getFeedIndate());
        response.put("feedContent", feed.getFeedContent());

        return response;
    }

}
