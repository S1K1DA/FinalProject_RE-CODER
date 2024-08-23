package com.heartlink.config;

import com.heartlink.charge.config.PaymentInterceptor;
import com.heartlink.format.NumberFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private PaymentInterceptor paymentInterceptor;

    @Autowired
    public WebConfig(PaymentInterceptor paymentInterceptor) {
        this.paymentInterceptor = paymentInterceptor;
    }

    // 빈 등록으로 숫자 포매팅하기 위한 메서드
    @Bean(name = "numberFormatter")
    public NumberFormatter numberFormatter() {
        return new NumberFormatter();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(numberFormatter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /image/review/** URL로 요청된 파일들을 로컬 경로로 매핑
        registry.addResourceHandler("/image/review/**")
                .addResourceLocations("file:" + Paths.get("").toAbsolutePath().toString() + "/src/main/resources/static/image/review/");

        registry.addResourceHandler("/image/user_profile/**")
                .addResourceLocations("file:" + Paths.get("").toAbsolutePath().toString() + "/src/main/resources/static/image/user_profile/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(paymentInterceptor)
                .addPathPatterns("/charge/**") // 결제 페이지와 관련된 URL 패턴
                .excludePathPatterns("/charge/shop", "/charge/history");  // 제외 페이지 경로
    }
}

