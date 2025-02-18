package com.ada.genealogyapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.xml.sax.SAXParseException;

import java.io.IOException;


@SpringBootApplication
@EnableNeo4jRepositories
@Slf4j
@EnableAspectJAutoProxy
@EnableSpringDataWebSupport
public class GenealogyAppApplication {

    public static void main(String[] args) throws SAXParseException, IOException {
        SpringApplication.run(GenealogyAppApplication.class, args);
        GedcomConverter converter = new GedcomConverter();
        converter.convertGedcomToJson("/Users/Ada/Desktop/tree.ged", "/Users/Ada/Desktop/output.json");
    }


}
