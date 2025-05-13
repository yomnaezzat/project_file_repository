package com.filerepository.repositoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.filerepository.repositoryservice.client")
public class RepositoryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RepositoryServiceApplication.class, args);
    }
}
