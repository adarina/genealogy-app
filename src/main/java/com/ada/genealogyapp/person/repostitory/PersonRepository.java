package com.ada.genealogyapp.person.repostitory;

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
                OPTIONAL MATCH (tree:Tree {id: $treeId})
                RETURN count(tree) > 0 AS treeExist, tree
            }
                        
            CALL apoc.do.case(
                [
                    treeExist, '
                        MERGE (tree)-[:HAS_PERSON]->(person:Person {id: personId})
                        SET person.firstname = $firstname,
                            person.lastname = $lastname,
                            person.gender = $gender,
                            person.name = $firstname + " " + $lastname,
                            person:Participant
                            
                        RETURN "PERSON_CREATED" AS message
                    '
                ],
                'RETURN "TREE_NOT_EXIST" AS message',
                {tree: tree, personId: $personId, firstname: $firstname, lastname: $lastname, gender: $gender}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String save(String treeId, String personId, String firstname, String lastname, String gender);

    @Query("""
            CALL {
                OPTIONAL MATCH (tree:Tree {id: $treeId})
                WITH count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person {id: $personId})
                RETURN treeExist, tree, count(person) > 0 AS personExist, person
            }
                        
            CALL apoc.do.case(
                [
                    treeExist AND personExist, '
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
                    treeExist, 'RETURN "PERSON_NOT_EXIST" AS message'
                ],
                'RETURN "TREE_NOT_EXIST" AS message',
                {tree: tree, person: person, firstname: $firstname, lastname: $lastname, gender: $gender}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String update(String treeId, String personId, String firstname, String lastname, String gender);

    @Query("""
            CALL {
                OPTIONAL MATCH (tree:Tree {id: $treeId})
                WITH count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person {id: $personId})
                RETURN treeExist, tree, count(person) > 0 AS personExist, person
            }
                        
            CALL apoc.do.case(
                [
                    treeExist AND personExist, '
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
                    treeExist, 'RETURN "PERSON_NOT_EXIST" AS message'
                ],
                'RETURN "TREE_NOT_EXIST" AS message',
                {tree: tree, person: person}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String delete(String treeId, String personId);

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
            MATCH (t:Tree)-[:HAS_PERSON]->(p:Person)
            WHERE t.id = $treeId
            AND (toLower(p.firstname) CONTAINS toLower($firstname) OR $firstname = '')
            AND (toLower(p.lastname) CONTAINS toLower($lastname) OR $lastname = '')
            AND (p.gender = $gender OR $gender = '')
            WITH p
            OPTIONAL MATCH (p)<-[:HAS_PARTICIPANT]-(birthEvent:Event {type: 'BIRTH'})
            OPTIONAL MATCH (p)<-[:HAS_PARTICIPANT]-(christeningEvent:Event {type: 'CHRISTENING'})
            OPTIONAL MATCH (p)<-[:HAS_PARTICIPANT]-(deathEvent:Event {type: 'DEATH'})
            OPTIONAL MATCH (p)<-[:HAS_PARTICIPANT]-(burialEvent:Event {type: 'BURIAL'})
            RETURN p.id AS id,
                   p.firstname AS firstname,
                   p.lastname AS lastname,
                   COALESCE(birthEvent.date, christeningEvent.date) AS birthdate,
                   COALESCE(deathEvent.date, burialEvent.date) AS deathdate,
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
    Page<PersonResponse> findByTreeIdAndFilteredFirstnameLastnameAndGender(@Param("treeId") String treeId, String firstname, String lastname, String gender, Pageable pageable);

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

