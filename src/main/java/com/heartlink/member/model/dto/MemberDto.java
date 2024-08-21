package com.heartlink.member.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class MemberDto {

    private int userNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "###.######")
    private double latitude; //위도
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "###.######")
    private double longitude; //경도

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 16)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+=-]).*$")
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    @Size(min = 2, max = 5)
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^[가-힣]+$")
    private String name;

    @NotBlank
    @Pattern(regexp = "^\\d{6}$")
    private String residentNumber;

    @NotBlank
    @Pattern(regexp = "^[1-4]$")
    private String gender;

    @NotBlank
    @Pattern(regexp = "^\\d{10,11}$")
    private String phoneNumber;

    @NotBlank
    private String address;

    // 비밀번호와 확인 비밀번호가 일치하는지 확인하는 메서드
    public boolean isPasswordConfirmed() {
        return this.password.equals(this.confirmPassword);
    }
}
