package com.ada.genealogyapp;

import com.ada.genealogyapp.file.properties.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableNeo4jRepositories
@EnableConfigurationProperties(StorageProperties.class)
@Slf4j
@EnableAspectJAutoProxy
public class GenealogyAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenealogyAppApplication.class, args);
    }


}
