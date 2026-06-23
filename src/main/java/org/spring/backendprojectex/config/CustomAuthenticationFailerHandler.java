package org.spring.backendprojectex.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;

public class CustomAuthenticationFailerHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        // 인증 실패시 상태
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        // 기본적으로 전달하는 예외 메시지 작성
        String errorMassage = "에러 메시지";
        // 시큐리티 로그인 예외가 발생이 되면 exception 별로
        if (exception instanceof BadCredentialsException) {
            errorMassage = "아이디 또는 비밀번호가 틀립니다. 다시 확인해주세요.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMassage = "내부적으로 발생한 시스템 문제로 요청을 처리 할 수 없습니다.";
        }else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMassage = "인증요청이 거부되었습니다. 관리자에게 문의바랍니다.";
        }else if (exception instanceof UsernameNotFoundException) {
            errorMassage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
        } else {
            errorMassage = "알 수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의바랍니다.";
        }
        errorMassage = URLEncoder.encode(errorMassage,"UTF-8");
        // /member/login 페이지로 error, exception
        setDefaultFailureUrl("/member/login?error=true&exception=" + errorMassage);

        super.onAuthenticationFailure(request, response, exception);
    }
}
