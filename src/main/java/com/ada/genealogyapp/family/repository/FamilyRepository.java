package com.ada.genealogyapp.family.repository;

import com.ada.genealogyapp.family.dto.*;
import com.ada.genealogyapp.family.model.Family;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
                RETURN userExist, treeExist, tree, count(family) > 0 AS familyExist, family
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND familyExist, '
                        RETURN "FAMILY_FOUND" AS message, family
                    ',
                    userExist AND treeExist, 'RETURN "FAMILY_NOT_EXIST" AS message',
                    treeExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {family: family}
            ) YIELD value
            RETURN value.message AS message, value.family AS family
            """)
    FamilyFindResponse findByTreeIdAndId(String userId, String treeId, String familyId);

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
    String removeMother(String userId, String treeId, String familyId, String fatherId);

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
            MATCH (t:Tree)-[:HAS_FAMILY]->(f:Family)
            WHERE t.id = $treeId
            AND ($status = '' OR f.status = toUpper($status))
            OPTIONAL MATCH (f)-[:HAS_MOTHER]->(p1:Person)
            WITH f, COALESCE(p1.name, '') AS motherName, p1.birthdate AS motherBirthdate, p1.id AS motherId
            WHERE toLower(motherName) CONTAINS toLower($motherName)
            OPTIONAL MATCH (f)-[:HAS_FATHER]->(p2:Person)
            WITH f, motherBirthdate, motherName, motherId, COALESCE(p2.name, '') AS fatherName, p2.birthdate AS fatherBirthdate, p2.id AS fatherId
            WHERE toLower(fatherName) CONTAINS toLower($fatherName)
            RETURN f.id AS id,
                   f.status AS status,
                   motherName,
                   fatherName,
                   motherId,
                   fatherId,
                   motherBirthdate,
                   fatherBirthdate
            :#{orderBy(#pageable)}
            SKIP $skip
            LIMIT $limit
            """,
            countQuery = """
                    MATCH (t:Tree)-[:HAS_FAMILY]->(f:Family)
                    WHERE t.id = $treeId
                    RETURN count(f)
                    """)
    Page<FamilyResponse> findByTreeIdAndFilteredParentNamesAndStatus(@Param("treeId") String treeId, String motherName, String fatherName, String status, Pageable pageable);

    @Query(value = """
            MATCH (f:Family)-[:HAS_CHILD]->(child:Person)
            WHERE f.id = $familyId
            WITH f, child
            OPTIONAL MATCH (f)-[:HAS_FATHER]->(father:Person)
            OPTIONAL MATCH (f)-[:HAS_MOTHER]->(mother:Person)
            OPTIONAL MATCH (father)-[r1:PARENT_OF]->(child)
            OPTIONAL MATCH (mother)-[r2:PARENT_OF]->(child)
            WITH child, father, mother, r1, r2
            RETURN child.id AS id,
                   child.name AS name,
                   child.birthdate AS birthdate,
                   child.gender AS gender,
                   CASE
                       WHEN r1.relationship IS NOT NULL THEN r1.relationship
                       ELSE null
                   END AS fatherRelationship,
                   CASE
                       WHEN r2.relationship IS NOT NULL THEN r2.relationship
                       ELSE null
                   END AS motherRelationship
                   :#{orderBy(#pageable)}
                   SKIP $skip
                   LIMIT $limit
            """,
            countQuery = """
                        MATCH (f:Family)-[:HAS_CHILD]->(child:Person)
                        WHERE f.id = $familyId
                        RETURN count(child)
                    """)
    Page<FamilyChildResponse> findChildren(@Param("familyId") String familyId, Pageable pageable);

    @Query("""
                MATCH (f:Family {id: $familyId})
                MATCH (t:Tree {id: $treeId})
                MERGE (t)-[:HAS_FAMILY]->(f)
                WITH f
                OPTIONAL MATCH (f)-[:HAS_FATHER]->(father:Person)
                OPTIONAL MATCH (f)-[:HAS_MOTHER]->(mother:Person)
                WITH f, father, mother
                RETURN f.id AS id,
                       f.status AS status,
                       father.id AS fatherId,
                       mother.id AS motherId,
                       father.name AS fatherName,
                       mother.name AS motherName,
                       father.birthdate AS fatherBirthdate,
                       mother.birthdate AS motherBirthdate
            """)
    Optional<FamilyResponse> findByTreeIdAndFamilyId(@Param("treeId") String treeId, @Param("familyId") String familyId);
}