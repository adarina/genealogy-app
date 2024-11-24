package com.ada.genealogyapp.event.repository;

import com.ada.genealogyapp.event.dto.*;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends Neo4jRepository<Event, UUID> {


    @Query("""
                MATCH (e:Event {id: $eventId})
                MATCH (t:Tree {id: $treeId})
                MERGE (t)-[:HAS_EVENT]->(e)
                WITH e
                OPTIONAL MATCH (e)-[:HAS_EVENT_CITATION]->(c:Citation)
                WITH e, COLLECT({
                    id: c.id,
                    page: c.page,
                    date: c.date
                }) AS citations
                OPTIONAL MATCH (e)-[r2:HAS_PARTICIPANT]->(p:Participant)
                WITH e, citations, COLLECT({
                    id: p.id,
                    name: p.name,
                    relationship: r2.relationship
                }) AS participants
                RETURN e.id AS id,
                       e.type AS type,
                       e.date AS date,
                       e.place AS place,
                       e.description AS description,
                       citations,
                       participants
            """)
    Optional<EventResponse> findByTreeIdAndEventId(@Param("treeId") UUID treeId, @Param("eventId") UUID eventId);


    @Query(value = """
            MATCH (t:Tree)-[:HAS_EVENT]->(e:Event)
            WHERE t.id = $treeId
            AND (toLower(e.description) CONTAINS toLower($description) OR $description = '')
            AND ($type = '' OR e.type = toUpper($type))
            OPTIONAL MATCH (e)-[:HAS_PARTICIPANT]->(p:Participant)
            WITH e, COLLECT(COALESCE(p.name, '')) AS participants
            WITH e, REDUCE(s = '', participant IN participants | s + CASE WHEN s = '' THEN participant ELSE ', ' + participant END) AS participantNames
            WHERE toLower(participantNames) CONTAINS toLower($participants) OR $participants = ''
            RETURN e.id AS id,
                   e.description AS description,
                   e.date AS date,
                   e.type AS type,
                   e.place AS place,
                   participantNames
                   :#{orderBy(#pageable)}
                   SKIP $skip
                   LIMIT $limit
                   """,
            countQuery = """
                    MATCH (t:Tree)-[:HAS_EVENT]->(e:Event)
                    WHERE t.id = $treeId
                    RETURN count(e)
                    """)
    Page<EventPageResponse> findByTreeIdAndFilteredDescriptionParticipantNamesAndType(@Param("treeId") UUID treeId, String description, String participants, String type, Pageable pageable);

    @Query(value = """
            MATCH (c:Citation)<-[r1:HAS_EVENT_CITATION]-(e1:Event)
            WHERE e1.id = $eventId
            WITH c
            RETURN c.id AS id,
                   c.page AS page,
                   c.date AS date
            :#{orderBy(#pageable)}
            SKIP $skip
            LIMIT $limit
            """,
            countQuery = """
                        MATCH (c:Citation)-[r1:HAS_EVENT_CITATION]->(e1:Event)
                        WHERE e1.id = $eventId
                        RETURN count(c)
                    """)
    Page<EventCitationResponse> findEventCitations(UUID eventId, Pageable pageable);

    @Query(value = """
            MATCH (e:Event {id: $eventId})
            MATCH (c:Citation {id: $citationId})
            MATCH (c)<-[r1:HAS_EVENT_CITATION]-(e)
            RETURN c.id AS id,
                   c.page AS page,
                   c.date AS date
            """)
    Optional<EventCitationResponse> findEventCitation(UUID eventId, UUID citationId);


    @Query("""
                MATCH (e:Event {id: $eventId})
                SET e.type = COALESCE($type, e.type),
                    e.date = COALESCE($date, e.date),
                    e.place = COALESCE($place, e.place),
                    e.description = COALESCE($description, e.description)
                RETURN e
            """)
    void updateEvent(UUID eventId, EventType type, LocalDate date, String place, String description);

    @Query(value = """
            MATCH (p:Participant)<-[r:HAS_PARTICIPANT]-(e:Event)
            WHERE e.id = $eventId
            WITH p, r
            RETURN p.id AS id,
                   p.name AS name,
                   r.relationship AS relationship
            :#{orderBy(#pageable)}
            SKIP $skip
            LIMIT $limit
            """,
            countQuery = """
                        MATCH (p:Participant)-[r1:HAS_PARTICIPANT]->(e:Event)
                        WHERE e.id = $eventId
                        RETURN count(p)
                    """)
    Page<EventParticipantResponse> findEventParticipants(UUID eventId, Pageable pageable);
}