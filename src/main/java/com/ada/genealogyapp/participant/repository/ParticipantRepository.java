package com.ada.genealogyapp.participant.repository;

import com.ada.genealogyapp.participant.dto.ParticipantEventResponse;
import com.ada.genealogyapp.participant.model.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParticipantRepository extends Neo4jRepository<Participant, String> {

    @Query(value = """
            MATCH (e:Event)-[r1:HAS_PARTICIPANT]->(p1:Participant)
            WHERE p1.id = $participantId
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
                        name: COALESCE(p2.name, p1.name),
                        id: COALESCE(p2.id, p1.id),
                        relationship: COALESCE(r2.relationship, relationship)
                    }) AS participants,
                    COLLECT({
                        id: c.id,
                        page: c.page,
                        date: c.date
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
    Page<ParticipantEventResponse> findParticipantEvents(@Param("participantId") String participantId, Pageable pageable);

    @Query("""
                MATCH (e:Event {id: $eventId})
                MATCH (p1:Participant {id: $participantId})
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
                    page: c.page,
                    date: c.date
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
    Optional<ParticipantEventResponse> findParticipantEvent(@Param("eventId") UUID eventId, @Param("participantId") String participantId);
}
