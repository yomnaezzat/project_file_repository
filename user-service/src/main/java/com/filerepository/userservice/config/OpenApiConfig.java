package com.filerepository.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("User Service API")
                        .description("User authentication and management service")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("File Repository Team")
                                .email("support@filerepository.com")));
    }
}