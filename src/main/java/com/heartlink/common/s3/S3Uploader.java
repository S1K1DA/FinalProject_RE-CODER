package com.heartlink.common.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Optional;
import java.util.UUID;

@Component
public class S3Uploader {

    private static final Logger logger = LogManager.getLogger(S3Uploader.class);

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFileToS3(MultipartFile multipartFile, String filePath, String fileName) {
        File uploadFile = null;
        try {

            uploadFile = resizeImage(multipartFile, 600)
                    .orElseThrow(() -> new IllegalArgumentException("[error] : MultipartFile -> 리사이징 실패"));

            // S3에 파일 업로드 (파일 경로와 이름 조합)
            String fullFilePath = filePath + fileName;
            String uploadImageUrl = putS3(uploadFile, fullFilePath);

            // 업로드 후 로컬 파일 삭제
            removeLocalFile(uploadFile);
            return uploadImageUrl;

        } catch (IOException e) {
            throw new IllegalArgumentException("[error] : MultipartFile -> 파일 변환 실패", e);
        }
    }

    // S3로 파일 업로드
    public String putS3(File uploadFile, String fileName) {
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (AmazonServiceException e) {
            logger.error("[error] : S3 업로드 실패 - {}", e.getMessage());
            throw e;
        }
    }

    // S3에서 파일 삭제
    public void deleteS3(String filePath) {
        try {
            String key = filePath.substring(filePath.indexOf(bucket) + bucket.length() + 1); // 폴더/파일.확장자
            amazonS3Client.deleteObject(bucket, key);
            logger.info("[S3Uploader] : S3에 있는 파일 삭제 성공 - {}", filePath);
        } catch (AmazonServiceException e) {
            logger.error("[error] : S3 파일 삭제 실패 - {}", e.getErrorMessage());
        } catch (Exception e) {
            logger.error("[error] : 파일 삭제 처리 중 오류 발생 - {}", e.getMessage());
        }
    }

    // 로컬 파일 삭제
    private void removeLocalFile(File targetFile) {
        if (targetFile.delete()) {
            logger.info("[파일 업로드] : 파일 삭제 성공 - {}", targetFile.getName());
        } else {
            logger.error("[파일 업로드] : 파일 삭제 실패 - {}", targetFile.getName());
        }
    }

    // MultipartFile을 File로 변환
    private Optional<File> convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());

        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // 이미지 크기 조정
    private Optional<File> resizeImage(MultipartFile file, int maxWidth) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, maxWidth);

        // 파일에 저장
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString() + ".jpg");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(resizedImage, "jpg", baos);
            try (InputStream is = new ByteArrayInputStream(baos.toByteArray());
                 FileOutputStream fos = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }
        return Optional.of(tempFile);
    }
}
