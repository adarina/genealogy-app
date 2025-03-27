package com.ada.genealogyapp.family.repository;

import com.ada.genealogyapp.family.dto.*;
import com.ada.genealogyapp.family.model.Family;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FamilyRepository extends Neo4jRepository<Family, String> {

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
                        MERGE (tree)-[:HAS_FAMILY]->(family:Family {id: familyId})
                        SET family.status = status,
                            family:Participant
                            
                        RETURN "FAMILY_CREATED" AS message
                    ',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, familyId: $familyId, status: $status}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String save(String userId, String treeId, String familyId, String status);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $familyId})
                RETURN userExist, treeExist, tree, count(family) > 0 AS familyExist, family
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND familyExist, '
                        OPTIONAL MATCH (family)-[childRel:HAS_CHILD]->(child:Person)
                        OPTIONAL MATCH (family)-[:HAS_FATHER]->(:Person)-[fatherRel:PARENT_OF]->(child)
                        OPTIONAL MATCH (family)-[:HAS_MOTHER]->(:Person)-[motherRel:PARENT_OF]->(child)
                        DELETE fatherRel, motherRel
                        WITH family
                        OPTIONAL MATCH (family)-[rel]-()
                        DELETE rel, family
                        RETURN "FAMILY_DELETED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "FAMILY_NOT_EXIST" AS message',
                    treeExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, family: family}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String delete(String userId, String treeId, String familyId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $familyId})
                RETURN userExist,treeExist, count(family) > 0 AS familyExist, family
            }
                        
            CALL apoc.do.case(
                [
                   userExist AND treeExist AND familyExist, '
                        SET family.status = status
                        RETURN "FAMILY_UPDATED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "FAMILY_NOT_EXIST" AS message',
                    treeExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {family: family, status: $status}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String update(String userId, String treeId, String familyId, String status);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $familyId})
                WITH userExist, treeExist, tree, count(family) > 0 AS familyExist, family
                
                OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person {id: $personId})
                RETURN userExist, treeExist, tree, familyExist, family, count(person) > 0 AS personExist, person
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND familyExist AND personExist, '
                        OPTIONAL MATCH (family)-[oldFatherRel:HAS_FATHER]->(:Person)
                        DELETE oldFatherRel

                        WITH family, person
                        OPTIONAL MATCH (family)-[:HAS_CHILD]->(child:Person)
                        OPTIONAL MATCH (family)-[:HAS_FATHER]->(:Person)-[oldParentRel:PARENT_OF]->(child)
                        DELETE oldParentRel

                        WITH family, person, collect(child) AS children
                        FOREACH (c IN children |
                            MERGE (person)-[:PARENT_OF {relationship: "BIOLOGICAL"}]->(c)
                        )
                        MERGE (family)-[:HAS_FATHER]->(person)
                        
                        WITH family, person
                        OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person)
                        SET family.name = COALESCE(person.name, "null") + " & " + COALESCE(mother.name, "null")

                        RETURN "FATHER_ADDED_TO_FAMILY" AS message
                    ',
                    userExist AND treeExist AND familyExist, 'RETURN "PERSON_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "FAMILY_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {family: family, person: person}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String addFather(String userId, String treeId, String familyId, String personId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $familyId})
                WITH userExist, treeExist, tree, count(family) > 0 AS familyExist, family
                
                OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person {id: $personId})
                RETURN userExist, treeExist, tree, familyExist, family, count(person) > 0 AS personExist, person
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND familyExist AND personExist, '
                        OPTIONAL MATCH (family)-[oldMotherRel:HAS_MOTHER]->(:Person)
                        DELETE oldMotherRel

                        WITH family, person
                        OPTIONAL MATCH (family)-[:HAS_CHILD]->(child:Person)
                        OPTIONAL MATCH (family)-[:HAS_MOTHER]->(:Person)-[oldParentRel:PARENT_OF]->(child)
                        DELETE oldParentRel

                        WITH family, person, collect(child) AS children
                        FOREACH (c IN children |
                            MERGE (person)-[:PARENT_OF {relationship: "BIOLOGICAL"}]->(c)
                        )
                        MERGE (family)-[:HAS_MOTHER]->(person)
                        
                        WITH family, person
                        OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person)
                        SET family.name = COALESCE(father.name, "null") + " & " + COALESCE(person.name, "null")

                        RETURN "MOTHER_ADDED_TO_FAMILY" AS message
                    ',
                    userExist AND treeExist AND familyExist, 'RETURN "PERSON_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "FAMILY_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {family: family, person: person}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String addMother(String userId, String treeId, String familyId, String personId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $familyId})
                WITH userExist, treeExist, tree, count(family) > 0 AS familyExist, family
                
                OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person {id: $personId})
                RETURN userExist, treeExist, tree, familyExist, family, count(person) > 0 AS personExist, person
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND familyExist AND personExist, '
                        WHERE NOT (family)-[:HAS_CHILD]->(person)
                        CREATE (family)-[:HAS_CHILD]->(person)
                        
                        WITH family, person, fatherRelationshipType, motherRelationshipType
                        OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person)
                        OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person)
                        FOREACH (fth IN CASE WHEN father IS NOT NULL THEN [father] ELSE [] END |
                            MERGE (fth)-[:PARENT_OF {relationship: fatherRelationshipType}]->(person)
                        )
                        FOREACH (mth IN CASE WHEN mother IS NOT NULL THEN [mother] ELSE [] END |
                            MERGE (mth)-[:PARENT_OF {relationship: motherRelationshipType}]->(person)
                        )
                        RETURN "CHILD_ADDED_TO_FAMILY" AS message
                    ',
                    userExist AND treeExist AND familyExist, 'RETURN "PERSON_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "FAMILY_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {family: family, person: person, fatherRelationshipType: $fatherRelationshipType, motherRelationshipType: $motherRelationshipType}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String addChild(String userId, String treeId, String familyId, String personId, String fatherRelationshipType, String motherRelationshipType);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $familyId})
                WITH userExist, treeExist, tree, count(family) > 0 AS familyExist, family
                
                OPTIONAL MATCH (family)-[fatherRel:HAS_FATHER]->(father:Person {id: $fatherId})
                RETURN userExist, treeExist, tree, familyExist, family, count(father) > 0 AS fatherExist, father, fatherRel
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND familyExist AND fatherExist, '
                        DELETE fatherRel
                        WITH family, father
                        OPTIONAL MATCH (family)-[:HAS_CHILD]->(child:Person)
                        OPTIONAL MATCH (father)-[rel:PARENT_OF]->(child)
                        WITH family, father, child, rel
                        DELETE rel
                        WITH family
                        OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person)
                        SET family.name = "null & " + COALESCE(mother.name, "null")
                        RETURN "FATHER_REMOVED_FROM_FAMILY" AS message
                    ',
                    userExist AND treeExist AND familyExist, 'RETURN "FATHER_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "FAMILY_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {family: family, father: father, fatherRel: fatherRel}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String removeFather(String userId, String treeId, String familyId, String fatherId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $familyId})
                WITH userExist, treeExist, tree, count(family) > 0 AS familyExist, family
                
                OPTIONAL MATCH (family)-[motherRel:HAS_MOTHER]->(mother:Person {id: $motherId})
                RETURN userExist, treeExist, tree, familyExist, family, count(mother) > 0 AS motherExist, mother, motherRel
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND familyExist AND motherExist, '
                        DELETE motherRel
                        WITH family, mother
                        OPTIONAL MATCH (family)-[:HAS_CHILD]->(child:Person)
                        OPTIONAL MATCH (mother)-[rel:PARENT_OF]->(child)
                        WITH family, mother, child, rel
                        DELETE rel
                        WITH family
                        OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person)
                        SET family.name = COALESCE(father.name, "null") + "& null"
                        RETURN "MOTHER_REMOVED_FROM_FAMILY" AS message
                    ',
                    userExist AND treeExist AND familyExist, 'RETURN "MOTHER_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "FAMILY_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {family: family, mother: mother, motherRel: motherRel}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String removeMother(String userId, String treeId, String familyId, String motherId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $familyId})
                WITH userExist, treeExist, tree, count(family) > 0 AS familyExist, family
                
                OPTIONAL MATCH (family)-[childRel:HAS_CHILD]->(child:Person {id: $childId})
                RETURN userExist, treeExist, tree, familyExist, family, count(child) > 0 AS childExist, child, childRel
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND familyExist AND childExist, '
                        DELETE childRel
                        WITH family, child
                        OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person)
                        OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person)
                        OPTIONAL MATCH (mother)-[motherRel:PARENT_OF]->(child)
                        OPTIONAL MATCH (father)-[fatherRel:PARENT_OF]->(child)
                        WITH fatherRel, motherRel
                        DELETE fatherRel, motherRel
                        RETURN "CHILD_REMOVED_FROM_FAMILY" AS message
                    ',
                    userExist AND treeExist AND familyExist, 'RETURN "CHILD_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "FAMILY_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {family: family, child: child, childRel: childRel}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String removeChild(String userId, String treeId, String familyId, String childId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $familyId})
                WITH userExist, treeExist, tree, count(family) > 0 AS familyExist, family
                
                OPTIONAL MATCH (family)-[:HAS_CHILD]->(child:Person {id: $childId})
                RETURN userExist, treeExist, tree, familyExist, family, count(child) > 0 AS childExist, child
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND familyExist AND childExist, '
                        WITH family, child, fatherRelationshipType, motherRelationshipType
                        OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person)
                        OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person)
                        OPTIONAL MATCH (mother)-[motherRel:PARENT_OF]->(child)
                        OPTIONAL MATCH (father)-[fatherRel:PARENT_OF]->(child)
                        DELETE fatherRel, motherRel
                        WITH mother, father, child, fatherRelationshipType, motherRelationshipType
                        FOREACH (ignore IN CASE WHEN mother IS NOT NULL AND motherRelationshipType IS NOT NULL THEN [1] ELSE [] END |
                            CREATE (mother)-[:PARENT_OF {relationship: motherRelationshipType}]->(child)
                        )
                        FOREACH (ignore IN CASE WHEN father IS NOT NULL AND fatherRelationshipType IS NOT NULL THEN [1] ELSE [] END |
                            CREATE (father)-[:PARENT_OF {relationship: fatherRelationshipType}]->(child)
                        )
                        RETURN "CHILD_UPDATED_IN_FAMILY" AS message
                    ',
                    userExist AND treeExist AND familyExist, 'RETURN "CHILD_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "FAMILY_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {family: family, child: child, fatherRelationshipType: $fatherRelationshipType, motherRelationshipType: $motherRelationshipType}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String updateChild(String userId, String treeId, String familyId, String childId, String fatherRelationshipType, String motherRelationshipType);

    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                        
            OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family)
            WHERE toUpper(family.status) = toUpper($status) OR $status = ''
            WITH family
                        
            OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person)
            WITH family, COALESCE(father.name, '') AS fatherName, father.id AS fatherId
            WHERE toLower(fatherName) CONTAINS toLower($fatherName)
                        
            OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person)
            WITH family, fatherName, fatherId, COALESCE(mother.name, '') AS motherName, mother.id AS motherId
            WHERE toLower(motherName) CONTAINS toLower($motherName)
                        
            OPTIONAL MATCH (family)<-[:HAS_PARTICIPANT]-(marriageEvent:Event {type: 'MARRIAGE'})
                        
            RETURN family.id AS id,
                   family.status AS status,
                   motherName,
                   fatherName,
                   motherId,
                   fatherId,
                   marriageEvent.date AS marriageDate
                   :#{orderBy(#pageable)}
                   SKIP $skip
                   LIMIT $limit
            """,
            countQuery = """
                        MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_FAMILY]->(family:Family)
                        WHERE (toUpper(family.status) CONTAINS toUpper($status) OR $status = '')
                        OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person)
                        WHERE toLower(father.name) CONTAINS toLower($fatherName) OR $fatherName = ''
                        OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person)
                        WHERE toLower(mother.name) CONTAINS toLower($motherName) OR $motherName = ''
                        RETURN count(family)
                    """)
    Page<FamiliesResponse> find(String userId, String treeId, String fatherName, String motherName, String status, Pageable pageable);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_FAMILY]->(family:Family {id: $familyId})
            WITH family
                        
            OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person)
            WITH family, father, COALESCE(father.name, '') AS fatherName, father.id AS fatherId
                        
            OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person)
            WITH family, father, fatherName, fatherId, mother, COALESCE(mother.name, '') AS motherName, mother.id AS motherId
                        
            OPTIONAL MATCH (family)<-[:HAS_PARTICIPANT]-(marriageEvent:Event {type: 'MARRIAGE'})
            WITH family, father, fatherName, fatherId, mother, motherName, motherId, marriageEvent.date AS marriageDate
                        
            OPTIONAL MATCH (mother)<-[:HAS_PARTICIPANT]-(motherBirthEvent:Event {type: 'BIRTH'})
            OPTIONAL MATCH (mother)<-[:HAS_PARTICIPANT]-(motherChristeningEvent:Event {type: 'CHRISTENING'})
            OPTIONAL MATCH (mother)<-[:HAS_PARTICIPANT]-(motherDeathEvent:Event {type: 'DEATH'})
            OPTIONAL MATCH (mother)<-[:HAS_PARTICIPANT]-(motherBurialEvent:Event {type: 'BURIAL'})
            OPTIONAL MATCH (father)<-[:HAS_PARTICIPANT]-(fatherBirthEvent:Event {type: 'BIRTH'})
            OPTIONAL MATCH (father)<-[:HAS_PARTICIPANT]-(fatherChristeningEvent:Event {type: 'CHRISTENING'})
            OPTIONAL MATCH (father)<-[:HAS_PARTICIPANT]-(fatherDeathEvent:Event {type: 'DEATH'})
            OPTIONAL MATCH (father)<-[:HAS_PARTICIPANT]-(fatherBurialEvent:Event {type: 'BURIAL'})
                        
            RETURN family.id AS id,
                family.status AS status,
                fatherName,
                motherName,
                fatherId,
                motherId,
                COALESCE(fatherBirthEvent.date, fatherChristeningEvent.date) AS fatherBirthdate,
                COALESCE(fatherDeathEvent.date, fatherBurialEvent.date) AS fatherDeathdate,
                COALESCE(motherBirthEvent.date, motherChristeningEvent.date) AS motherBirthdate,
                COALESCE(motherDeathEvent.date, motherBurialEvent.date) AS motherDeathdate,
                marriageDate
            """)
    FamilyResponse find(String userId, String treeId, String familyId);

    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_FAMILY]->(family:Family {id: $familyId})
            MATCH (family)-[:HAS_CHILD]->(child:Person)
            
            OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person)
            OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person)
            OPTIONAL MATCH (father)-[rel1:PARENT_OF]->(child)
            OPTIONAL MATCH (mother)-[rel2:PARENT_OF]->(child)
            
            OPTIONAL MATCH (child)<-[:HAS_PARTICIPANT]-(birthEvent:Event {type: 'BIRTH'})
            OPTIONAL MATCH (child)<-[:HAS_PARTICIPANT]-(christeningEvent:Event {type: 'CHRISTENING'})
            OPTIONAL MATCH (child)<-[:HAS_PARTICIPANT]-(deathEvent:Event {type: 'DEATH'})
            OPTIONAL MATCH (child)<-[:HAS_PARTICIPANT]-(burialEvent:Event {type: 'BURIAL'})
                        
            RETURN child.id AS id,
                   child.name AS name,
                   child.firstname AS firstname,
                   child.lastname AS lastname,
                   child.gender AS gender,
                   COALESCE(rel1.relationship, null) AS fatherRelationship,
                   COALESCE(rel2.relationship, null) AS motherRelationship,
                   COALESCE(birthEvent.date, christeningEvent.date) AS birthdate,
                   COALESCE(deathEvent.date, burialEvent.date) AS deathdate
                   :#{orderBy(#pageable)}
                   SKIP $skip
                   LIMIT $limit
            """,
            countQuery = """
                        MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_FAMILY]->(family:Family {id: $familyId})
                        OPTIONAL MATCH (family)-[:HAS_CHILD]->(child:Person)
                        RETURN count(child)
                    """)
    Page<FamilyChildResponse> findChildren(String userId, String treeId, String familyId, Pageable pageable);

    @Query("MATCH (f:Family) RETURN f")
    List<Family> findAllFamilies();
}