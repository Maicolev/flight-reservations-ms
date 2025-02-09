package com.example.reservation;

import com.example.common.config.DatabaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DatabaseConfig.class)
public class PublishApplication {
    public static void main(String[] args) {
        SpringApplication.run(PublishApplication.class, args);
    }
}