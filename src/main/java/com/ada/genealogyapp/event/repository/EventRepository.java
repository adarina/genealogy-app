package com.ada.genealogyapp.event.repository;

import com.ada.genealogyapp.citation.dto.CitationSourceResponse;
import com.ada.genealogyapp.event.dto.*;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.location.dto.LocationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.Set;

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
                        
                        WITH tree, event, locationId
                        OPTIONAL MATCH (tree)-[:HAS_LOCATION]->(location:Location {id: locationId})
                        WITH tree, event, locationId
                        OPTIONAL MATCH (tree)-[:HAS_LOCATION]->(location:Location {id: locationId})
                        FOREACH (_ IN CASE WHEN location IS NOT NULL THEN [1] ELSE [] END |
                        MERGE (event)-[:HAS_EVENT_LOCATION]->(location)
                        )
                            
                        RETURN "EVENT_CREATED" AS message
                    ',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, eventId: $eventId, description: $description, place: $place, type: $type, date: $date, locationId: $locationId}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String save(String userId, String treeId, String eventId, String description, String place, String type, String date, String locationId);

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
            OPTIONAL MATCH (event)-[:HAS_EVENT_LOCATION]->(location:Location)
            OPTIONAL MATCH (location)-[:LOCATED_IN*]->(parentLocation:Location)
            WITH event, location,
                 COLLECT(DISTINCT toString(COALESCE(location.name, ''))) AS primaryLocationNames,
                 COLLECT(DISTINCT toString(COALESCE(parentLocation.name, ''))) AS parentLocationNames
            WITH event, location,
                 [name IN (primaryLocationNames + parentLocationNames) WHERE name <> '' | name] AS allValidLocationNames
            RETURN event.id AS id,
                   event.type AS type,
                   event.date AS date,
                   apoc.text.join(allValidLocationNames, ', ') AS name,
                   location.id AS locationId,
                   event.description AS description""")
    EventLocationResponse find(String userId, String treeId, String eventId);

    //TODO frontend add place uncomment
    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
            OPTIONAL MATCH (tree)-[:HAS_EVENT]->(event:Event)
            OPTIONAL MATCH (event)-[:HAS_EVENT_LOCATION]->(location:Location)
            OPTIONAL MATCH path = (location)-[:LOCATED_IN*]->(parentLocation:Location)
            WITH event, COLLECT(DISTINCT COALESCE(location.name, '')) + collect(DISTINCT COALESCE(parentLocation.name, '')) AS locationNames, event AS newEvent
            OPTIONAL MATCH (newEvent)-[:HAS_PARTICIPANT]->(part:Participant)
            WITH newEvent, locationNames, COLLECT(COALESCE(part.name, '')) AS parts
            WITH newEvent, parts, locationNames, REDUCE(acc = '', name IN locationNames | acc + (CASE WHEN acc = '' THEN '' ELSE ', ' END) + toString(COALESCE(name, ''))) AS placeNames
            WITH newEvent, parts, placeNames, REDUCE(acc = '', part IN parts | acc + (CASE WHEN acc = '' THEN '' ELSE ', ' END) + COALESCE(part, '')) AS participantNames
            WHERE ($participants = "" OR toLower(participantNames) CONTAINS toLower($participants))
                AND ($description = "" OR toLower(newEvent.description) CONTAINS toLower($description))
                AND ($type = "" OR newEvent.type = $type)
            WITH newEvent, participantNames, placeNames
            WHERE newEvent IS NOT NULL
            RETURN newEvent.id AS id,
                   newEvent.description AS description,
                   newEvent.date AS date,
                   newEvent.type AS type,
                   placeNames AS place,
                   participantNames
                   :#{orderBy(#pageable)}
                   SKIP $skip
                   LIMIT $limit""",
            countQuery = """
                    MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                    OPTIONAL MATCH (tree)-[:HAS_EVENT]->(event:Event)
                    OPTIONAL MATCH (event)-[:HAS_EVENT_LOCATION]->(location:Location)
                    OPTIONAL MATCH path = (location)-[:LOCATED_IN*]->(parentLocation:Location)
                    WITH event, COLLECT(DISTINCT COALESCE(location.name, '')) + collect(DISTINCT COALESCE(parentLocation.name, '')) AS locationNames, event AS newEvent
                    OPTIONAL MATCH (newEvent)-[:HAS_PARTICIPANT]->(part:Participant)
                    WITH newEvent, locationNames, COLLECT(COALESCE(part.name, '')) AS parts
                    WITH newEvent, locationNames, REDUCE(acc = '', part IN parts | acc + (CASE WHEN acc = '' THEN '' ELSE ', ' END) + COALESCE(part, '')) AS participantNames
                    WHERE ($participants = "" OR toLower(participantNames) CONTAINS toLower($participants))
                        AND ($description = "" OR toLower(newEvent.description) CONTAINS toLower($description))
                        AND ($type = "" OR newEvent.type = $type)
                    RETURN count(newEvent)
                    """)
    Page<EventsResponse> find(String userId, String treeId, String description, String participants, String type, String place, Pageable pageable);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_EVENT]->(event:Event {id: $eventId})-[:HAS_EVENT_LOCATION]->(location:Location)
            MATCH (location)-[:LOCATED_IN*0..]->(ancestor:Location)
            RETURN ancestor.id AS id,
                   ancestor.name AS name
                   ancestor.type AS type
                   """)
    LinkedHashSet<LocationResponse> findLocation(String userId, String treeId, String eventId);

    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_EVENT]->(event:Event {id: $eventId})-[:HAS_EVENT_CITATION]->(citation:Citation)
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
                    MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_EVENT]->(event:Event {id: $eventId})-[:HAS_EVENT_CITATION]->(citation:Citation)
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


    //TODO
    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_EVENT]->(event:Event {id: $eventId})
                WITH userExist, treeExist, tree, count(event) > 0 AS eventExist, event
                
                OPTIONAL MATCH (event)-[locationRel:HAS_EVENT_LOCATION]->(location:Location {id: $locationId})
                RETURN userExist, treeExist, tree, eventExist, event, count(location) > 0 AS locationExist, location, locationRel
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND eventExist AND locationExist, '
                        DELETE locationRel
                        RETURN "LOCATION_REMOVED_FROM_EVENT" AS message
                    ',
                    userExist AND treeExist AND eventExist, 'RETURN "LOCATION_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "EVENT_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {event: event, location: location, locationRel: locationRel}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String removeLocation(String userId, String treeId, String eventId, String locationId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_EVENT]->(event:Event {id: $eventId})
                WITH userExist, treeExist, tree, count(event) > 0 AS eventExist, event
                
                OPTIONAL MATCH (tree)-[:HAS_LOCATION]->(location:Location {id: $locationId})
                RETURN userExist, treeExist, tree, eventExist, event, count(location) > 0 AS locationExist, location
            }
                            
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND eventExist AND locationExist, '
                        OPTIONAL MATCH (event)-[r:HAS_EVENT_LOCATION]->()
                        DELETE r
                        WITH event, location
                        MERGE (event)-[:HAS_EVENT_LOCATION]->(location)
                            
                        RETURN "LOCATION_ADDED_TO_EVENT" AS message
                    ',
                    userExist AND treeExist AND eventExist, 'RETURN "LOCATION_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "EVENT_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {event: event, location: location}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String addLocation(String userId, String treeId, String eventId, String locationId);


    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_EVENT]->(event:Event)
            OPTIONAL MATCH (event)-[:HAS_EVENT_LOCATION]->(location:Location)
            RETURN event.id AS id,
                   event.type AS type,
                   event.date AS date,
                   event.description AS description,
                   location.id AS locationId
                   """)
    Set<EventExportResponse> find(String userId, String treeId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_EVENT]->(event:Event {id: $eventId})
            MATCH (event)-[r:HAS_PARTICIPANT]->(participant:Participant)
            RETURN participant.id AS participantId,
                   r.relationship AS relationship
            """)
    Set<EventParticipantExportResponse> findParticipants(String userId, String treeId, String eventId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_EVENT]->(event:Event {id: $eventId})
            MATCH (event)-[r:HAS_EVENT_CITATION]->(citation:Citation)
            RETURN citation.id AS citationId
            """)
    Set<EventCitationExportResponse> findCitations(String userId, String treeId, String eventId);

}
