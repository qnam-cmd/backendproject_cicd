package org.spring.backendprojectex.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;

import static org.hibernate.query.sqm.tree.SqmNode.log;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("============= authentication ================");
        log.info(authentication);
        log.info("============= Principal ================");
        log.info(authentication.getPrincipal() + "Principal"); // 로그인사용자
        log.info("============= getAuthorities ================");
        log.info(authentication.getAuthorities());// 사용자 권한


        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();

        out.println(
                "<script> alert('"
                        + authentication.getName()
                        + " 님 반갑습니다.');"
                        + "location.href='/' ;"
                        + "</script>"
        );
        out.close();

    }
}
