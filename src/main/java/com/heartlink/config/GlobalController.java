package com.heartlink.config;

import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.model.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class GlobalController {

    private final MemberService memberService;

    @Autowired
    public GlobalController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ModelAttribute("member")
    public MemberDto addMemberToModel() {
        System.out.println("Global : ");
        return memberService.getLoggedInUser();
    }
}
