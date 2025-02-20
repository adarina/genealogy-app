package com.ada.genealogyapp.event.repository;

import com.ada.genealogyapp.event.dto.*;
import com.ada.genealogyapp.event.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EventRepository extends Neo4jRepository<Event, String> {

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                RETURN count(user) > 0 AS userExist, count(tree) > 0 AS treeExist, tree
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist, '
                        MERGE (tree)-[:HAS_EVENT]->(event:Event {id: eventId})
                        SET event.description = description,
                            event.place = place,
                            event.type = type,
                            event.date = date
                            
                        RETURN "EVENT_CREATED" AS message
                    ',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, eventId: $eventId, description: $description, place: $place, type: $type, date: $date}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String save(String userId, String treeId, String eventId, String description, String place, String type, String date);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH count(user) > 0 AS userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_FAMILY|HAS_PERSON]->(participant:Participant {id: $participantId})
                RETURN userExist, treeExist, tree, count(participant) > 0 AS participantExist, participant
            }
                            
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND participantExist, '
                        MERGE (tree)-[:HAS_EVENT]->(event:Event {id: $eventId})
                        SET event.description = $description,
                            event.place = $place,
                            event.type = $type,
                            event.date = $date
                
                        MERGE (event)-[:HAS_PARTICIPANT {relationship: $relationshipType}]->(participant)
                            
                        RETURN "EVENT_CREATED" AS message
                    ',
                    userExist AND treeExist, '
                        RETURN "PARTICIPANT_NOT_EXIST" AS message
                    ',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, eventId: $eventId, description: $description, place: $place, type: $type, date: $date, participant: participant, relationshipType: $relationshipType}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String save(String userId, String treeId, String eventId, String description, String place, String type, String date, String participantId, String relationshipType);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH count(user) > 0 AS userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_EVENT]->(event:Event {id: $eventId})
                RETURN userExist, treeExist, tree, count(event) > 0 AS eventExist, event
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND eventExist, '
                        OPTIONAL MATCH (event)-[rel]-()
                        DELETE rel, event
                        RETURN "EVENT_DELETED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "EVENT_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, event: event}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String delete(String userId, String treeId, String eventId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH count(user) > 0 AS userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_EVENT]->(event:Event {id: $eventId})
                RETURN userExist, treeExist, tree, count(event) > 0 AS eventExist, event
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND eventExist, '
                        SET event.description = $description,
                            event.place = $place,
                            event.date = $date,
                            event.type = $type
                            
                        RETURN "EVENT_UPDATED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "EVENT_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {event: event, description: $description, place: $place, date: $date, type: $type}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String update(String userId, String treeId, String eventId, String description, String place, String date, String type);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH count(user) > 0 AS userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_EVENT]->(event:Event {id: $eventId})
                WITH userExist, treeExist, tree, count(event) > 0 AS eventExist, event
                
                OPTIONAL MATCH (tree)-[:HAS_FAMILY|HAS_PERSON]->(participant:Participant {id: $participantId})
                RETURN userExist, treeExist, tree, eventExist, event, count(participant) > 0 AS participantExist, participant
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND eventExist AND participantExist, '
                        SET event.description = $description,
                            event.place = $place,
                            event.date = $date,
                            event.type = $type
                     
                        MERGE (event)-[rel:HAS_PARTICIPANT]->(participant)
                        ON CREATE SET rel.relationship = $relationshipType
                        ON MATCH SET rel.relationship = $relationshipType
                   
                        RETURN "EVENT_UPDATED" AS message
                    ',
                    userExist AND treeExist AND eventExist, 'RETURN "PARTICIPANT_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "EVENT_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {event: event, description: $description, place: $place, date: $date, type: $type, participant: participant, relationshipType: $relationshipType}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String update(String userId, String treeId, String eventId, String description, String place, String date, String type, String participantId, String relationshipType);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH count(user) > 0 AS userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_EVENT]->(event:Event {id: $eventId})
                WITH userExist, treeExist, tree, count(event) > 0 AS eventExist, event
                
                OPTIONAL MATCH (tree)-[:HAS_FAMILY|HAS_PERSON]->(participant:Participant {id: $participantId})
                RETURN userExist, treeExist, tree, eventExist, event, count(participant) > 0 AS participantExist, participant
            }
                            
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND eventExist AND participantExist, '
                        MERGE (event)-[:HAS_PARTICIPANT {relationship: relationshipType}]->(participant)
                            
                        RETURN "PARTICIPANT_ADDED_TO_EVENT" AS message
                    ',
                    userExist AND treeExist AND eventExist, 'RETURN "PARTICIPANT_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "EVENT_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {event: event, participant: participant, relationshipType: $relationshipType}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String addParticipant(String userId, String treeId, String eventId, String participantId, String relationshipType);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH count(user) > 0 AS userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_EVENT]->(event:Event {id: $eventId})
                WITH userExist, treeExist, tree, count(event) > 0 AS eventExist, event
                
                OPTIONAL MATCH (event)-[participantRel:HAS_PARTICIPANT]->(participant:Participant {id: $participantId})
                RETURN userExist, treeExist, tree, eventExist, event, count(participant) > 0 AS participantExist, participant, participantRel
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND eventExist AND participantExist, '
                        DELETE participantRel
                        RETURN "PARTICIPANT_REMOVED_FROM_EVENT" AS message
                    ',
                    userExist AND treeExist AND eventExist, 'RETURN "PARTICIPANT_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "EVENT_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {participantRel: participantRel}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String removeParticipant(String userId, String treeId, String eventId, String participantId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH count(user) > 0 AS userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_EVENT]->(event:Event {id: $eventId})
                WITH userExist, treeExist, tree, count(event) > 0 AS eventExist, event
                
                OPTIONAL MATCH (event)-[citationRel:HAS_EVENT_CITATION]->(citation:Citation {id: $citationId})
                RETURN userExist, treeExist, tree, eventExist, event, count(citation) > 0 AS citationExist, citation, citationRel
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND eventExist AND citationExist, '
                        DELETE citationRel
                        RETURN "CITATION_REMOVED_FROM_EVENT" AS message
                    ',
                    userExist AND treeExist AND eventExist, 'RETURN "CITATION_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "EVENT_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {citationRel: citationRel}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String removeCitation(String userId, String treeId, String eventId, String citationId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH count(user) > 0 AS userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_EVENT]->(event:Event {id: $eventId})
                WITH userExist, treeExist, tree, count(event) > 0 AS eventExist, event
                
                OPTIONAL MATCH (tree)-[:HAS_CITATION]->(citation:Citation {id: $citationId})
                RETURN userExist, treeExist, tree, eventExist, event, count(citation) > 0 AS citationExist, citation
            }
                            
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND eventExist AND citationExist, '
                        MERGE (event)-[:HAS_EVENT_CITATION]->(citation)
                            
                        RETURN "CITATION_ADDED_TO_EVENT" AS message
                    ',
                    userExist AND treeExist AND eventExist, 'RETURN "CITATION_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "EVENT_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {event: event, citation: citation}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String addCitation(String userId, String treeId, String eventId, String citationId);

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
    Optional<EventResponse> findByTreeIdAndEventId(@Param("treeId") String treeId, @Param("eventId") String eventId);


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
    Page<EventPageResponse> findByTreeIdAndFilteredDescriptionParticipantNamesAndType(@Param("treeId") String treeId, String description, String participants, String type, Pageable pageable);

    @Query(value = """
            MATCH (t:Tree)-[:HAS_EVENT]->(e:Event)-[:HAS_EVENT_CITATION]->(c:Citation)
            WHERE t.id = $treeId AND e.id = $eventId
            WITH c
            RETURN c.id AS id,
                   c.page AS page,
                   c.date AS date
            :#{orderBy(#pageable)}
            SKIP $skip
            LIMIT $limit
            """,
            countQuery = """
                        MATCH (t:Tree)-[:HAS_EVENT]->(e:Event)-[:HAS_EVENT_CITATION]->(c:Citation)
                        WHERE t.id = $treeId AND e.id = $eventId
                        RETURN count(c)
                    """)
    Page<EventCitationResponse> findEventCitations(String treeId, String eventId, Pageable pageable);

    @Query(value = """
            MATCH (t:Tree)-[:HAS_EVENT]->(e:Event)-[:HAS_EVENT_CITATION]->(c:Citation)
            WHERE t.id = $treeId AND e.id = $eventId AND c.id = $citationId
            RETURN c.id AS id,
                   c.page AS page,
                   c.date AS date
            """)
    Optional<EventCitationResponse> findEventCitation(String treeId, String eventId, String citationId);


    @Query(value = """
            MATCH (t:Tree)-[:HAS_EVENT]->(e:Event)-[r:HAS_PARTICIPANT]->(p:Participant)
            WHERE t.id = $treeId AND e.id = $eventId
            WITH p, e, r
            RETURN p.id AS id,
                   p.name AS name,
                   r.relationship AS relationship
            :#{orderBy(#pageable)}
            SKIP $skip
            LIMIT $limit
            """,
            countQuery = """
                        MATCH (t:Tree)-[:HAS_EVENT]->(e:Event)-[:HAS_PARTICIPANT]->(p:Participant)
                        WHERE t.id = $treeId AND e.id = $eventId
                        RETURN count(p)
                    """)
    Page<EventParticipantResponse> findEventParticipants(String treeId, String eventId, Pageable pageable);
}