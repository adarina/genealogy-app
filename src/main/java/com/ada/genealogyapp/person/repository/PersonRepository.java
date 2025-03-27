package com.ada.genealogyapp.person.repository;

import com.ada.genealogyapp.person.dto.PersonFamilyResponse;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface PersonRepository extends Neo4jRepository<Person, String> {
    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_PERSON]->(child:Person {id: $childId})
            MATCH (ancestor:Person)-[:PARENT_OF]->(child)
            OPTIONAL MATCH (ancestor)<-[:HAS_PARTICIPANT]-(birthEvent:Event {type: 'BIRTH'})
            OPTIONAL MATCH (ancestor)<-[:HAS_PARTICIPANT]-(christeningEvent:Event {type: 'CHRISTENING'})
            OPTIONAL MATCH (ancestor)<-[:HAS_PARTICIPANT]-(deathEvent:Event {type: 'DEATH'})
            OPTIONAL MATCH (ancestor)<-[:HAS_PARTICIPANT]-(burialEvent:Event {type: 'BURIAL'})
                      
            RETURN ancestor.id AS id,
                   ancestor.name AS name,
                   ancestor.gender AS gender,
                   COALESCE(birthEvent.date, christeningEvent.date) AS birthdate,
                   COALESCE(deathEvent.date, burialEvent.date) AS deathdate
            """)
    Set<PersonResponse> findParentsOf(String userId, String treeId, String childId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
            MATCH (tree)-[:HAS_PERSON]->(parent:Person {id: $parentId})
            MATCH (tree)-[:HAS_PERSON]->(child:Person {id: $childId})
            MERGE (parent)-[:PARENT_OF {relationship: $relationshipType}]->(child)
            """)
    void addParentChildRelationship(String userId, String treeId, String parentId, String childId, String relationshipType);

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
                            OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person)
                            OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person)
                            SET family.name = COALESCE(father.name, "null") + " & " + COALESCE(mother.name, "null")
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
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_PERSON]->(person:Person {id: $personId})
            OPTIONAL MATCH (person)<-[:HAS_PARTICIPANT]-(birthEvent:Event {type: 'BIRTH'})
            OPTIONAL MATCH (person)<-[:HAS_PARTICIPANT]-(christeningEvent:Event {type: 'CHRISTENING'})
            OPTIONAL MATCH (person)<-[:HAS_PARTICIPANT]-(deathEvent:Event {type: 'DEATH'})
            OPTIONAL MATCH (person)<-[:HAS_PARTICIPANT]-(burialEvent:Event {type: 'BURIAL'})
            RETURN person.id AS id,
                   person.firstname AS firstname,
                   person.lastname AS lastname,
                   person.name AS name,
                   COALESCE(birthEvent.date, christeningEvent.date) AS birthdate,
                   COALESCE(deathEvent.date, burialEvent.date) AS deathdate,
                   person.gender AS gender
            """)
    PersonResponse find(String userId, String treeId, String personId);

    @Query(value = """
                MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person)
                WHERE
                    (toLower(person.firstname) CONTAINS toLower($firstname) OR $firstname = '')
                    AND (toLower(person.lastname) CONTAINS toLower($lastname) OR $lastname = '')
                    AND (toUpper(person.gender) = toUpper($gender) OR $gender = '')
                    OPTIONAL MATCH (person)<-[birthRel:HAS_PARTICIPANT {relationship: "MAIN"}]-(birthEvent:Event {type: "BIRTH"})
                    OPTIONAL MATCH (person)<-[christeningRel:HAS_PARTICIPANT {relationship: "MAIN"}]-(christeningEvent:Event {type: "CHRISTENING"})
                    OPTIONAL MATCH (person)<-[deathRel:HAS_PARTICIPANT {relationship: "MAIN"}]-(deathEvent:Event {type: "DEATH"})
                    OPTIONAL MATCH (person)<-[burialRel:HAS_PARTICIPANT {relationship: "MAIN"}]-(burialEvent:Event {type: "BURIAL"})
                    WITH person, MIN(COALESCE(birthEvent.date, christeningEvent.date)) AS birthdate,  MIN(COALESCE(deathEvent.date, burialEvent.date)) AS deathdate

                RETURN person.id AS id,
                       person.firstname AS firstname,
                       person.lastname AS lastname,
                       person.gender AS gender,
                       birthdate,
                       deathdate
                       
                :#{orderBy(#pageable)}
                SKIP $skip
                LIMIT $limit
            """,
            countQuery = """
                        MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_PERSON]->(person:Person)
                        WHERE
                            (toLower(person.firstname) CONTAINS toLower($firstname) OR $firstname = '')
                            AND (toLower(person.lastname) CONTAINS toLower($lastname) OR $lastname = '')
                            AND (toUpper(person.gender) CONTAINS toUpper($gender) OR $gender = '')
                        RETURN count(person)
                    """)
    Page<PersonResponse> find(String userId, String treeId, String firstname, String lastname, String gender, Pageable pageable);

    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_PERSON]->(person:Person {id: $personId})
            MATCH (family:Family)
            WHERE (family)-[:HAS_FATHER]->(person)
                OR (family)-[:HAS_MOTHER]->(person)
                OR (family)-[:HAS_CHILD]->(person)
            WITH family
            OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person)
            OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person)
            OPTIONAL MATCH (family)-[:HAS_CHILD]->(child:Person)
            OPTIONAL MATCH (father)<-[:HAS_PARTICIPANT]-(fatherBirthEvent:Event {type: "BIRTH"})
            OPTIONAL MATCH (father)<-[:HAS_PARTICIPANT]-(fatherChristeningEvent:Event {type: "CHRISTENING"})
            OPTIONAL MATCH (father)<-[:HAS_PARTICIPANT]-(fatherDeathEvent:Event {type: "DEATH"})
            OPTIONAL MATCH (father)<-[:HAS_PARTICIPANT]-(fatherBurialEvent:Event {type: "BURIAL"})
            OPTIONAL MATCH (mother)<-[:HAS_PARTICIPANT]-(motherBirthEvent:Event {type: "BIRTH"})
            OPTIONAL MATCH (mother)<-[:HAS_PARTICIPANT]-(motherChristeningEvent:Event {type: "CHRISTENING"})
            OPTIONAL MATCH (mother)<-[:HAS_PARTICIPANT]-(motherDeathEvent:Event {type: "DEATH"})
            OPTIONAL MATCH (mother)<-[:HAS_PARTICIPANT]-(motherBurialEvent:Event {type: "BURIAL"})
            OPTIONAL MATCH (child)<-[:HAS_PARTICIPANT]-(childBirthEvent:Event {type: "BIRTH"})
            OPTIONAL MATCH (child)<-[:HAS_PARTICIPANT]-(childChristeningEvent:Event {type: "CHRISTENING"})
            OPTIONAL MATCH (child)<-[:HAS_PARTICIPANT]-(childDeathEvent:Event {type: "DEATH"})
            OPTIONAL MATCH (child)<-[:HAS_PARTICIPANT]-(childBurialEvent:Event {type: "BURIAL"})
            WITH family, father, mother, child,
                fatherBirthEvent, fatherChristeningEvent, fatherDeathEvent, fatherBurialEvent,
                motherBirthEvent, motherChristeningEvent, motherDeathEvent, motherBurialEvent,
                childBirthEvent, childChristeningEvent, childDeathEvent, childBurialEvent
            WITH DISTINCT family, father, mother, COLLECT({child: child, childBirthEvent: childBirthEvent, childChristeningEvent: childChristeningEvent, childDeathEvent: childDeathEvent, childBurialEvent: childBurialEvent}) AS childrenEvents,
                fatherBirthEvent, fatherChristeningEvent, fatherDeathEvent, fatherBurialEvent,
                motherBirthEvent, motherChristeningEvent, motherDeathEvent, motherBurialEvent

            RETURN family.id AS id,
                   father.name AS fatherName,
                   father.id AS fatherId,
                   COALESCE(fatherBirthEvent.date, fatherChristeningEvent.date) AS fatherBirthdate,
                   COALESCE(fatherDeathEvent.date, fatherBurialEvent.date) AS fatherDeathdate,
                   mother.name AS motherName,
                   mother.id AS motherId,
                   COALESCE(motherBirthEvent.date, motherChristeningEvent.date) AS motherBirthdate,
                   COALESCE(motherDeathEvent.date, motherBurialEvent.date) AS motherDeathdate,
                   CASE
                        WHEN size(childrenEvents) = 0 THEN []
                        ELSE [childEvent IN childrenEvents WHERE childEvent.child IS NOT NULL | {
                            childId: childEvent.child.id,
                            childName: childEvent.child.name,
                            childBirthdate: COALESCE(childEvent.childBirthEvent.date, childEvent.childChristeningEvent.date),
                            childDeathdate: COALESCE(childEvent.childDeathEvent.date, childEvent.childBurialEvent.date)
                        }]
                   END AS children
                   :#{orderBy(#pageable)}
                   SKIP $skip
                   LIMIT $limit
                   """,
            countQuery = """
                         MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_PERSON]->(person:Person {id: $personId})
                         MATCH (family:Family)
                         WHERE (family)-[:HAS_FATHER]->(person)
                             OR (family)-[:HAS_MOTHER]->(person)
                             OR (family)-[:HAS_CHILD]->(person)
                         RETURN count(family)
                    """)
    Page<PersonFamilyResponse> findFamilies(String userId, String treeId, String personId, Pageable pageable);
}

