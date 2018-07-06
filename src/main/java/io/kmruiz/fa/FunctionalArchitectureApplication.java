package io.kmruiz.fa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.io.IOException;

@SpringBootApplication
@EnableElasticsearchRepositories
public class FunctionalArchitectureApplication {

    public static void main(String[] args) {
        SpringApplication.run(FunctionalArchitectureApplication.class, args);
    }
}
