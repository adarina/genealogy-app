package com.ada.genealogyapp.person.repository;

import com.ada.genealogyapp.person.dto.*;
import com.ada.genealogyapp.person.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    Set<PersonResponse> findAncestor(String userId, String treeId, String childId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_PERSON]->(person:Person {id: $personId})
            OPTIONAL MATCH (person)<-[:HAS_PARTICIPANT]-(event:Event)
            OPTIONAL MATCH (event)-[:HAS_EVENT_LOCATION]->(location:Location)
            RETURN person.id AS id,
                   collect(DISTINCT {
                       type: location.type,
                       id: location.id,
                       name: location.name,
                       latitude: location.latitude,
                       longitude: location.longitude
                   }) AS locations
            """)
    PersonGeographyResponse findGeography(String userId, String treeId, String personId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_PERSON]->(child:Person {id: $childId})
            MATCH (ancestor:Person)-[:PARENT_OF]->(child)
            OPTIONAL MATCH (ancestor)<-[:HAS_PARTICIPANT]-(event:Event)
            OPTIONAL MATCH (event)-[:HAS_EVENT_LOCATION]->(location:Location)
            
            RETURN ancestor.id AS id,
                   collect(DISTINCT {
                       type: location.type,
                       id: location.id,
                       name: location.name,
                       latitude: location.latitude,
                       longitude: location.longitude
                   }) AS locations
            """)
    Set<PersonGeographyResponse> findAncestorGeography(String userId, String treeId, String childId);

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
                        OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person)
                        OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person)
                        SET family.name = COALESCE(father.name, "null") + " & " + COALESCE(mother.name, "null")
            
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
            
            OPTIONAL MATCH (person)<-[:HAS_PARTICIPANT {relationship: 'MAIN'}]-(birthEvent:Event {type: 'BIRTH'})
            WITH person, head(collect(birthEvent)) AS singleBirthEvent
            
            OPTIONAL MATCH (person)<-[:HAS_PARTICIPANT {relationship: 'MAIN'}]-(christeningEvent:Event {type: 'CHRISTENING'})
            WITH person, singleBirthEvent, head(collect(christeningEvent)) AS singleChristeningEvent
            
            OPTIONAL MATCH (person)<-[:HAS_PARTICIPANT {relationship: 'MAIN'}]-(deathEvent:Event {type: 'DEATH'})
            WITH person, singleBirthEvent, singleChristeningEvent, head(collect(deathEvent)) AS singleDeathEvent
            
            OPTIONAL MATCH (person)<-[:HAS_PARTICIPANT {relationship: 'MAIN'}]-(burialEvent:Event {type: 'BURIAL'})
            WITH person, singleBirthEvent, singleChristeningEvent, singleDeathEvent, head(collect(burialEvent)) AS singleBurialEvent
            
            RETURN person.id AS id,
                   person.firstname AS firstname,
                   person.lastname AS lastname,
                   person.name AS name,
                   COALESCE(singleBirthEvent.date, singleChristeningEvent.date) AS birthdate,
                   COALESCE(singleDeathEvent.date, singleBurialEvent.date) AS deathdate,
                   person.gender AS gender
            """)
    PersonResponse find(String userId, String treeId, String personId);

    @Query(
            value = """
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
                    
                        WITH person,
                             TRIM(toUpper(NULLIF(COALESCE(birthEvent.date, christeningEvent.date), ''))) AS birthdate_str,
                             TRIM(toUpper(NULLIF(COALESCE(deathEvent.date, burialEvent.date), ''))) AS deathdate_str
                        WHERE person IS NOT NULL
                             AND (toLower(birthdate_str) CONTAINS toLower($birthdateFilter) OR $birthdateFilter = '')
                             AND (toLower(deathdate_str) CONTAINS toLower($deathdateFilter) OR $deathdateFilter = '')
                    
                        WITH person, birthdate_str, deathdate_str,
                             CASE
                                 WHEN birthdate_str IS NULL THEN NULL
                                 ELSE
                                     CASE
                                         WHEN birthdate_str =~ '(EXACT|EST|CAL) BET [A-Z]{3} \\d{4} AND \\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 ' + TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' BET ') + 5, apoc.text.indexOf(birthdate_str, ' AND ') - apoc.text.indexOf(birthdate_str, ' BET ') - 5)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '(EXACT|EST|CAL) BET \\d{1,2} [A-Z]{3} \\d{4} AND [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' BET ') + 5, apoc.text.indexOf(birthdate_str, ' AND ') - apoc.text.indexOf(birthdate_str, ' BET ') - 5)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '(EXACT|EST|CAL) BET \\d{1,2} [A-Z]{3} \\d{4} AND \\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' BET ') + 5, apoc.text.indexOf(birthdate_str, ' AND ') - apoc.text.indexOf(birthdate_str, ' BET ') - 5)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '(EXACT|EST|CAL) BET [A-Z]{3} \\d{4} AND [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 ' + TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' BET ') + 5, apoc.text.indexOf(birthdate_str, ' AND ') - apoc.text.indexOf(birthdate_str, ' BET ') - 5)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '(EXACT|EST|CAL) BET \\d{4} AND \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 JAN ' + TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' BET ') + 5, apoc.text.indexOf(birthdate_str, ' AND ') - apoc.text.indexOf(birthdate_str, ' BET ') - 5)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ 'BET [A-Z]{3} \\d{4} AND \\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 ' + TRIM(SUBSTRING(birthdate_str, 4, apoc.text.indexOf(birthdate_str, ' AND ') - 4)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ 'BET \\d{1,2} [A-Z]{3} \\d{4} AND [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(birthdate_str, 4, apoc.text.indexOf(birthdate_str, ' AND ') - 4)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ 'BET \\d{1,2} [A-Z]{3} \\d{4} AND \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(birthdate_str, 4, apoc.text.indexOf(birthdate_str, ' AND ') - 4)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ 'BET \\d{4} AND \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 JAN ' + TRIM(SUBSTRING(birthdate_str, 4, apoc.text.indexOf(birthdate_str, ' AND ') - 4)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '(EXACT|EST|CAL) (EXACT|AFT|BEF|ABT) \\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' ', apoc.text.indexOf(birthdate_str, ' ') + 1) + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '(EXACT|EST|CAL) (EXACT|AFT|BEF|ABT) [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 ' + TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' ', apoc.text.indexOf(birthdate_str, ' ') + 1) + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '(EXACT|EST|CAL) (EXACT|AFT|BEF|ABT) \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 JAN ' + TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' ', apoc.text.indexOf(birthdate_str, ' ') + 1) + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '(EXACT|AFT|BEF|ABT) \\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' ') + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '(EXACT|AFT|BEF|ABT) [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 ' + TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' ') + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '(EXACT|AFT|BEF|ABT) \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 JAN ' + TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' ') + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '(EXACT|EST|CAL) \\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' ') + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '(EXACT|EST|CAL) [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 ' + TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' ') + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '(EXACT|EST|CAL) \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 JAN ' + TRIM(SUBSTRING(birthdate_str, apoc.text.indexOf(birthdate_str, ' ') + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN birthdate_str =~ '\\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(birthdate_str, 'ms', 'dd MMM yyyy')
                                         WHEN birthdate_str =~ '[A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse('01 ' + birthdate_str, 'ms', 'dd MMM yyyy')
                                         WHEN birthdate_str =~ '\\d{4}' THEN
                                             apoc.date.parse('01 JAN ' + birthdate_str, 'ms', 'dd MMM yyyy')
                    
                                         ELSE NULL
                                     END
                             END AS birthdate_sortable,
                             CASE
                                 WHEN deathdate_str IS NULL THEN NULL
                                 ELSE
                                     CASE
                                         WHEN deathdate_str =~ '(EXACT|EST|CAL) BET [A-Z]{3} \\d{4} AND \\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 ' + TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' BET ') + 5, apoc.text.indexOf(deathdate_str, ' AND ') - apoc.text.indexOf(deathdate_str, ' BET ') - 5)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '(EXACT|EST|CAL) BET \\d{1,2} [A-Z]{3} \\d{4} AND [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' BET ') + 5, apoc.text.indexOf(deathdate_str, ' AND ') - apoc.text.indexOf(deathdate_str, ' BET ') - 5)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '(EXACT|EST|CAL) BET \\d{1,2} [A-Z]{3} \\d{4} AND \\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' BET ') + 5, apoc.text.indexOf(deathdate_str, ' AND ') - apoc.text.indexOf(deathdate_str, ' BET ') - 5)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '(EXACT|EST|CAL) BET [A-Z]{3} \\d{4} AND [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 ' + TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' BET ') + 5, apoc.text.indexOf(deathdate_str, ' AND ') - apoc.text.indexOf(deathdate_str, ' BET ') - 5)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '(EXACT|EST|CAL) BET \\d{4} AND \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 JAN ' + TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' BET ') + 5, apoc.text.indexOf(deathdate_str, ' AND ') - apoc.text.indexOf(deathdate_str, ' BET ') - 5)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ 'BET [A-Z]{3} \\d{4} AND \\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 ' + TRIM(SUBSTRING(deathdate_str, 4, apoc.text.indexOf(deathdate_str, ' AND ') - 4)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ 'BET \\d{1,2} [A-Z]{3} \\d{4} AND [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(deathdate_str, 4, apoc.text.indexOf(deathdate_str, ' AND ') - 4)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ 'BET \\d{1,2} [A-Z]{3} \\d{4} AND \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(deathdate_str, 4, apoc.text.indexOf(deathdate_str, ' AND ') - 4)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ 'BET \\d{4} AND \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 JAN ' + TRIM(SUBSTRING(deathdate_str, 4, apoc.text.indexOf(deathdate_str, ' AND ') - 4)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '(EXACT|EST|CAL) (EXACT|AFT|BEF|ABT) \\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' ', apoc.text.indexOf(deathdate_str, ' ') + 1) + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '(EXACT|EST|CAL) (EXACT|AFT|BEF|ABT) [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 ' + TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' ', apoc.text.indexOf(deathdate_str, ' ') + 1) + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '(EXACT|EST|CAL) (EXACT|AFT|BEF|ABT) \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 JAN ' + TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' ', apoc.text.indexOf(deathdate_str, ' ') + 1) + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '(EXACT|AFT|BEF|ABT) \\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' ') + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '(EXACT|AFT|BEF|ABT) [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 ' + TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' ') + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '(EXACT|AFT|BEF|ABT) \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 JAN ' + TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' ') + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '(EXACT|EST|CAL) \\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' ') + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '(EXACT|EST|CAL) [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 ' + TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' ') + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '(EXACT|EST|CAL) \\d{4}' THEN
                                             apoc.date.parse(
                                                 '01 JAN ' + TRIM(SUBSTRING(deathdate_str, apoc.text.indexOf(deathdate_str, ' ') + 1)),
                                                 'ms', 'dd MMM yyyy'
                                             )
                                         WHEN deathdate_str =~ '\\d{1,2} [A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse(deathdate_str, 'ms', 'dd MMM yyyy')
                                         WHEN deathdate_str =~ '[A-Z]{3} \\d{4}' THEN
                                             apoc.date.parse('01 ' + deathdate_str, 'ms', 'dd MMM yyyy')
                                         WHEN deathdate_str =~ '\\d{4}' THEN
                                             apoc.date.parse('01 JAN ' + deathdate_str, 'ms', 'dd MMM yyyy')
                    
                                         ELSE NULL
                                     END
                             END AS deathdate_sortable
                        WITH person, birthdate_str, deathdate_str, birthdate_sortable, deathdate_sortable,
                             CASE WHEN birthdate_sortable IS NULL THEN 1 ELSE 0 END AS birthdate_isnull,
                             CASE WHEN deathdate_sortable IS NULL THEN 1 ELSE 0 END AS deathdate_isnull,
                             CASE WHEN person.firstname IS NULL OR person.firstname = '' THEN 1 ELSE 0 END AS firstname_isnull,
                             CASE WHEN person.lastname IS NULL OR person.lastname = '' THEN 1 ELSE 0 END AS lastname_isnull,
                             CASE WHEN person.gender IS NULL OR person.gender = '' THEN 1 ELSE 0 END AS gender_isnull
                        RETURN person.id AS id,
                               person.firstname AS firstname,
                               person.lastname AS lastname,
                               person.gender AS gender,
                               birthdate_str AS birthdate,
                               deathdate_str AS deathdate
                    
                        ORDER BY
                            CASE WHEN $sortProperty = 'birthdate' THEN birthdate_isnull END ASC,
                            CASE WHEN $sortProperty = 'birthdate' AND $sortDirection = 'ASC' THEN birthdate_sortable END ASC,
                            CASE WHEN $sortProperty = 'birthdate' AND $sortDirection = 'DESC' THEN birthdate_sortable END DESC,
                    
                            CASE WHEN $sortProperty = 'deathdate' THEN deathdate_isnull END ASC,
                            CASE WHEN $sortProperty = 'deathdate' AND $sortDirection = 'ASC' THEN deathdate_sortable END ASC,
                            CASE WHEN $sortProperty = 'deathdate' AND $sortDirection = 'DESC' THEN deathdate_sortable END DESC,
                    
                            CASE WHEN $sortProperty = 'firstname' THEN firstname_isnull END ASC,
                            CASE WHEN $sortProperty = 'firstname' AND $sortDirection = 'ASC' THEN person.firstname END ASC,
                            CASE WHEN $sortProperty = 'firstname' AND $sortDirection = 'DESC' THEN person.firstname END DESC,
                    
                            CASE WHEN $sortProperty = 'lastname' THEN lastname_isnull END ASC,
                            CASE WHEN $sortProperty = 'lastname' AND $sortDirection = 'ASC' THEN person.lastname END ASC,
                            CASE WHEN $sortProperty = 'lastname' AND $sortDirection = 'DESC' THEN person.lastname END DESC,
                    
                            CASE WHEN $sortProperty = 'gender' THEN gender_isnull END ASC,
                            CASE WHEN $sortProperty = 'gender' AND $sortDirection = 'ASC' THEN person.gender END ASC,
                            CASE WHEN $sortProperty = 'gender' AND $sortDirection = 'DESC' THEN person.gender END DESC
                        SKIP $skip
                        LIMIT $limit
                    """,
            countQuery = """
                        MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_PERSON]->(person:Person)
                                                                WHERE
                                                                    (toLower(person.firstname) CONTAINS toLower($firstname) OR $firstname = '')
                                                                    AND (toLower(person.lastname) CONTAINS toLower($lastname) OR $lastname = '')
                                                                    AND (toUpper(person.gender) = toUpper($gender) OR $gender = '')
                    
                                                                OPTIONAL MATCH (person)<-[birthRel:HAS_PARTICIPANT {relationship: "MAIN"}]-(birthEvent:Event {type: "BIRTH"})
                                                                OPTIONAL MATCH (person)<-[christeningRel:HAS_PARTICIPANT {relationship: "MAIN"}]-(christeningEvent:Event {type: "CHRISTENING"})
                                                                OPTIONAL MATCH (person)<-[deathRel:HAS_PARTICIPANT {relationship: "MAIN"}]-(deathEvent:Event {type: "DEATH"})
                                                                OPTIONAL MATCH (person)<-[burialRel:HAS_PARTICIPANT {relationship: "MAIN"}]-(burialEvent:Event {type: "BURIAL"})
                    
                                                                WITH person,
                                                                     TRIM(toUpper(NULLIF(COALESCE(birthEvent.date, christeningEvent.date), ''))) AS birthdate_str,
                                                                     TRIM(toUpper(NULLIF(COALESCE(deathEvent.date, burialEvent.date), ''))) AS deathdate_str
                                                                WHERE\s
                                                                    person IS NOT NULL
                                                                    AND (toLower(birthdate_str) CONTAINS toLower($birthdateFilter) OR $birthdateFilter = '')
                                                                    AND (toLower(deathdate_str) CONTAINS toLower($deathdateFilter) OR $deathdateFilter = '')
                    
                                                                RETURN count(person)
                    """)
    Page<PersonResponse> find(String userId, String treeId, String firstname, String lastname, String gender, String birthdateFilter, String deathdateFilter, String sortProperty, String sortDirection, Pageable pageable);

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

    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_PERSON]->(person:Person {id: $personId})
            MATCH (family:Family)
            WHERE (family)-[:HAS_CHILD]->(person)
               OR (family)-[:HAS_MOTHER]->(person)
               OR (family)-[:HAS_FATHER]->(person)
            RETURN family.id AS id,
                   COALESCE((family)-[:HAS_FATHER]->(person) OR (family)-[:HAS_MOTHER]->(person), false) AS isParent
            """)
    List<PersonFamilyGedcomResponse> findFamilies(String userId, String treeId, String personId);

    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_PERSON]->(person:Person)
            RETURN person.id AS id,
                   person.firstname AS firstname,
                   person.lastname AS lastname,
                   person.gender AS gender,
                   person.name AS name
            """)
    Set<PersonExportResponse> find(String userId, String treeId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_PERSON]->(person:Person {id: $personId})
            MATCH (person)-[r:PARENT_OF]->(child:Person)
            RETURN child.id AS childId,
                   r.relationship AS relationship
            """)
    Set<PersonRelationshipExportResponse> findRelationships(String userId, String treeId, String personId);

}

