package com.htbs.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HtbsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HtbsApplication.class, args);
    }

}
