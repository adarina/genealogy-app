package com.ada.genealogyapp.family.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


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

}
