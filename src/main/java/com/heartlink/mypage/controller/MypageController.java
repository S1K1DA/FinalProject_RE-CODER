package com.heartlink.mypage.controller;

import com.heartlink.mypage.model.dto.MypageDto;
import com.heartlink.mypage.model.service.MypageService;
import com.heartlink.review.common.Pagination;
import jakarta.servlet.http.HttpServletRequest;
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

    // 모든 요청에 대해 userId를 모델에 추가
    @ModelAttribute
    public void addUserToModel(Model model) {
        int userId = 9; // 임의로 설정한 userId, 실제로는 로그인 정보로 대체되어야 함
        model.addAttribute("userId", userId);
    }

    @GetMapping("/main")
    public String mainpage(HttpServletRequest request, Model model) {
        int userId = (Integer) model.getAttribute("userId");  // 모델에서 userId를 가져옴
        MypageDto user = mypageService.getUserInfo(userId);
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("user", user);
        return "mypage/mypage_main/mypage-main";
    }

    @GetMapping("/infoedit")
    public String editPage(HttpServletRequest request, Model model) {
        Integer userId = (Integer) model.getAttribute("userId");
        MypageDto user = mypageService.getUserInfo(userId);
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("user", user);
        return "mypage/mypage_main/mypage-infoedit";
    }

    @GetMapping("/feedlike")
    public String feedlikepage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_feedlike/mypage-feedlike";
    }

    @GetMapping("/ptreview")
    public String ptreview(@RequestParam(name="page", defaultValue = "1") int page, Model model) {
        int pageSize = 6; // 여기서 페이지 크기를 설정
        int userId = (Integer) model.getAttribute("userId");
        List<MypageDto> photoReviews = mypageService.getPhotoReviews(userId);

        // 페이지네이션 데이터 생성
        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, photoReviews);

        model.addAttribute("photoReviews", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("paginationUrl", "/mypage/ptreview");

        return "mypage/mypage_review/mypage-ptreview";
    }




    @GetMapping("/lireview")
    public String lireview(@RequestParam(name="page",defaultValue = "1") int page, Model model) {
        int pageSize = 5;
        int userId = (Integer) model.getAttribute("userId");
        List<MypageDto> liveReviews = mypageService.getLiveReviews(userId);

        // 페이지네이션 데이터 생성
        Map<String, Object> paginationData = pagination.getPagination(page, pageSize, liveReviews);

        model.addAttribute("liveReviews", paginationData.get("items"));
        model.addAttribute("currentPage", paginationData.get("currentPage"));
        model.addAttribute("totalPages", paginationData.get("totalPages"));
        model.addAttribute("paginationUrl", "/mypage/lireview");

        return "mypage/mypage_review/mypage-lireview";
    }

    @GetMapping("/proflike")
    public String profPage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_proflike/mypage-proflike";
    }

    @GetMapping("/match")
    public String matchPage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_match/mypage-match";
    }

    @GetMapping("/delete")
    public String deletePage(HttpServletRequest request, Model model) {
        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_delete/mypage-delete";
    }

    @GetMapping("/hobby")
    public String hobbyPage(HttpServletRequest request, Model model) {
        int userId = (Integer) model.getAttribute("userId");

        // 성향 조회
        List<MypageDto> likeCategories = mypageService.getPersonalCategoriesByType("L");
        List<MypageDto> dislikeCategories = mypageService.getPersonalCategoriesByType("H");
        List<Integer> userSelectedCategories = mypageService.getUserSelectedCategories(userId);

        model.addAttribute("likeCategories", likeCategories);
        model.addAttribute("dislikeCategories", dislikeCategories);
        model.addAttribute("userSelectedCategories", userSelectedCategories);

        // 취미 조회
        List<MypageDto> userHobbies = mypageService.getUserHobbies(userId);
        model.addAttribute("userHobbies", userHobbies);

        model.addAttribute("currentUrl", request.getRequestURI());
        return "mypage/mypage_hobby/mypage-hobby";
    }

    @GetMapping("/sentiedit")
    public String editSentiPage(Model model) {
        int userId = (Integer) model.getAttribute("userId");

        // 선호하는 것 (L 타입) 조회
        List<MypageDto> likeCategories = mypageService.getPersonalCategoriesByType("L");

        // 기피하는 것 (H 타입) 조회
        List<MypageDto> dislikeCategories = mypageService.getPersonalCategoriesByType("H");

        // 사용자가 선택한 성향 조회
        List<Integer> userSelectedCategories = mypageService.getUserSelectedCategories(userId);

        model.addAttribute("likeCategories", likeCategories);
        model.addAttribute("dislikeCategories", dislikeCategories);
        model.addAttribute("userSelectedCategories", userSelectedCategories);

        return "mypage/mypage_hobby/mypage-sentiedit";
    }

    @GetMapping("/hobbyedit")
    public String hobbyEditPage(HttpServletRequest request, Model model) {
        int userId = (Integer) model.getAttribute("userId");

        // 모든 취미 항목을 가져옴
        List<MypageDto> hobbyCategories = mypageService.getHobbyCategories();
        // 사용자가 선택한 취미 항목을 가져옴
        List<MypageDto> userHobbies = mypageService.getUserHobbies(userId);
        List<Integer> userHobbyIds = userHobbies.stream().map(MypageDto::getHobbyNo).collect(Collectors.toList());

        model.addAttribute("hobbyCategories", hobbyCategories);
        model.addAttribute("userHobbyIds", userHobbyIds);
        model.addAttribute("currentUrl", request.getRequestURI());

        return "mypage/mypage_hobby/mypage-hobbyedit";
    }

    // 비밀번호 확인 요청 처리
    @PostMapping("/validatePassword")
    @ResponseBody
    public Map<String, Boolean> validatePassword(@RequestBody Map<String, String> payload, Model model) {
        Integer userId = (Integer) model.getAttribute("userId");
        String inputPassword = payload.get("password");
        String storedPassword = mypageService.getPasswordByUserId(userId);

        Map<String, Boolean> response = new HashMap<>();
        if (storedPassword != null && storedPassword.equals(inputPassword)) {
            response.put("valid", true);
        } else {
            response.put("valid", false);
        }
        return response;
    }

    @PostMapping("/update")
    public String updateUserInfo(
            @ModelAttribute("user") MypageDto user,
            Model model) {

        // 현재 로그인한 유저의 ID 가져오기
        Integer userId = (Integer) model.getAttribute("userId");
        user.setUserId(userId);

        // MypageDto 객체의 데이터 출력 (로그로 확인)
        System.out.println("업데이트할 데이터: " + user);

        // 정보 업데이트
        int result = mypageService.updateUserInfo(user);

        // 업데이트 결과 확인
        if(result > 0) {
            return "redirect:/mypage/main";
        } else {
            model.addAttribute("message", "업데이트에 실패했습니다.");
            return "mypage/mypage_main/mypage-infoedit";
        }
    }

    @PostMapping("/sentiedit/submit")
    public String submitSentiEdit(
            @RequestParam(value = "likes", required = false) List<Integer> likeIds,
            @RequestParam(value = "dislikes", required = false) List<Integer> dislikeIds,
            @ModelAttribute("userId") int userId) {

        // 모든 성향 데이터 합치기
        List<Integer> allCategoryIds = new ArrayList<>();
        if (likeIds != null) {
            allCategoryIds.addAll(likeIds);
        }
        if (dislikeIds != null) {
            allCategoryIds.addAll(dislikeIds);
        }

        // 전체 성향 데이터 저장
        mypageService.saveUserCategories(userId, allCategoryIds);

        return "redirect:/mypage/hobby";
    }

    @PostMapping("/hobbyedit/submit")
    public String submitHobbyEdit(
            @RequestParam(value = "hobbies", required = false) List<Integer> hobbyIds,
            @ModelAttribute("userId") int userId) {

        // 유저의 선택한 취미를 저장
        mypageService.saveUserHobbies(userId, hobbyIds);

        return "redirect:/mypage/hobby";
    }
}
