package com.ada.genealogyapp.event.repository;

import com.ada.genealogyapp.event.model.Event;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.UUID;

public interface EventRepository extends Neo4jRepository<Event, UUID> {

}
