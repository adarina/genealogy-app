package com.ada.genealogyapp.event.repository;

import com.ada.genealogyapp.event.dto.*;
import com.ada.genealogyapp.event.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends Neo4jRepository<Event, String> {

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
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
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
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
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
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
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
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
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
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
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
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
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
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
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
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_EVENT]->(event:Event {id: $eventId})
            OPTIONAL MATCH (event)-[:HAS_EVENT_CITATION]->(citation:Citation)
            WITH event, COLLECT({id: citation.id, page: citation.page, date: citation.date}) AS citations
                        
            OPTIONAL MATCH (event)-[rel:HAS_PARTICIPANT]->(participant:Participant)
            WITH event, citations, COLLECT({id: participant.id, name: participant.name, relationship: rel.relationship}) AS participants
                        
            RETURN event.id AS id,
                   event.type AS type,
                   event.date AS date,
                   event.place AS place,
                   event.description AS description,
                   citations,
                   participants
            """)
    EventResponse find(String userId, String treeId, String eventId);

    //TODO frontend add place uncomment
    @Query(value = """
                        MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                        OPTIONAL MATCH (tree)-[:HAS_EVENT]->(event:Event)
                        OPTIONAL MATCH (event)-[:HAS_PARTICIPANT]->(part:Participant)
                        WITH event, COLLECT(COALESCE(part.name, '')) AS parts
                        WITH event, REDUCE(s = '', part IN parts | s + CASE WHEN s = '' THEN part ELSE ', ' + part END) AS participantNames
                        
                        WHERE ($participants = "" OR toLower (participantNames) CONTAINS toLower($participants))
                            AND ($description = "" OR toLower(event.description) CONTAINS toLower($description))
            //              AND ($place = "" OR toLower(event.place) CONTAINS toLower($place))
                            AND ($type = "" OR event.type = $type)
                        WITH event, participantNames
                        WHERE event IS NOT NULL
                                      
                        RETURN event.id AS id,
                               event.description AS description,
                               event.date AS date,
                               event.type AS type,
                               event.place AS place,
                               participantNames
                               :#{orderBy(#pageable)}
                               SKIP $skip
                               LIMIT $limit
                               """,
            countQuery = """
                                MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                                OPTIONAL MATCH (tree)-[:HAS_EVENT]->(event:Event)
                                OPTIONAL MATCH (event)-[:HAS_PARTICIPANT]->(part:Participant)
                                WITH event, COLLECT(COALESCE(part.name, '')) AS parts
                                WITH event, REDUCE(s = '', part IN parts | s + CASE WHEN s = '' THEN part ELSE ', ' + part END) AS participantNames
                                WHERE ($participants = "" OR toLower (participantNames) CONTAINS toLower($participants))
                                    AND ($description = "" OR toLower(event.description) CONTAINS toLower($description))
                    //              AND ($place = "" OR toLower(event.place) CONTAINS toLower($place))
                                    AND ($type = "" OR event.type = $type)
                                RETURN count(event)
                                            """)
    Page<EventsResponse> find(String userId, String treeId, String description, String participants, String type, String place, Pageable pageable);

    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_EVENT]->(event:Event {id: $eventId})
            OPTIONAL MATCH (event)-[:HAS_EVENT_CITATION]->(citation:Citation)
            OPTIONAL MATCH (citation)-[:HAS_CITATION_SOURCE]->(source:Source)
            RETURN citation.id AS id,
                   citation.page AS page,
                   citation.date AS date,
                   source.name AS name
            :#{orderBy(#pageable)}
            SKIP $skip
            LIMIT $limit
            """,
            countQuery = """
                        MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_EVENT]->(event:Event {id: $eventId})
                        OPTIONAL MATCH (event)-[:HAS_EVENT_CITATION]->(citation:Citation)
                        RETURN count(citation)
                    """)
    Page<EventCitationResponse> findCitations(String userId, String treeId, String eventId, Pageable pageable);

    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_EVENT]->(event:Event {id: $eventId})
            OPTIONAL MATCH (event)-[rel:HAS_PARTICIPANT]->(participant:Participant)
                        
            RETURN participant.id AS id,
                   participant.name AS name,
                   rel.relationship AS relationship
            :#{orderBy(#pageable)}
            SKIP $skip
            LIMIT $limit
            """,
            countQuery = """
                        MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_EVENT]->(event:Event {id: $eventId})
                        OPTIONAL MATCH (event)-[rel:HAS_PARTICIPANT]->(participant:Participant)
                        RETURN count(participant)
                    """)
    Page<EventParticipantResponse> findParticipants(String userId, String treeId, String eventId, Pageable pageable);
}