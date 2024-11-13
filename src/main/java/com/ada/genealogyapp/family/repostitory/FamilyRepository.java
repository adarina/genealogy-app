package com.ada.genealogyapp.family.repostitory;

import com.ada.genealogyapp.family.dto.FamiliesResponse;
import com.ada.genealogyapp.family.dto.FamilyChildrenResponse;
import com.ada.genealogyapp.family.dto.FamilyEventResponse;
import com.ada.genealogyapp.family.dto.FamilyEventsResponse;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.type.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FamilyRepository extends Neo4jRepository<Family, UUID> {

    List<Family> findAllByTree_Id(UUID treeId);

    @Query(value = """
            MATCH (t:Tree)-[:HAS_FAMILY]->(f:Family)
            WHERE t.id = $treeId
            AND ($status = '' OR f.status = toUpper($status))
            OPTIONAL MATCH (f)-[:HAS_MOTHER]->(p1:Person)
            WITH f, p1.name AS motherName
            WHERE toLower(motherName) CONTAINS toLower($motherName)
            OPTIONAL MATCH (f)-[:HAS_FATHER]->(p2:Person)
            WITH f, motherName, p2.name AS fatherName
            WHERE toLower(fatherName) CONTAINS toLower($fatherName)
            RETURN f.id AS id,
                   f.status AS status,
                   motherName,
                   fatherName
            :#{orderBy(#pageable)}
            SKIP $skip
            LIMIT $limit
            """,
            countQuery = """
                    MATCH (t:Tree)-[:HAS_FAMILY]->(f:Family)
                    WHERE t.id = $treeId
                    RETURN count(f)
                    """)
    Page<FamiliesResponse> findByTreeIdAndFilteredParentNamesAndStatus(@Param("treeId") UUID treeId, String motherName, String fatherName, String status, Pageable pageable);

    @Query("""
                MATCH (e:Event {id: $eventId})
                MATCH (p1:Participant {id: $familyId})
                MERGE (e)-[r1:HAS_PARTICIPANT]->(p1)
                WITH e, p1, r1.relationship AS relationship
                OPTIONAL MATCH (e)-[r2:HAS_PARTICIPANT]->(p2:Participant)
                WITH e, relationship, COLLECT({
                    id: COALESCE(p2.id, p1.id),
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
    Optional<FamilyEventResponse> findFamilyEvent(@Param("eventId") UUID eventId, @Param("familyId") UUID familyId);

    @Query(value = """
            MATCH (e:Event)-[r1:HAS_PARTICIPANT]->(p1:Participant)
            WHERE p1.id = $familyId
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
                        name: COALESCE(p2.name, p1.name)
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
                        WHERE p1.id = $familyId
                        RETURN count(e)
                    """)
    Page<FamilyEventsResponse> findFamilyEvents(@Param("familyId") UUID familyId, Pageable pageable);

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
    Page<FamilyChildrenResponse> findChildren(@Param("familyId") UUID familyId, Pageable pageable);
}
