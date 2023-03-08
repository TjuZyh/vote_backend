package com.tju.bclab.vote_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tju.bclab"})
public class VoteBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoteBackendApplication.class, args);
    }

}
