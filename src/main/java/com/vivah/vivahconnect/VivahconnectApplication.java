package com.vivah.vivahconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.vivah.vivahconnect.repository")
public class VivahconnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(VivahconnectApplication.class, args);
    }
}
