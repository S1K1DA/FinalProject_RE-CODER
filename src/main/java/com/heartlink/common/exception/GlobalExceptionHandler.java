package com.heartlink.common.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    // 데이터 무결성 위반 예외 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        logger.error("데이터 무결성 위반 : " + ex);

        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .header("Redirect-URL", "/error-page")
                             .body("Data integrity violation: " + ex.getMessage());
    }

    // 결과가 비어있는 경우 예외 처리
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        
        logger.error("EmptyResult : " + ex);
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .header("Redirect-URL", "/error-page")
                             .body("No data found: " + ex.getMessage());
    }

    // 기타 데이터 접근 관련 예외 처리
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {

        logger.error("데이터 접근 예외 발생 : " + ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .header("Redirect-URL", "/error-page")
                             .body("Database error: " + ex.getMessage());
    }

    // 커스텀 예외
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {

        logger.error("Custom 예외 발생 : " + ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST) // 원하는 HTTP 상태 코드로 변경 가능
                .header("Redirect-URL", "/error-page")
                .body("Custom error occurred: " + ex.getMessage());
    }
}
