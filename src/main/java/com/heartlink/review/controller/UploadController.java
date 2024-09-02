package com.heartlink.review.controller;

import com.heartlink.common.s3.S3Uploader;
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
    private final S3Uploader s3Uploader;


    @PostMapping("/image-upload")
    public ResponseEntity<String> imageUpload(@RequestParam("file") MultipartFile file) {
        try {
            // 파일 이름 생성 (UUID 사용)
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uuidFileName = UUID.randomUUID().toString() + fileExtension;

            String filePath = "/image/review/";
            String fileUrl = s3Uploader.uploadFileToS3(file, filePath, uuidFileName);

            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("이미지 업로드 실패");
        }
    }
}
