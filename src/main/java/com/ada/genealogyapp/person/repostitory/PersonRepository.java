package com.ada.genealogyapp.person.repostitory;

import com.ada.genealogyapp.person.dto.PersonEventResponse;
import com.ada.genealogyapp.person.dto.PersonFamiliesResponse;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.type.GenderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import com.ada.genealogyapp.person.dto.PersonEventsResponse;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;


public interface PersonRepository extends Neo4jRepository<Person, UUID> {
    @Query("MATCH (p:Person)-[:PARENT_OF]->(c:Person) WHERE c.id = $childId RETURN p")
    Set<Person> findParentsOf(UUID childId);

    Page<Person> findAllByTree_Id(UUID treeId, Pageable pageable);

    Page<PersonResponse> findByTreeIdAndFirstnameContainingIgnoreCaseAndLastnameContainingIgnoreCaseAndGenderContaining(UUID treeId, String firstname, String lastname, GenderType gender, Pageable pageable);

    Page<PersonResponse> findByTreeIdAndFirstnameContainingIgnoreCaseAndLastnameContainingIgnoreCase(UUID treeId, String firstname, String lastname, Pageable pageable);

    @Query("""
                MATCH (e:Event {id: $eventId})
                MATCH (p1:Participant {id: $personId})
                MERGE (e)-[r1:HAS_PARTICIPANT]->(p1)
                WITH e, p1, r1.relationship AS relationship
                OPTIONAL MATCH (e)-[r2:HAS_PARTICIPANT]->(p2:Participant)
                WITH e, relationship, COLLECT({
                    id: COALESCE(p2.id, 'p1.id'),
                    name: COALESCE(p2.name, p1.name),
                    relationship: COALESCE(r2.relationship, relationship)
                    }) AS participants
                OPTIONAL MATCH (e)-[:HAS_EVENT_CITATION]->(c:Citation)
                WITH e, relationship, participants, COLLECT({
                    id: c.id,
                    page: c.page
                }) AS citations
                RETURN e.id AS id,
                       e.type AS type,
                       e.date AS date,
                       e.place AS place,
                       e.description AS description,
                       relationship,
                       participants,
                       citations
            """)
    Optional<PersonEventResponse> findPersonalEvent(@Param("eventId") UUID eventId, @Param("personId") UUID personId);

    @Query(value = """
            MATCH (e:Event)-[r1:HAS_PARTICIPANT]->(p1:Participant)
            WHERE p1.id = $personId
            WITH e, p1, r1.relationship AS relationship
            OPTIONAL MATCH (e)-[r2:HAS_PARTICIPANT]->(p2:Participant)
            OPTIONAL MATCH (e)-[:HAS_EVENT_CITATION]->(c:Citation)
            RETURN e.id AS id,
                    e.type AS type,
                    e.date AS date,
                    e.place AS place,
                    e.description AS description,
                    relationship,
                    COLLECT({
                        name: COALESCE(p2.name, 'p1.name')
                    }) AS participants,
                    COLLECT({
                        id: COALESCE(c.id, '')
                    }) AS citations
                    :#{orderBy(#pageable)}
                    SKIP $skip
                    LIMIT $limit
                    """,
            countQuery = """
                        MATCH (e:Event)-[r1:HAS_PARTICIPANT]->(p1:Participant)
                        WHERE p1.id = $personId
                        RETURN count(e)
                    """)
    Page<PersonEventsResponse> findPersonalEvents(@Param("personId") UUID personId, Pageable pageable);

    @Query("""
                MATCH (p:Person {id: $personId})
                MATCH (t:Tree {id: $treeId})
                MERGE (t)-[:HAS_PERSON]->(p)
                RETURN p.id AS id,
                       p.firstname AS firstname,
                       p.lastname AS lastname,
                       p.birthdate AS birthdate,
                       p.gender AS gender
            """)
    Optional<PersonResponse> findPersonResponseByTreeIdAndPersonId(@Param("treeId") UUID treeId, @Param("personId") UUID personId);

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
    Page<PersonFamiliesResponse> findPersonalFamilies(@Param("personId") UUID personId, Pageable pageable);

}

