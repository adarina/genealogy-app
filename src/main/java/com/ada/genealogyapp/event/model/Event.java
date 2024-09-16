package com.ada.genealogyapp.event.model;


import com.ada.genealogyapp.person.model.Person;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import java.util.HashSet;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@ToString
@Node
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue
    private UUID id;

    public Type type;

    private LocalDate eventDate;

    private String place;

    private String description;


//    @Relationship(type = "PARTICIPATES_IN", direction = Relationship.Direction.INCOMING)
//    private Set<Person> participants = new HashSet<>();




}
