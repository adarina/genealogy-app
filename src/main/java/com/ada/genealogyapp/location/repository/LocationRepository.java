package com.ada.genealogyapp.location.repository;


import com.ada.genealogyapp.location.dto.*;
import com.ada.genealogyapp.location.model.Location;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface LocationRepository extends Neo4jRepository<Location, String> {

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
                        MERGE (tree)-[:HAS_LOCATION]->(location:Location {id: locationId})
                        SET location.name = name,
                            location.type = type,
                            location.isMain = isMain,
                            location.latitude = latitude,
                            location.longitude = longitude
            
                        RETURN "LOCATION_CREATED" AS message
                    ',
                     userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, locationId: $locationId, name: $name, type: $type, isMain: $isMain, latitude: $latitude, longitude: $longitude}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String save(String userId, String treeId, String locationId, String name, String type, Boolean isMain, Double latitude, Double longitude);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
            
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
            
                OPTIONAL MATCH (tree)-[:HAS_LOCATION]->(location:Location {id: $locationId})
                RETURN userExist, treeExist, tree, count(location) > 0 AS locationExist, location
            }
            
            CALL apoc.do.case(
                [
                     userExist AND treeExist AND locationExist, '
                        SET location.name = name,
                            location.type = type,
                            location.latitude = latitude,
                            location.longitude = longitude
            
                        RETURN "LOCATION_UPDATED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "LOCATION_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {location: location, name: $name, type: $type, latitude: $latitude, longitude: $longitude}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String update(String userId, String treeId, String locationId, String name, String type, Double latitude, Double longitude);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
            
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
            
                OPTIONAL MATCH (tree)-[:HAS_LOCATION]->(location:Location {id: $locationId})
                WITH userExist, treeExist, tree, count(location) > 0 AS locationExist, location
            
                OPTIONAL MATCH (tree)-[:HAS_LOCATION]->(parent:Location {id: $parentId})
                RETURN userExist, treeExist, tree, locationExist, location, count(parent) > 0 AS parentExist, parent
            }
            
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND locationExist AND parentExist, '
                        OPTIONAL MATCH (location)-[r:LOCATED_IN]->()
                        DELETE r
                        WITH location, parent
                        MERGE (location)-[:LOCATED_IN]->(parent)
            
                        RETURN "PARENT_ADDED_TO_LOCATION" AS message
                    ',
                    userExist AND treeExist AND locationExist, 'RETURN "LOCATION_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "LOCATION_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {location: location, parent: parent}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String addParent(String userId, String treeId, String locationId, String parentId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_LOCATION]->(location:Location {id: $locationId})
            MATCH (child:Location)-[:LOCATED_IN]->(location)
            
            RETURN child.id AS id,
                   child.name AS name,
                   child.type AS type,
                   child.latitude AS latitude,
                   child.longitude AS longitude
            """)
    Set<LocationResponse> findLocationsIn(String userId, String treeId, String locationId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_LOCATION]->(location:Location {id: $locationId})
            OPTIONAL MATCH (location)<-[:HAS_EVENT_LOCATION]-(event:Event)
            RETURN location.id AS id,
                   location.name AS name,
                   location.type AS type,
                   location.latitude AS latitude,
                   location.longitude AS longitude,
                   count(event) AS amount
            """)
    LocationResponse find(String userId, String treeId, String locationId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_LOCATION]->(location:Location {id: $locationId})
            OPTIONAL MATCH (location)-[:LOCATED_IN*0..]->(parentLocation:Location)
            RETURN location.id AS id,
                   location.name AS name,
                   location.type AS type,
                   location.latitude AS latitude,
                   location.longitude AS longitude,
                   collect({id: parentLocation.id, name: parentLocation.name, type: parentLocation.type, latitude: parentLocation.latitude, longitude: parentLocation.longitude}) AS parentLocations
            """)
    LocationWithParentsResponse findParents(String userId, String treeId, String locationId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_LOCATION]->(location:Location)
            WITH location
            WHERE location.isMain = true
            RETURN location.id AS id,
                   location.name AS name,
                   location.type AS type,
                   location.latitude AS latitude,
                   location.longitude AS longitude
            """)
    List<LocationResponse> findMain(String userId, String treeId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_LOCATION]->(location:Location)
                WHERE
                    (toLower(location.name) CONTAINS toLower($name) OR $name = '')
                    AND (toUpper(location.type) = toUpper($type) OR $type = '')
                OPTIONAL MATCH (location)<-[:HAS_EVENT_LOCATION]-(event:Event)
                RETURN location.id AS id,
                    location.name AS name,
                    location.type AS type,
                    location.latitude AS latitude,
                    location.longitude AS longitude,
                    count(event) AS amount
            """)
    List<GeographyResponse> find(String userId, String treeId, String name, String type);


    @Query(value = """
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_LOCATION]->(location:Location)
            
            RETURN location.id AS id,
                   location.name AS name,
                   location.type AS type,
                   location.isMain AS isMain,
                   location.latitude AS latitude,
                   location.longitude AS longitude,
                   [(location)-[:LOCATED_IN]->(parentLocation:Location) | parentLocation.id][0] AS locationId
            """)
    Set<LocationExportResponse> find(String userId, String treeId);

    @Query("""
            MATCH (user:GraphUser {id: $userId})-[:HAS_TREE]->(tree:Tree {id: $treeId})-[:HAS_LOCATION]->(location:Location {id: $locationId})-[:LOCATED_IN]->(parent:Location)
            RETURN parent.id AS id,
                   parent.name AS name,
                   parent.type AS type,
                   parent.latitude AS latitude,
                   parent.longitude AS longitude
            """)
    LocationResponse findParent(String userId, String treeId, String locationId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
            
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
            
                OPTIONAL MATCH (tree)-[:HAS_LOCATION]->(location:Location {id: $locationId})
                WITH userExist, treeExist, tree, count(location) > 0 AS locationExist, location
            
                OPTIONAL MATCH (location)-[parentRel:LOCATED_IN]->(parent:Location {id: $parentId})
                RETURN userExist, treeExist, tree, locationExist, location, count(parent) > 0 AS parentExist, parent, parentRel
            }
            
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND locationExist AND parentExist, '
                        DELETE parentRel
                        SET location.isMain = true
                        RETURN "PARENT_REMOVED_FROM_LOCATION" AS message
                    ',
                    userExist AND treeExist AND locationExist, 'RETURN "PARENT_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "LOCATION_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {parentRel: parentRel, location: location}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String removeParent(String userId, String treeId, String locationId, String parentId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
            
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
            
                OPTIONAL MATCH (tree)-[:HAS_LOCATION]->(location:Location {id: $locationId})
                WITH userExist, treeExist, tree, count(location) > 0 AS locationExist, location
            
                OPTIONAL MATCH (tree)-[:HAS_LOCATION]->(parent:Location {id: $parentId})
                RETURN userExist, treeExist, tree, locationExist, location, count(parent) > 0 AS parentExist, parent
            }
            
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND locationExist AND parentExist, '
                         SET parent.name = name,
                             parent.type = type,
                             parent.latitude = latitude,
                             parent.longitude = longitude
                        WITH location, parent
                        OPTIONAL MATCH (location)-[r:LOCATED_IN]->()
                        DELETE r
                        WITH location, parent
                        MERGE (location)-[:LOCATED_IN]->(parent)
                        WITH location
                        SET location.isMain = false
            
                        RETURN "LOCATION_UPDATED" AS message
                    ',
                    userExist AND treeExist AND locationExist, 'RETURN "PARENT_NOT_EXIST" AS message',
                    userExist AND treeExist, 'RETURN "EVENT_NOT_EXIST" AS message',
                    userExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {location: location, name: $name, type: $type, latitude: $latitude, longitude: $longitude, parent: parent}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String update(String userId, String treeId, String locationId, String name, String type, Double latitude, Double longitude, String parentId);

    @Query("""
            CALL {
                OPTIONAL MATCH (user:GraphUser {id: $userId})
                WITH count(user) > 0 AS userExist
            
                OPTIONAL MATCH (user)-[:HAS_TREE]->(tree:Tree {id: $treeId})
                WITH userExist, count(tree) > 0 AS treeExist, tree
            
                OPTIONAL MATCH (tree)-[:HAS_LOCATION]->(location:Location {id: $locationId})
                RETURN userExist, treeExist, tree, count(location) > 0 AS locationExist, location
            }
            
            CALL apoc.do.case(
                [
                    userExist AND treeExist AND locationExist, '
                        MATCH (location)<-[:LOCATED_IN*0..]-(child:Location)
                        DETACH DELETE child
                        DELETE location
                        RETURN "LOCATION_DELETED" AS message
                    ',
                    userExist AND treeExist, 'RETURN "LOCATION_NOT_EXIST" AS message',
                    treeExist, 'RETURN "TREE_NOT_EXIST" AS message'
                ],
                'RETURN "USER_NOT_EXIST" AS message',
                {tree: tree, location: location}
            ) YIELD value
            RETURN value.message
            LIMIT 1
            """)
    String delete(String userId, String treeId, String locationId);

    @Query("""
                MATCH (t:Tree {id: $treeId})
                MERGE (l:Location {name: $name, type: $type})<-[:HAS_LOCATION]-(t)
                ON CREATE SET
                    l.id = randomUUID(),
                    l.isMain = $isMain,
                    l.latitude = $latitude,
                    l.longitude = $longitude
                RETURN l
            """)
    Location findOrCreateTopLevelLocation(String treeId, String name, String type, boolean isMain, Double latitude, Double longitude);

    @Query("""
                MATCH (t:Tree {id: $treeId})
                MATCH (parent:Location {id: $parentId})
                MERGE (l:Location {name: $name, type: $type})<-[:HAS_LOCATION]-(t)
                ON CREATE SET
                    l.id = randomUUID(),
                    l.isMain = $isMain
                MERGE (parent)-[:LOCATED_IN]->(l)
                RETURN l
            """)
    Location findOrCreateChildLocation(String treeId, String parentId, String name, String type, boolean isMain);
}
