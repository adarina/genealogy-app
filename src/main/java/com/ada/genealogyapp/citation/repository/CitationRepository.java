package com.ada.genealogyapp.citation.repository;

import com.ada.genealogyapp.citation.dto.CitationSourceResponse;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.file.dto.FileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;


public interface CitationRepository extends Neo4jRepository<Citation, String> {


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
                        MERGE (tree)-[:HAS_CITATION]->(citation:Citation {id: citationId})
                        SET citation.page = page,
                            citation.date = date
                            
                        RETURN "CITATION_CREATED" AS message
                    ',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, citationId: $citationId, page: $page, date: $date}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String save(String userId, String treeId, String citationId, String page, String date);

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
                        MERGE (tree)-[:HAS_CITATION]->(citation:Citation {id: citationId})
                        SET citation.page = page,
                            citation.date = date
                            
                        MERGE (citation)-[:HAS_CITATION_SOURCE]->(:Source {id: sourceId})
                        MERGE (:Event {id: eventId})-[:HAS_EVENT_CITATION]->(citation)
                            
                        RETURN "CITATION_CREATED" AS message
                    ',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, citationId: $citationId, page: $page, date: $date, sourceId: $sourceId, eventId: $eventId}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String save(String userId, String treeId, String citationId, String page, String date, String sourceId, String eventId);
    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist, '
                        MERGE (tree)-[:HAS_CITATION]->(citation:Citation {id: citationId})
                        SET citation.page = page,
                            citation.date = date
                        
                        WITH tree, citation, sourceId, filesIds
                        OPTIONAL MATCH (tree)-[:HAS_SOURCE]->(source:Source {id: sourceId})
                        MERGE (citation)-[:HAS_CITATION_SOURCE]->(source)
                        
                        WITH tree, citation, filesIds
                        UNWIND filesIds AS fileId
                        OPTIONAL MATCH (tree)-[:HAS_FILE]->(file:File {id: fileId})
                        MERGE (citation)-[:HAS_CITATION_FILE]->(file)
                            
                        RETURN "CITATION_CREATED" AS message
                    ',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, citationId: $citationId, page: $page, date: $date, sourceId: $sourceId, filesIds: $filesIds}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String save(String userId, String citationId, String page, String date, String treeId, String sourceId, List<String> filesIds);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_CITATION]->(citation:Citation {id: $citationId})
                RETURN userExist, treeExist, tree, count(citation) > 0 AS citationExist, citation
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND citationExist, '
                        OPTIONAL MATCH (citation)-[rel]-()
                        DELETE rel, citation
                        RETURN "CITATION_DELETED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "CITATION_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, citation: citation}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String delete(String userId, String treeId, String citationId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_CITATION]->(citation:Citation {id: $citationId})
                RETURN userExist, treeExist, tree, count(citation) > 0 AS citationExist, citation
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND citationExist, '
                        SET citation.page = $page,
                            citation.date = $date
                            
                        RETURN "CITATION_UPDATED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "CITATION_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {citation: citation, page: $page, date: $date}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String update(String userId, String treeId, String citationId, String page, String date);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_CITATION]->(citation:Citation {id: $citationId})
                WITH userExist, treeExist, tree, count(citation) > 0 AS citationExist, citation
                
                OPTIONAL MATCH (tree)-[:HAS_FILE]->(file:File {id: $fileId})
                RETURN userExist, treeExist, tree, citationExist, citation, count(file) > 0 AS fileExist, file
            }
                        
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND citationExist AND fileExist, '
                        MERGE (citation)-[:HAS_CITATION_FILE]->(file)
                       
                        RETURN "FILE_ADDED_TO_CITATION" AS message
                    ',
                    userExist AND treeExist AND citationExist, 'RETURN "FILE_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "CITATION_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {citation: citation, file: file}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String addFile(String userId, String treeId, String citationId, String fileId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_CITATION]->(citation:Citation {id: $citationId})
                WITH userExist, treeExist, tree, count(citation) > 0 AS citationExist, citation
                
                OPTIONAL MATCH (tree)-[:HAS_SOURCE]->(source:Source {id: $sourceId})
                RETURN userExist, treeExist, tree, citationExist, citation, count(source) > 0 AS sourceExist, source
            }
                        
            CALL apoc.do.case(
                [
                    userExist, treeExist AND citationExist AND sourceExist, '
                        MERGE (citation)-[:HAS_CITATION_SOURCE]->(source)
                       
                        RETURN "SOURCE_ADDED_TO_CITATION" AS message
                    ',
                    userExist AND treeExist AND citationExist, 'RETURN "SOURCE_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "CITATION_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {citation: citation, source: source}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String addSource(String userId, String treeId, String citationId, String sourceId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_CITATION]->(citation:Citation {id: $citationId})
                WITH userExist, treeExist, tree, count(citation) > 0 AS citationExist, citation
                
                OPTIONAL MATCH (citation)-[fileRel:HAS_CITATION_FILE]->(file:File {id: $fileId})
                RETURN userExist, treeExist, tree, citationExist, citation, count(file) > 0 AS fileExist, file, fileRel
            }
                        
            CALL apoc.do.case(
                [
                    userExist, treeExist AND citationExist AND fileExist, '
                        DELETE fileRel
                        RETURN "FILE_REMOVED_FROM_CITATION" AS message
                    ',
                    userExist AND treeExist AND citationExist, 'RETURN "FILE_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "CITATION_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {citation: citation, file: file, fileRel: fileRel}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String removeFile(String userId, String treeId, String citationId, String fileId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
                
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
                
                OPTIONAL MATCH (tree)-[:HAS_CITATION]->(citation:Citation {id: $citationId})
                WITH userExist, treeExist, tree, count(citation) > 0 AS citationExist, citation
                
                OPTIONAL MATCH (citation)-[sourceRel:HAS_CITATION_SOURCE]->(source:Source {id: $sourceId})
                RETURN userExist, treeExist, tree, citationExist, citation, count(source) > 0 AS sourceExist, source, sourceRel
            }
                        
            CALL apoc.do.case(
                [
                    userExist, treeExist AND citationExist AND sourceExist, '
                        DELETE sourceRel
                        RETURN "SOURCE_REMOVED_FROM_CITATION" AS message
                    ',
                    userExist AND treeExist AND citationExist, 'RETURN "SOURCE_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "CITATION_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {citation: citation, source: source, sourceRel: sourceRel}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String removeSource(String userId, String treeId, String citationId, String sourceId);

    @Query(value = """
            MATCH (t:Tree)-[:HAS_CITATION]->(c:Citation)
            WHERE t.id = $treeId
            AND (toLower(c.page) CONTAINS toLower($page) OR $page = '')
            WITH c
            OPTIONAL MATCH (c)-[:HAS_CITATION_SOURCE]->(s:Source)
            WITH c, s
            RETURN c.id AS id,
                   c.page AS page,
                   c.date AS date,
                   s.name AS name
                   :#{orderBy(#pageable)}
                   SKIP $skip
                   LIMIT $limit
                   """,
            countQuery = """
                    MATCH (t:Tree)-[:HAS_CITATION]->(c:Citation)
                    WHERE t.id = $treeId
                    RETURN count(c)
                    """)
    Page<CitationSourceResponse> findByTreeIdAndFilteredPage(@Param("treeId") String treeId, String page, Pageable pageable);

    @Query("""
                MATCH (c:Citation {id: $citationId})
                MATCH (t:Tree {id: $treeId})
                MERGE (t)-[:HAS_CITATION]->(c)
                WITH c
                OPTIONAL MATCH (c)-[:HAS_CITATION_SOURCE]->(s:Source)
                WITH c, s
                RETURN c.id AS id,
                       c.page AS page,
                       c.date AS date,
                       s.name AS name,
                       s.id AS sourceId
            """)
    Optional<CitationSourceResponse> findByTreeIdAndCitationId(@Param("treeId") String treeId, @Param("citationId") String citationId);

    @Query(value = """
            MATCH (f:File)<-[:HAS_CITATION_FILE]-(c:Citation)
            WHERE c.id = $citationId
            WITH f
            RETURN f.id AS id,
                   f.name AS name,
                   f.type AS type,
                   'http://localhost:8080/upload-dir/' + f.filename AS path
            :#{orderBy(#pageable)}
            SKIP $skip
            LIMIT $limit
            """,
            countQuery = """
                        MATCH (f:File)<-[:HAS_CITATION_FILE]-(c:Citation)
                        WHERE c.id = $citationId
                        RETURN count(f)
                    """
    )
    Page<FileResponse> findCitationFiles(String citationId, Pageable pageable);

}
