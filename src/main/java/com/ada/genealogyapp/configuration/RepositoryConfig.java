package com.ada.genealogyapp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.ada.genealogyapp.user.repository"
)
@EnableNeo4jRepositories(
        basePackages = {"com.ada.genealogyapp.family.repository",
                "com.ada.genealogyapp.citation.repository",
                "com.ada.genealogyapp.source.repository",
                "com.ada.genealogyapp.graphuser.repository",
                "com.ada.genealogyapp.person.repository",
                "com.ada.genealogyapp.file.repository",
                "com.ada.genealogyapp.tree.repository",
                "com.ada.genealogyapp.event.repository",
                "com.ada.genealogyapp.participant.repository"}
)
public class RepositoryConfig {
}
