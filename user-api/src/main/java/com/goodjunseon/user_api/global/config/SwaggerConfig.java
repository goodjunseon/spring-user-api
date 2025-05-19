package com.goodjunseon.user_api.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI cutomOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User API")
                        .version("v1.0")
                        .description("Spring Boot 기반 회원 인증/인가 시스템 API 명세서"));
    }
}
