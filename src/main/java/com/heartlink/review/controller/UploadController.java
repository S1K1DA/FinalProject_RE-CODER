package com.heartlink.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class UploadController {

    @Autowired
    private final WebApplicationContext context;

    @PostMapping("/image-upload")
    public ResponseEntity<String> imageUpload(@RequestParam("file") MultipartFile file) {
        try {
            // 로컬 경로 설정
            String projectDirectory = Paths.get("").toAbsolutePath().toString();
            String uploadDirectory = projectDirectory + "/src/main/resources/static/image/review";

            // 파일 이름 생성 (UUID 사용)
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uuidFileName = UUID.randomUUID().toString() + fileExtension;

            // 디렉토리 생성
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 파일 저장
            file.transferTo(new File(uploadDirectory, uuidFileName));

            // 이미지 파일의 URL을 반환 (브라우저에서 접근 가능한 URL로 설정)
            String fileUrl = "/image/review/" + uuidFileName;
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("이미지 업로드 실패");
        }
    }
}
