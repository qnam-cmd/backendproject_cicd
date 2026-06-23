package org.spring.backendprojectex.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    // http://localhost:8090/swagger-ui/index.html

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(info());
    }

    private Info info() {
        return new Info()
                .title("API Swagger Test")                               // Swagger UI화면 맨위에 노출될 제목
                .description("RestApiController Test Swagger!!")    // 문서에 대한 설명
                .version("1.0.0");                                    // API의 현재버전
    }
}
