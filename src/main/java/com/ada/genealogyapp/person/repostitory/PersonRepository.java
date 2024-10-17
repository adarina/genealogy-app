package com.ada.genealogyapp.person.repostitory;

import com.ada.genealogyapp.person.model.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;


public interface PersonRepository extends Neo4jRepository<Person, UUID> {
    Person findByFirstnameAndLastname(String firstname, String lastname);

    Optional<Person> findPersonById(UUID personId);

    @Query("MATCH (p:Person)-[:PARENT_OF]->(c:Person) WHERE id(c) = $childId RETURN p")
    Set<Person> findParentsOf(UUID childId);
}
