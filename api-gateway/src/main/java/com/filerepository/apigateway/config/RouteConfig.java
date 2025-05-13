package com.filerepository.apigateway.config;

import com.filerepository.apigateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    private final AuthenticationFilter authenticationFilter;

    public RouteConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service-public", r -> r
                        .path("/api/v1/auth/**")
                        .uri("lb://user-service"))
                .route("user-service-public", r -> r
                        .path("/api/users/login", "/api/users/register")
                        .uri("lb://user-service"))
                .route("user-service-protected", r -> r
                        .path("/api/users/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://user-service"))
                .route("file-service-protected", r -> r
                        .path("/api/files/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://file-service"))
                .route("repository-service-protected", r -> r
                        .path("/api/repositories/**", "/api/comments/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://repository-service"))
                .route("user-service-docs", r -> r
                        .path("/user-service/v3/api-docs/**")
                        .uri("lb://user-service"))
                .route("file-service-docs", r -> r
                        .path("/file-service/v3/api-docs/**")
                        .uri("lb://file-service"))
                .route("repository-service-docs", r -> r
                        .path("/repository-service/v3/api-docs/**")
                        .uri("lb://repository-service"))
                .route("swagger-ui", r -> r
                        .path("/swagger-ui/**")
                        .uri("lb://api-gateway"))
                .build();
    }
}
