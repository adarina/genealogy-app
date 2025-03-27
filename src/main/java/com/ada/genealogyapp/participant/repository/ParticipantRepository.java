package com.ada.genealogyapp.participant.repository;

import com.ada.genealogyapp.participant.dto.ParticipantEventResponse;
import com.ada.genealogyapp.participant.model.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ParticipantRepository extends Neo4jRepository<Participant, String> {

    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})
            OPTIONAL MATCH (tree)-[:HAS_PERSON]->(person:Person {id: $participantId})
            OPTIONAL MATCH (tree)-[:HAS_FAMILY]->(family:Family {id: $participantId})
            WITH user, tree, COALESCE(person, family) AS participant

            MATCH (event:Event)-[rel:HAS_PARTICIPANT]->(participant)
            WITH event, participant, rel.relationship AS relationship
            OPTIONAL MATCH (event)-[otherRel:HAS_PARTICIPANT]->(others:Participant)
            OPTIONAL MATCH (event)-[:HAS_EVENT_CITATION]->(citation:Citation)
            RETURN event.id AS id,
                    event.type AS type,
                    event.date AS date,
                    event.place AS place,
                    event.description AS description,
                    relationship,
                    COLLECT({
                        name: COALESCE(others.name, participant.name),
                        id: COALESCE(others.id, participant.id),
                        relationship: COALESCE(otherRel.relationship, relationship)
                    }) AS participants,
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
}
