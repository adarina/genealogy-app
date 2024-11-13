package com.ada.genealogyapp.event.repository;

import com.ada.genealogyapp.event.dto.EventCitationResponse;
import com.ada.genealogyapp.event.dto.EventCitationsResponse;
import com.ada.genealogyapp.event.dto.EventsResponse;
import com.ada.genealogyapp.event.dto.EventResponse;
import com.ada.genealogyapp.event.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends Neo4jRepository<Event, UUID> {

    @Query("MATCH (e:Event)<-[:HAS_EVENT]-(t:Tree {id: $treeId}) RETURN e")
    List<Event> findAllByTreeId(UUID treeId);

    @Query("""
                MATCH (e:Event {id: $eventId})
                MATCH (t:Tree {id: $treeId})
                MERGE (t)-[:HAS_EVENT]->(e)
                WITH e
                OPTIONAL MATCH (e)-[:HAS_EVENT_CITATION]->(c:Citation)
                WITH e, COLLECT({
                    id: c.id,
                    page: c.page
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
    Page<EventsResponse> findByTreeIdAndFilteredDescriptionParticipantNamesAndType(@Param("treeId") UUID treeId, String description, String participants, String type, Pageable pageable);

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
                    """
    )
    Page<EventCitationsResponse> findEventCitations(UUID eventId, Pageable pageable);

    @Query(value = """
            MATCH (e:Event {id: $eventId})
            MATCH (c:Citation {id: $citationId})
            MATCH (c)<-[r1:HAS_EVENT_CITATION]-(e)
            RETURN c.id AS id,
                   c.page AS page,
                   c.date AS date
            """)
    Optional<EventCitationResponse> findEventCitation(UUID eventId, UUID citationId);
}