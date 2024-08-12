package com.heartlink.review.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /image/review/** URL로 요청된 파일들을 로컬 경로로 매핑
        registry.addResourceHandler("/image/review/**")
                .addResourceLocations("file:" + Paths.get("").toAbsolutePath().toString() + "/src/main/resources/static/image/review/");
    }
}
