package com.ada.genealogyapp.person.repository;

import com.ada.genealogyapp.person.dto.PersonFamilyResponse;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;


public interface PersonRepository extends Neo4jRepository<Person, String> {
    @Query("MATCH (p:Person)-[:PARENT_OF]->(c:Person) WHERE c.id = $childId RETURN p")
    Set<Person> findParentsOf(String childId);

    @Query("""
            MATCH (t:Tree {id: $treeId})-[:HAS_PERSON]->(p:Person {id: $personId})
            RETURN p
            """)
    Person findPersonByTreeIdAndPersonId(String treeId, String personId);


    @Query("""
            MATCH (t:Tree {id: $treeId})
            MATCH (t)-[:HAS_PERSON]->(p:Person {id: $personId})
            MATCH (t)-[:HAS_PERSON]->(c:Person {id: $childId})
            MERGE (p)-[r:PARENT_OF {relationship: $relationshipType}]->(c)
            """)
    void addParentChildRelationship(@Param("treeId") String treeId, @Param("personId") String personId, @Param("childId") String childId, @Param("relationshipType") String relationshipType);


    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                RETURN userExist, count(tree) > 0 AS treeExist, tree
            }
                            
            CALL apoc.do.case(
                [
                    userExist AND treeExist, '
                        MERGE (tree)-[:HAS_PERSON]->(person:Person {id: $personId})
                        SET person.firstname = $firstname,
                            person.lastname = $lastname,
                            person.gender = $gender,
                            person.name = COALESCE($firstname, "") + " " + COALESCE($lastname, ""),
                            person:Participant
                            
                        RETURN "PERSON_CREATED" AS message
                    ',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, personId: $personId, firstname: $firstname, lastname: $lastname, gender: $gender}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String save(String userId, String treeId, String personId, String firstname, String lastname, String gender);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person {id: $personId})
                RETURN userExist, treeExist, tree, count(person) > 0 AS personExist, person
            }
                            
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND personExist, '
                        SET person.firstname = $firstname,
                            person.lastname = $lastname,
                            person.gender = $gender,
                            person.name = COALESCE($firstname, "") + " " + COALESCE($lastname, "")
                                                    
                        WITH tree, person
                        OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family)
                        WHERE (family)-[:HAS_FATHER]->(person) OR (family)-[:HAS_MOTHER]->(person)
                        WITH person, COLLECT(family) AS families
                        
                        UNWIND families AS fam
                        OPTIONAL MATCH (fam)-[:HAS_FATHER]->(father:Person)
                        OPTIONAL MATCH (fam)-[:HAS_MOTHER]->(mother:Person)
                        SET fam.name = COALESCE(father.name, "null") + " & " + COALESCE(mother.name, "null")
                        
                        RETURN "PERSON_UPDATED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "PERSON_NOT_EXIST" AS message',
                    treeExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, person: person, firstname: $firstname, lastname: $lastname, gender: $gender}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String update(String userId, String treeId, String personId, String firstname, String lastname, String gender);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person {id: $personId})
                RETURN userExist, treeExist, tree, count(person) > 0 AS personExist, person
            }
                            
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND personExist, '
                        SET person.name = "null"
                        WITH tree, person
                        OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family)
                        WHERE (family)-[:HAS_FATHER]->(person) OR (family)-[:HAS_MOTHER]->(person)
                        WITH person, COLLECT(family) AS families
                        
                        UNWIND families AS fam
                        OPTIONAL MATCH (fam)-[:HAS_FATHER]->(father:Person)
                        OPTIONAL MATCH (fam)-[:HAS_MOTHER]->(mother:Person)
                        SET fam.name = COALESCE(father.name, "null") + " & " + COALESCE(mother.name, "null")
                        WITH person
                        OPTIONAL MATCH (person)-[rel]-()
                        DELETE rel, person
                        RETURN "PERSON_DELETED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "PERSON_NOT_EXIST" AS message',
                    treeExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, person: person}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String delete(String userId, String treeId, String personId);


    @Query("""
            MATCH (t:Tree)-[:HAS_PERSON]->(p:Person)
            WHERE t.id = $treeId AND p.id = $personId
            WITH p
            OPTIONAL MATCH (p)<-[:HAS_PARTICIPANT]-(birthEvent:Event {type: 'BIRTH'})
            OPTIONAL MATCH (p)<-[:HAS_PARTICIPANT]-(christeningEvent:Event {type: 'CHRISTENING'})
            OPTIONAL MATCH (p)<-[:HAS_PARTICIPANT]-(deathEvent:Event {type: 'DEATH'})
            OPTIONAL MATCH (p)<-[:HAS_PARTICIPANT]-(burialEvent:Event {type: 'BURIAL'})
            RETURN p.id AS id,
                   p.firstname AS firstname,
                   p.lastname AS lastname,
                   p.name AS name,
                   COALESCE(birthEvent.date, christeningEvent.date) AS birthdate,
                   COALESCE(deathEvent.date, burialEvent.date) AS deathdate,
                   p.gender AS gender
            """)
    Optional<PersonResponse> findByTreeIdAndId(@Param("treeId") String treeId, @Param("personId") String personId);

    @Query(value = """
                MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person)
                WHERE
                    ($firstname = "" OR toLower(person.firstname) CONTAINS toLower($firstname))
                    AND ($lastname = "" OR toLower(person.lastname) CONTAINS toLower($lastname))
                    AND ($gender = "" OR person.gender = $gender)
                OPTIONAL MATCH (person)<-[:HAS_PARTICIPANT]-(birthEvent:Event {type: "BIRTH"})
                OPTIONAL MATCH (person)<-[:HAS_PARTICIPANT]-(christeningEvent:Event {type: "CHRISTENING"})
                OPTIONAL MATCH (person)<-[:HAS_PARTICIPANT]-(deathEvent:Event {type: "DEATH"})
                OPTIONAL MATCH (person)<-[:HAS_PARTICIPANT]-(burialEvent:Event {type: "BURIAL"})
                WITH person, birthEvent, christeningEvent, deathEvent, burialEvent
                WHERE person IS NOT NULL
                RETURN person.id AS id,
                       person.firstname AS firstname,
                       person.lastname AS lastname,
                       COALESCE(birthEvent.date, christeningEvent.date) AS birthdate,
                       COALESCE(deathEvent.date, burialEvent.date) AS deathdate,
                       person.gender AS gender
                :#{orderBy(#pageable)}
                SKIP $skip LIMIT $limit
            """,
            countQuery = """
                        MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_PERSON]->(person:Person)
                        WHERE
                            ($firstname = "" OR toLower(person.firstname) CONTAINS toLower($firstname))
                            AND ($lastname = "" OR toLower(person.lastname) CONTAINS toLower($lastname))
                            AND ($gender = "" OR person.gender = $gender)
                        RETURN count(person) AS count
                    """)
    Page<PersonResponse> find(String userId, String treeId, String firstname, String lastname, String gender, Pageable pageable);


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
    Page<PersonFamilyResponse> findPersonalFamilies(@Param("personId") String personId, Pageable pageable);

}

