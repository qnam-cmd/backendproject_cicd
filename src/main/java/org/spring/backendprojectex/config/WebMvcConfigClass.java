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

        String itemPath = itemImgPath.replace("file://", "");
        String boardPath = boardImgPath.replace("file://", "");

        // 브라우저에서 "/uploads/item/..." 로 요청하면 도커 내부의 file:/app/uploads/item/ 파일을 찾아주도록 변경
        registry.addResourceHandler("/uploads/item/**")
                .addResourceLocations("file:" + itemPath);

        registry.addResourceHandler("/uploads/board/**")
                .addResourceLocations("file:" + boardPath);
    }
}
