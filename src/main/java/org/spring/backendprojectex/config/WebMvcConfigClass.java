package org.spring.backendprojectex.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigClass implements WebMvcConfigurer {

    @Value("${img.path.item}")
    private String itemImgPath;

    @Value("${img.path.board}")
    private String boardImgPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

//        // file:// 경로에서 "file://" 부분을 제거
//        String itemPath = itemImgPath.replace("file://","");
//        String boardPath = boardImgPath.replace("file://","");

        // 리소스 핸들러 설정
        registry.addResourceHandler("/full/upload/backend/item/**")
                        .addResourceLocations("file:///" + itemImgPath);  // 실제 이미지경로생성
        registry.addResourceHandler("/full/upload/backend/board/**")
                .addResourceLocations("file:///" + boardImgPath);
    }
}
