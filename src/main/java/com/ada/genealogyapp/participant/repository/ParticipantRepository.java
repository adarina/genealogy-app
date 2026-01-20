package com.ada.genealogyapp.participant.repository;

import com.ada.genealogyapp.participant.dto.ParticipantEventGedcomResponse;
import com.ada.genealogyapp.participant.dto.ParticipantEventResponse;
import com.ada.genealogyapp.participant.model.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ParticipantRepository extends Neo4jRepository<Participant, String> {

    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
            OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person {id: $participantId})
            OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $participantId})
            WITH user, tree, COALESCE(person, family) AS participant
            
            MATCH (event:Event)-[rel:HAS_PARTICIPANT]->(participant)
            
            OPTIONAL MATCH (event)-[:HAS_EVENT_LOCATION]->(location:Location)
            OPTIONAL MATCH path = (location)-[:LOCATED_IN*]->(parentLocation:Location)
            
            WITH event, participant, rel.relationship AS relationship,
                 COLLECT(DISTINCT COALESCE(location.name, '')) + collect(DISTINCT COALESCE(parentLocation.name, '')) AS locationNames
            
            WITH event, participant, relationship,
                 REDUCE(acc = '', name IN locationNames | acc + (CASE WHEN acc = '' THEN '' ELSE ', ' END) + toString(COALESCE(name, ''))) AS placeNames
            
            OPTIONAL MATCH (event)-[otherRel:HAS_PARTICIPANT]->(others:Participant)
            WITH event, relationship, placeNames, COLLECT(DISTINCT {
                name: COALESCE(others.name, participant.name),
                id: COALESCE(others.id, participant.id),
                relationship: COALESCE(otherRel.relationship, relationship)
            }) AS participants
            OPTIONAL MATCH (event)-[:HAS_EVENT_CITATION]->(citation:Citation)
            
            RETURN event.id AS id,
                   event.type AS type,
                   event.date AS date,
                   placeNames AS place,
                   event.description AS description,
                   relationship,
                   participants,
                   COLLECT({
                       id: citation.id,
                       page: citation.page,
                       date: citation.date
                   }) AS citations
                   :#{orderBy(#pageable)}
            SKIP $skip
            LIMIT $limit
            """,
            countQuery = """
                    MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
                    OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person {id: $participantId})
                    OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $participantId})
                    WITH user, tree, COALESCE(person, family) AS participant
                    
                    MATCH (event:Event)-[rel:HAS_PARTICIPANT]->(participant)
                    RETURN count(event)
                    """)
    Page<ParticipantEventResponse> findParticipantEvents(String userId, String treeId, String participantId, Pageable pageable);

    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
            OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person {id: $participantId})
            OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $participantId})
            WITH user, tree, COALESCE(person, family) AS participant
            MATCH (event:Event)-[:HAS_PARTICIPANT]->(participant)
            OPTIONAL MATCH (event)-[:HAS_EVENT_CITATION]->(citation:Citation)
            
            RETURN event.id AS id,
                    event.type AS type,
                    event.date AS date,
                    event.place AS place,
                    event.description AS description,
                    COLLECT({
                        id: citation.id,
                        page: citation.page,
                        date: citation.date
                    }) AS citations
            """)
    List<ParticipantEventGedcomResponse> findParticipantEvents(String userId, String treeId, String participantId);

    @Query(value = """
         MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
        
        OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person {id: $participantId})
        OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $participantId})
        WITH tree, COALESCE(person, family) AS participant
        
        WHERE participant IS NOT NULL
        
        MATCH (event:Event {id: $eventId})-[rel:HAS_PARTICIPANT]->(participant)
        MATCH (tree)-[:HAS_EVENT]->(event)
        
        OPTIONAL MATCH (event)-[otherRel:HAS_PARTICIPANT]->(otherParticipant)
        OPTIONAL MATCH (event)-[:HAS_EVENT_CITATION]->(citation:Citation)
        
       
        OPTIONAL MATCH (event)-[:HAS_EVENT_LOCATION]->(location:Location)
        OPTIONAL MATCH path = (location)-[:LOCATED_IN*]->(parentLocation:Location)
        
        WITH event,
             rel.relationship AS relationship,
             COLLECT(DISTINCT {
                 id: otherParticipant.id,
                 name: otherParticipant.name,
                 relationship: otherRel.relationship
             }) AS participants,
             COLLECT(DISTINCT {
                 id: citation.id,
                 page: citation.page,
                 date: citation.date
             }) AS citations,

             COLLECT(DISTINCT COALESCE(location.name, '')) + collect(DISTINCT COALESCE(parentLocation.name, '')) AS locationNames


        WITH event,
             relationship,
             participants,
             citations,
             REDUCE(acc = '', name IN locationNames | acc + (CASE WHEN acc = '' THEN '' ELSE ', ' END) + toString(COALESCE(name, ''))) AS placeNames
 
        
        RETURN event.id AS id,
               event.type AS type,
               event.date AS date,
               placeNames AS place,
               event.description AS description,
               relationship,
               participants,
               citations
        """)
    ParticipantEventResponse findParticipantEvent(String userId, String treeId, String participantId, String eventId);
}
