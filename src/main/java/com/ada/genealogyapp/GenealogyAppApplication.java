package com.ada.genealogyapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


@SpringBootApplication
@EnableNeo4jRepositories
@Slf4j
public class GenealogyAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenealogyAppApplication.class, args);
    }


}
