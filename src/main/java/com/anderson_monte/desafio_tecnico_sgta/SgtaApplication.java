package com.anderson_monte.desafio_tecnico_sgta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(
        pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
public class SgtaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SgtaApplication.class, args);
    }
}
