package org.spring.backendprojectex.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 잘못된 요청 (파라미터 오류)  // 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        String html =
                "<script>" +
                " alert('" + e.getMessage() + "'); " +
                " history.go(-1) " +
                " </script>" ;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(html);
    }

    // 상태 충돌 (중복 이메일등)  // 409
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> illegalStateExceptionHandler(IllegalStateException e) {
        String html =
                "<script>" +
                " alert('" + e.getMessage() + "'); " +
                " history.go(-1) " +
                " </script>" ;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Content-Type","text/html; charset=UTF-8")
                .body(html);
    }

    // 데이터없음     // 404
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> noSuchElementExceptionHandler(NoSuchElementException e) {
        String html =
                "<script>" +
                " alert('" + e.getMessage() + "'); " +
                " history.go(-1) " +
                " </script>" ;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Content-Type","text/html; charset=UTF-8")
                .body(html);
    }

    // null  (서버문제로 가져옴)   // 500
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> nullPointerExceptionHandler(NullPointerException e) {
        String html =
                "<script>" +
                " alert('" + e.getMessage() + "'); " +
                " history.go(-1) " +
                " </script>" ;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Content-Type","text/html; charset=UTF-8")
                .body(html);
    }
    // 모든 예외  (최종 fallback)   // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception e) {
        String html =
                "<script>" +
                " alert('" + e.getMessage() + "'); " +
                " history.go(-1) " +
                " </script>" ;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Content-Type","text/html; charset=UTF-8")
                .body(html);
    }
}
