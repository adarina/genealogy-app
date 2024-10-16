package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class FamilyRequest {

    private UUID fatherId;

    private UUID motherId;

    private Set<UUID> childrenIds = new HashSet<>();

    private Set<UUID> eventsIds = new HashSet<>();


    public static Function<FamilyRequest, Family> dtoToEntityMapper(
            Function<UUID, Person> personFinder, Function<UUID, Event> eventFinder) {
        return request -> {
            Person father = request.getFatherId() != null ? personFinder.apply(request.getFatherId()) : null;
            Person mother = request.getMotherId() != null ? personFinder.apply(request.getMotherId()) : null;
            Set<Person> children = request.getChildrenIds().stream()
                    .map(personFinder)
                    .collect(Collectors.toSet());
            Set<Event> events = request.getEventsIds().stream()
                    .map(eventFinder)
                    .collect(Collectors.toSet());

            return Family.builder()
                    .father(father)
                    .mother(mother)
                    .children(children)
                    .events(events)
                    .build();
        };
    }
}
