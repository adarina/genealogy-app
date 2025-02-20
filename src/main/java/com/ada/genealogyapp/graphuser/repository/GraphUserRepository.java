package com.ada.genealogyapp.graphuser.repository;


import com.ada.genealogyapp.graphuser.model.GraphUser;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphUserRepository extends Neo4jRepository<GraphUser, String> {
    @Query("""
            MERGE (user:GraphUser {id: $userId})
            """)
    String save(String userId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})
            RETURN user
            """)
    GraphUser find(String userId);
}
