package com.github.pdgs.MyPengAPI.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI swaggerApi() {
        return new OpenAPI()
                .info(new Info().title("MyPengAPI")
                        .description("MyPeng 앱 개발 시 사용되는 서버 API에 대한 연동 문서입니다.")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("MyPeng OverView Documentation")
                        .url("https://github.com/penguin-head-whale-shark"));

    }

}
