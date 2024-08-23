package com.heartlink.charge.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentInterceptor implements HandlerInterceptor {


    private static final Map<String, String> pathMapping = new HashMap<>();

    static {
        pathMapping.put("/charge/payment-cancle", "/charge/history");
        pathMapping.put("/charge/payment-order", "/charge/shop");
        pathMapping.put("/charge/complete", "/charge/shop");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 요청된 경로
        String requestUri = request.getRequestURI();
        // 요청을 해온 경로 (리퍼러)
        String referrer = request.getHeader("Referer");

        // 리퍼러의 순수 경로만 추출
        String referrerPath = getPathWithoutQuery(referrer);

        // 로그 출력
//        System.out.println("Request URI: " + requestUri);
//        System.out.println("Referrer Path: " + referrerPath);
//        System.out.println("================================");

        if (isValidPair(requestUri, referrerPath)) {
            // 유효한 쌍일 경우, 요청을 계속 처리하도록 허용
            return true;
        } else {
            // 유효하지 않은 쌍일 경우, 요청을 거부하거나 적절한 처리를 할 수 있음
            sendErrorRedirect(response, "잘못된 요청 경로입니다!", "/");
            return false;
        }
    }

    private String getPathWithoutQuery(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        try {
            URI uri = new URI(url);
            return uri.getPath();
        } catch (URISyntaxException e) {
            // URI 파싱 오류가 발생하면 null 반환
            System.out.println("Failed to parse URL: " + url + "," + e);
            return null;
        }
    }

    private boolean isValidPair(String requestUri, String referrerPath) {
        return referrerPath != null && referrerPath.equals(pathMapping.get(requestUri));
    }

    private void sendErrorRedirect(HttpServletResponse response, String errorMessage, String redirectUrl) throws Exception {
        // 에러 메시지를 alert로 띄우고 메인 페이지로 리디렉션
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(
                "<script>" +
                        "alert('" + errorMessage + "');" +
                        "window.location.href='" + redirectUrl + "';" +
                        "</script>"
        );
        response.getWriter().flush();
    }
}
