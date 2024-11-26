package com.ada.genealogyapp.person.repostitory;

import com.ada.genealogyapp.person.dto.PersonFamilyResponse;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;


public interface PersonRepository extends Neo4jRepository<Person, UUID> {
    @Query("MATCH (p:Person)-[:PARENT_OF]->(c:Person) WHERE c.id = $childId RETURN p")
    Set<Person> findParentsOf(UUID childId);

    @Query(value = """
            MATCH (t:Tree)-[:HAS_PERSON]->(p:Person)
            WHERE t.id = $treeId
            AND (toLower(p.firstname) CONTAINS toLower($firstname) OR $firstname = '')
            AND (toLower(p.lastname) CONTAINS toLower($lastname) OR $lastname = '')
            AND (p.gender = $gender OR $gender = '')
            RETURN p.id AS id,
                    p.firstname AS firstname,
                    p.lastname AS lastname,
                    p.birthdate AS birthdate,
                    p.gender AS gender
            :#{orderBy(#pageable)}
            SKIP $skip
            LIMIT $limit
            """,
            countQuery = """
                        MATCH (t:Tree)-[:HAS_PERSON]->(p:Person)
                        WHERE t.id = $treeId
                        RETURN count(p)
                    """)
    Page<PersonResponse> findByTreeIdAndFilteredFirstnameLastnameAndGender(@Param("treeId") UUID treeId, String firstname, String lastname, String gender, Pageable pageable);


    @Query(value = """
            MATCH (f:Family)
            WHERE (f)-[:HAS_FATHER]->(:Person {id: $personId})
               OR (f)-[:HAS_MOTHER]->(:Person {id: $personId})
               OR (f)-[:HAS_CHILD]->(:Person {id: $personId})
            WITH f
            OPTIONAL MATCH (f)-[:HAS_FATHER]->(father:Person)
            OPTIONAL MATCH (f)-[:HAS_MOTHER]->(mother:Person)
            OPTIONAL MATCH (f)-[:HAS_CHILD]->(child:Person)
            WITH f, father, mother, child
            RETURN f.id AS id,
                   father.name AS fatherName,
                   father.id AS fatherId,
                   father.birthdate AS fatherBirthdate,
                   mother.name AS motherName,
                   mother.id AS motherId,
                   mother.birthdate AS motherBirthdate,
                   COLLECT({
                           childId: child.id,
                           childName: child.name,
                           childBirthdate: child.birthdate
                       }) AS children
                       :#{orderBy(#pageable)}
                        SKIP $skip
                        LIMIT $limit
                        """,
            countQuery = """
                         MATCH (f:Family)
                         WHERE (f)-[:HAS_FATHER]->(:Person {id: $personId})
                            OR (f)-[:HAS_MOTHER]->(:Person {id: $personId})
                            OR (f)-[:HAS_CHILD]->(:Person {id: $personId})
                         RETURN count(f)
                    """)
    Page<PersonFamilyResponse> findPersonalFamilies(@Param("personId") UUID personId, Pageable pageable);

}

