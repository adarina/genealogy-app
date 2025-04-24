package com.ada.genealogyapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jReactiveDataAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.config.EnableSpringDataWebSupport;



//@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy
//@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableDiscoveryClient
@SpringBootApplication(exclude = {Neo4jReactiveDataAutoConfiguration.class})

public class GenealogyAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenealogyAppApplication.class, args);
    }
}
